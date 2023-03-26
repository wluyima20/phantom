/*
 * Add Copyright
 */
package com.amiyul.phantom.driver.api;

import com.amiyul.phantom.api.logging.DriverLogger;

public class DriverUtils {
	
	protected static DriverLogger LOGGER;
	
	private static Client client;
	
	protected static Client getClient() {
		//TODO Create client
		return client;
	}
	
}
