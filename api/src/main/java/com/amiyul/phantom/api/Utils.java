/*
 * Add Copyright
 */
package com.amiyul.phantom.api;

import java.io.File;
import java.util.Iterator;

import com.amiyul.phantom.api.config.ConfigFileParser;

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
	public static String getFilePath(String propertyName) {
		String configFile = System.getProperty(propertyName);
		
		if (isBlank(configFile)) {
			configFile = SystemUtils.getEnv(propertyName);
		}
		
		return configFile;
	}
	
	/**
	 * Gets a {@link ConfigFileParser} via the service loader mechanism that can parse the specified
	 * file
	 * 
	 * @param clazz The parser class
	 * @param file the file to parse
	 * @return ConfigFileParser instance
	 */
	public static <T extends ConfigFileParser> T getParser(Class<T> clazz, File file) {
		Iterator<T> parsers = ServiceLoaderUtils.getProviders(clazz);
		while (parsers.hasNext()) {
			T parser = parsers.next();
			if (parser.canParse(file)) {
				return parser;
			}
		}
		
		return null;
	}
	
}
