/*
 * Add Copyright
 */
package com.amiyul.phantom.api.config;

import com.amiyul.phantom.api.SystemUtils;
import com.amiyul.phantom.api.Utils;

/**
 * Contains config utilities
 */
public class ConfigUtils {
	
	/**
	 * Gets the path to a config file set via the specified system or environmental property name
	 *
	 * @param propertyName the system or environmental property name
	 * @return path to the config file
	 */
	public synchronized static String getConfigFile(String propertyName) {
		String configFile = System.getProperty(propertyName);
		
		if (Utils.isBlank(configFile)) {
			configFile = SystemUtils.getEnv(propertyName);
		}
		
		return configFile;
	}
	
}
