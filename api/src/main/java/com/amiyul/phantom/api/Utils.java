/*
 * Add Copyright
 */
package com.amiyul.phantom.api;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.Properties;

import com.amiyul.phantom.api.config.ConfigFileParser;

public class Utils {
	
	private static Properties PROPERTIES;
	
	protected static final String BUILD_PROP_FILE = "build.properties";
	
	protected static final String BUILD_PROP_VERSION = "version";
	
	static {
		try (InputStream file = Utils.class.getClassLoader().getResourceAsStream(BUILD_PROP_FILE)) {
			PROPERTIES = new Properties();
			PROPERTIES.load(file);
		}
		catch (IOException e) {
			throw new RuntimeException("Failed to load the build properties file: ", e);
		}
	}
	
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
	
	/**
	 * Gets the input stream for the specified file
	 * 
	 * @param file file to open
	 * @return FileInputStream
	 * @throws FileNotFoundException
	 */
	public static FileInputStream getInputStream(File file) throws FileNotFoundException {
		return new FileInputStream(file);
	}
	
	/**
	 * Loads the class object matching the specified class name
	 * 
	 * @param className class name to load
	 * @return Class object
	 * @throws ClassNotFoundException
	 */
	public static Class<?> loadClass(String className) throws ClassNotFoundException {
		return Thread.currentThread().getContextClassLoader().loadClass(className);
	}
	
	/**
	 * Gets the build version
	 *
	 * @return build version
	 */
	public static String getVersion() {
		String version = PROPERTIES.getProperty(BUILD_PROP_VERSION);
		if (Utils.isBlank(version)) {
			throw new RuntimeException("Failed to determine the build version");
		}
		
		return version;
	}
	
}
