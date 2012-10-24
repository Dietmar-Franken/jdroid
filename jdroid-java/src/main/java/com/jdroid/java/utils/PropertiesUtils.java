package com.jdroid.java.utils;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import com.jdroid.java.collections.Lists;
import com.jdroid.java.collections.Sets;
import com.jdroid.java.exception.UnexpectedException;

public abstract class PropertiesUtils {
	
	private static List<Properties> propertiesList = Lists.newArrayList();
	
	public static void loadProperties(String resourceName) {
		Properties properties = null;
		URL url = PropertiesUtils.class.getClassLoader().getResource(resourceName);
		if (url != null) {
			try {
				properties = new Properties();
				properties.load(url.openStream());
			} catch (IOException e) {
				throw new UnexpectedException("Cannot read from resource: " + resourceName, e);
			}
		}
		if (properties != null) {
			propertiesList.add(properties);
		}
	}
	
	private static String getStringProperty(Properties properties, String name) {
		return properties != null ? properties.getProperty(name) : null;
	}
	
	public static String getStringProperty(String name) {
		String value = null;
		for (Properties properties : propertiesList) {
			value = getStringProperty(properties, name);
			if (value != null) {
				return value;
			}
		}
		return value;
	}
	
	public static String getStringProperty(String name, String defaultValue) {
		String value = getStringProperty(name);
		return value != null ? value : defaultValue;
	}
	
	public static Integer getIntegerProperty(String name) {
		return PropertiesUtils.getIntegerProperty(name, null);
	}
	
	public static Integer getIntegerProperty(String name, Integer defaultValue) {
		String value = getStringProperty(name);
		return value != null ? Integer.valueOf(value) : defaultValue;
	}
	
	public static Boolean getBooleanProperty(String name) {
		return PropertiesUtils.getBooleanProperty(name, null);
	}
	
	public static Boolean getBooleanProperty(String name, Boolean defaultValue) {
		String value = getStringProperty(name);
		return value != null ? Boolean.valueOf(value) : defaultValue;
	}
	
	public static Set<String> getStringSetProperty(String name) {
		String value = getStringProperty(name);
		return Sets.newHashSet(StringUtils.splitToCollection(value));
	}
}