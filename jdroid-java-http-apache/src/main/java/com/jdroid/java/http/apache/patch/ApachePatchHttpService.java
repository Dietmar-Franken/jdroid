package com.jdroid.java.http.apache.patch;

import com.jdroid.java.http.HttpMethod;
import com.jdroid.java.http.HttpServiceProcessor;
import com.jdroid.java.http.Server;
import com.jdroid.java.http.apache.ApacheBodyEnclosingHttpService;
import com.jdroid.java.http.apache.HttpClientFactory;

import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;

import java.util.List;

public class ApachePatchHttpService extends ApacheBodyEnclosingHttpService {
	
	public ApachePatchHttpService(HttpClientFactory httpClientFactory, Server baseURL, List<Object> urlSegments,
								  List<HttpServiceProcessor> httpServiceProcessors) {
		super(httpClientFactory, baseURL, urlSegments, httpServiceProcessors);
	}
	
	@Override
	public HttpMethod getHttpMethod() {
		return HttpMethod.PATCH;
	}
	
	/**
	 * @see ApacheBodyEnclosingHttpService#createHttpEntityEnclosingRequestBase(java.lang.String)
	 */
	@Override
	protected HttpEntityEnclosingRequestBase createHttpEntityEnclosingRequestBase(String uri) {
		return new HttpPatch(uri);
	}
}
