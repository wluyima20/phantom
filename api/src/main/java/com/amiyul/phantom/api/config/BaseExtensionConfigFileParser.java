/*
 * Add Copyright
 */
package com.amiyul.phantom.api.config;

import java.io.File;

/**
 * Base class for ConfigFileParsers that check the config file extension to determine if they can
 * parse the file
 */
public abstract class BaseExtensionConfigFileParser<T> implements ConfigFileParser<T> {
	
	@Override
	public boolean canParse(File configFile) {
		return configFile.getName().endsWith("." + getExtension(configFile));
	}
	
	/**
	 * Gets the supported file extension
	 * 
	 * @param configFile config file
	 * @return file extension
	 */
	public abstract String getExtension(File configFile);
	
}
