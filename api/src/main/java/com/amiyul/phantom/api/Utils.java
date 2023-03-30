/*
 * Add Copyright
 */
package com.amiyul.phantom.api;

public class Utils {
	
	/**
	 * Checks if the specified string is null or an empty string or a white space character.
	 * 
	 * @param s the string to check
	 * @return true if the string is blank otherwise false
	 */
	public static boolean isBlank(String s) {
		return s == null || s.trim().isEmpty();
	}
	
	/**
	 * Gets the path to a file configured via the specified system or environmental property name
	 *
	 * @param propertyName the system or environmental property name
	 * @return path to the config file
	 */
	public synchronized static String getFilePath(String propertyName) {
		String configFile = System.getProperty(propertyName);
		
		if (isBlank(configFile)) {
			configFile = SystemUtils.getEnv(propertyName);
		}
		
		return configFile;
	}
	
}
