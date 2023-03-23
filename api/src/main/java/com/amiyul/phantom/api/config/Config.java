/*
 * Add Copyright
 */
package com.amiyul.phantom.api.config;

import com.amiyul.phantom.api.Server;
import com.amiyul.phantom.api.logging.DriverLogger.LoggingApi;

/**
 * Marker interface for the driver configuration
 */
public interface Config {
	
	Server getServer();
	
	/**
	 * Gets the loggingApi
	 *
	 * @return the loggingApi
	 */
	LoggingApi getLoggingApi();
	
}
