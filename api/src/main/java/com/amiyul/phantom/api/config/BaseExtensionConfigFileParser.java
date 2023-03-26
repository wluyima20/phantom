/*
 * Add Copyright
 */
package com.amiyul.phantom.api.config;

import java.io.File;

/**
 * {@link ConfigFileParser} that checks the config file extension to determine if it can parse the
 * file
 */
public abstract class BaseExtensionConfigFileParser implements ConfigFileParser {
	
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
