package com.jdroid.gradle.android

import com.jdroid.gradle.commons.JavaBaseGradleExtension

public class AndroidGradlePluginExtension extends JavaBaseGradleExtension {

	String[] resourcesDirsPaths = ['src/main/res/']
	String[] notDefaultLanguages = []
	String missingTranslationExpression = "#TODO#"

	public AndroidGradlePluginExtension(AndroidGradlePlugin androidGradlePlugin) {
		super(androidGradlePlugin)
	}

	public void setString(def flavor, String propertyName, String defaultValue) {
		def value = getProp(propertyName, defaultValue)
		def stringValue = value == null ? "null" : '"' + value + '"'
		flavor.buildConfigField "String", propertyName, stringValue
	}

	public void setBoolean(def flavor, String propertyName, Boolean defaultValue) {
		def value = getProp(propertyName, defaultValue).toString()
		flavor.buildConfigField "Boolean", propertyName, value
	}

	public void setInteger(def flavor, String propertyName, Integer defaultValue) {
		def value = getProp(propertyName, defaultValue).toString()
		flavor.buildConfigField "Integer", propertyName, value
	}
}