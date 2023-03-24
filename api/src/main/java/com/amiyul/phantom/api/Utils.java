/*
 * Add Copyright
 */
package com.amiyul.phantom.api;

import com.amiyul.phantom.api.logging.DriverLogger;

public class Utils {
	
	protected static DriverLogger LOGGER;
	
	/**
	 * Checks if the specified string is null or an empty string or a white space character.
	 * 
	 * @param s the string to check
	 * @return true if the string is blank otherwise false
	 */
	public static boolean isBlank(String s) {
		return s == null || s.trim().isEmpty();
	}
	
}
