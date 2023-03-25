/*
 * Add Copyright
 */
package com.amiyul.phantom.driver.impl.config;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

import com.amiyul.phantom.api.config.ConfigFileParser;
import com.amiyul.phantom.api.config.ConfigMetadata;
import com.amiyul.phantom.api.config.ConfigUtils;
import com.amiyul.phantom.api.config.ExtensionBasedConfigFileParser;

/**
 * {@link ConfigFileParser} for a properties file
 */
public class PropertiesFileParser extends ExtensionBasedConfigFileParser {
	
	@Override
	public String getExtension(File configFile) {
		return "properties";
	}
	
	@Override
	public ConfigMetadata parse(FileInputStream configInputStream) throws Exception {
		Properties props = new Properties();
		props.load(configInputStream);
		String providerClassName = props.getProperty(ConfigMetadata.PROP_DB_PROVIDER_CLASS);
		String loggingApi = props.getProperty(ConfigMetadata.PROP_LOGGING_API);
		return ConfigUtils.createMetadata(providerClassName, loggingApi);
	}
	
}
