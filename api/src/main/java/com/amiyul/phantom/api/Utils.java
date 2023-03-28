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
	
}
