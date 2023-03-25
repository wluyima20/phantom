/*
 * Add Copyright
 */
package com.amiyul.phantom.driver.impl.config;

import java.io.File;
import java.io.FileInputStream;

import com.amiyul.phantom.api.config.ConfigFileParser;
import com.amiyul.phantom.api.config.ConfigMetadata;
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
	public ConfigMetadata parse(FileInputStream configInputStream) {
		return null;
	}
	
}
