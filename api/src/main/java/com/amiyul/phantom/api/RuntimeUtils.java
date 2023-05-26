/*
 * Add Copyright
 */
package com.amiyul.phantom.api;

public class RuntimeUtils {
	
	/**
	 * Gets the number of available processors
	 *
	 * @return the number of processors
	 */
	public static int getAvailableProcessors() {
		return Runtime.getRuntime().availableProcessors();
	}
}
