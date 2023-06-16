/*
 * Add Copyright
 */
package com.amiyul.phantom.api;

import static java.time.ZoneId.systemDefault;
import static java.time.format.DateTimeFormatter.ISO_OFFSET_DATE_TIME;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.lang.reflect.Method;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.temporal.Temporal;
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
	 * @param <T>
	 * @return Class object
	 * @throws ClassNotFoundException
	 */
	public static <T> Class<T> loadClass(String className) throws ClassNotFoundException {
		return (Class<T>) Thread.currentThread().getContextClassLoader().loadClass(className);
	}
	
	/**
	 * Parses the specified local date time string
	 * 
	 * @param date date string
	 * @return LocalDateTime object
	 */
	public static LocalDateTime parseDateString(String date) {
		return ZonedDateTime.parse(date, ISO_OFFSET_DATE_TIME).withZoneSameInstant(systemDefault()).toLocalDateTime();
	}
	
	/**
	 * Tests if a {@link LocalDateTime} object is after another {@link LocalDateTime} object with null
	 * being the earliest date
	 * 
	 * @param date the date object
	 * @param other the other date to compare to
	 * @return true if the date is not null and is after other date otherwise false
	 */
	public static boolean isDateAfter(LocalDateTime date, LocalDateTime other) {
		if (other == null) {
			throw new RuntimeException("other date can't be null");
		}
		
		return date != null && date.isAfter(other);
	}
	
	/**
	 * Tests if the specified method is a hashCode method
	 * 
	 * @param method {@link Method} object
	 * @return true if it is a hashCode method otherwise false
	 */
	public static boolean isHashCodeMethod(Method method) {
		return "hashCode".equals(method.getName()) && method.getParameterTypes().length == 0;
	}
	
	/**
	 * Tests if the specified method is a toString method
	 *
	 * @param method {@link Method} object
	 * @return true if it is a toString method otherwise false
	 */
	public static boolean isToStringMethod(Method method) {
		return "toString".equals(method.getName()) && method.getParameterTypes().length == 0;
	}
	
	/**
	 * Tests if the specified method is an equals method
	 *
	 * @param method {@link Method} object
	 * @return true if it is an equals method otherwise false
	 */
	public static boolean isEqualsMethod(Method method) {
		if ("equals".equals(method.getName())) {
			Class<?>[] parameterTypes = method.getParameterTypes();
			return parameterTypes.length == 1 && parameterTypes[0] == Object.class;
		}
		
		return false;
	}
	
	/**
	 * @see Duration#between(Temporal, Temporal)
	 */
	public static Duration getDurationBetween(LocalDateTime start, LocalDateTime end) {
		return Duration.between(start, end);
	}
	
}
