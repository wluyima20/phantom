/*
 * Add Copyright
 */
package com.amiyul.phantom.api;

public class SystemUtils {
	
	/**
	 * Gets the value of the environment variable matching the specified name
	 * 
	 * @param name the name of environment variable
	 * @return the value
	 */
	public static String getEnv(String name) {
		return System.getenv(name);
	}
	
}
