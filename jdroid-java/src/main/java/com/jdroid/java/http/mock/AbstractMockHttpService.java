package com.jdroid.java.http.mock;

import com.jdroid.java.collections.Maps;
import com.jdroid.java.concurrent.ExecutorUtils;
import com.jdroid.java.exception.UnexpectedException;
import com.jdroid.java.http.HttpServiceProcessor;
import com.jdroid.java.http.MultipartHttpService;
import com.jdroid.java.http.HttpService;
import com.jdroid.java.http.post.BodyEnclosingHttpService;
import com.jdroid.java.parser.Parser;
import com.jdroid.java.utils.FileUtils;
import com.jdroid.java.utils.LoggerUtils;
import com.jdroid.java.utils.StringUtils;

import org.slf4j.Logger;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Collection;
import java.util.Map;

/**
 * Mocked {@link HttpService} and {@link BodyEnclosingHttpService} implementation that returns mocked responses
 */
public abstract class AbstractMockHttpService implements MultipartHttpService {
	
	private static final Logger LOGGER = LoggerUtils.getLogger(AbstractMockHttpService.class);
	
	private static final String MOCK_SEPARATOR = "_";
	private static final String SUFFIX_SEPARATOR = "-";
	private static final String URL_SEPARATOR = "/";
	
	private Object[] urlSegments;
	private Map<String, String> parameters = Maps.newHashMap();
	private Map<String, String> headers = Maps.newHashMap();
	private String body;
	
	public AbstractMockHttpService(Object... urlSegments) {
		this.urlSegments = urlSegments;
	}
	
	/**
	 * @see HttpService#execute(com.jdroid.java.parser.Parser)
	 */
	@SuppressWarnings({ "unchecked", "resource" })
	@Override
	public <T> T execute(Parser parser) {
		
		String filePath = generateMockFilePath(urlSegments);
		
		LOGGER.warn("Request: " + filePath);
		if (!parameters.isEmpty()) {
			LOGGER.warn("Parameters: " + parameters.toString());
		}
		if (!headers.isEmpty()) {
			LOGGER.warn("Headers: " + headers.toString());
		}
		
		if (StringUtils.isNotBlank(body)) {
			LOGGER.warn("HTTP Entity Body: " + body);
		}
		
		Integer httpMockSleep = getHttpMockSleepDuration(urlSegments);
		if ((httpMockSleep != null) && (httpMockSleep > 0)) {
			ExecutorUtils.sleep(httpMockSleep);
		}
		
		simulateCrash();
		
		if (parser != null) {
			InputStream inputStream = getClass().getClassLoader().getResourceAsStream(filePath);
			if (inputStream == null) {
				throw new UnexpectedException("The mocked file wasn't found");
			}
			// Parse the stream
			T t = (T)(parser.parse(inputStream));
			FileUtils.safeClose(inputStream);
			return t;
		} else {
			return null;
		}
	}
	
	/**
	 * @see HttpService#execute()
	 */
	@SuppressWarnings("unchecked")
	@Override
	public <T> T execute() {
		return (T)execute(null);
	}
	
	protected void simulateCrash() {
		// Do nothing by default
	}
	
	/**
	 * @see HttpService#addHeader(java.lang.String, java.lang.String)
	 */
	@Override
	public void addHeader(String name, String value) {
		if (value != null) {
			headers.put(name, value);
		}
	}
	
	/**
	 * @see HttpService#addQueryParameter(java.lang.String, java.lang.Object)
	 */
	@Override
	public void addQueryParameter(String name, Object value) {
		if (value != null) {
			parameters.put(name, value.toString());
		}
	}
	
	/**
	 * @see HttpService#addQueryParameter(java.lang.String, java.util.Collection)
	 */
	@Override
	public void addQueryParameter(String name, Collection<?> values) {
		addQueryParameter(name, StringUtils.join(values));
	}
	
	/**
	 * @see MultipartHttpService#addPart(java.lang.String, java.io.ByteArrayInputStream,
	 *      java.lang.String, java.lang.String)
	 */
	@Override
	public void addPart(String name, ByteArrayInputStream in, String mimeType, String filename) {
		// Do Nothing
	}
	
	/**
	 * @see MultipartHttpService#addPart(java.lang.String, java.lang.Object, java.lang.String)
	 */
	@Override
	public void addPart(String name, Object value, String mimeType) {
		// Do Nothing
	}
	
	/**
	 * @see MultipartHttpService#addJsonPart(java.lang.String, java.lang.Object)
	 */
	@Override
	public void addJsonPart(String name, Object value) {
		// Do Nothing
	}
	
	/**
	 * @see HttpService#addUrlSegment(java.lang.Object)
	 */
	@Override
	public void addUrlSegment(Object segment) {
		// Do Nothing
	}
	
	/**
	 * @see HttpService#addHttpServiceProcessor(HttpServiceProcessor)
	 */
	@Override
	public void addHttpServiceProcessor(HttpServiceProcessor httpServiceProcessor) {
		// Do Nothing
	}
	
	/**
	 * @see BodyEnclosingHttpService#setBody(String)
	 */
	@Override
	public void setBody(String body) {
		this.body = body;
	}
	
	/**
	 * @see HttpService#setConnectionTimeout(java.lang.Integer)
	 */
	@Override
	public void setConnectionTimeout(Integer connectionTimeout) {
		// Do Nothing
	}

	@Override
	public void setReadTimeout(Integer readTimeout) {
		// Do Nothing
	}
	
	/**
	 * @see HttpService#setUserAgent(java.lang.String)
	 */
	@Override
	public void setUserAgent(String userAgent) {
		// Do Nothing
	}
	
	/**
	 * @see HttpService#setSsl(java.lang.Boolean)
	 */
	@Override
	public void setSsl(Boolean ssl) {
		// Do Nothing
	}
	
	/**
	 * @return The time to sleep (in seconds) to simulate the execution of the request
	 */
	protected abstract Integer getHttpMockSleepDuration(Object... urlSegments);
	
	/**
	 * @see HttpService#getUrl()
	 */
	@Override
	public String getUrl() {
		return generateMockFilePath(urlSegments);
	}
	
	/**
	 * @see HttpService#getUrlSuffix()
	 */
	@Override
	public String getUrlSuffix() {
		return null;
	}
	
	protected String generateMockFilePath(Object... urlSegments) {
		StringBuilder sb = new StringBuilder(getMocksBasePath());
		if (urlSegments != null) {
			for (Object urlSegment : urlSegments) {
				sb.append(urlSegment.toString().replaceAll(URL_SEPARATOR, MOCK_SEPARATOR));
				sb.append(MOCK_SEPARATOR);
			}
		}
		sb.deleteCharAt(sb.length() - 1);
		
		String suffix = getSuffix(sb.toString());
		if (StringUtils.isNotBlank(suffix)) {
			sb.append(SUFFIX_SEPARATOR);
			sb.append(suffix);
		}
		sb.append(getMocksExtension());
		return sb.toString();
	}
	
	/**
	 * @return The mocks base path
	 */
	protected abstract String getMocksBasePath();
	
	/**
	 * @return The mocks extension
	 */
	protected abstract String getMocksExtension();
	
	/**
	 * @return The suffix to add to the mock file
	 */
	protected String getSuffix(String path) {
		return null;
	}

	@Override
	public String getHeaderValue(String key) {
		return headers.get(key);
	}
}
