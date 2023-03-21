/*
 * Add Copyright
 */
package com.amiyul.driver.phantom;

public class SystemUtils {
	
	/**
	 * Gets the value of the environment variable matching the specified name
	 * 
	 * @param name the name of environment variable
	 * @return the value
	 */
	protected static String getEnv(String name) {
		return System.getenv(name);
	}
	
}
