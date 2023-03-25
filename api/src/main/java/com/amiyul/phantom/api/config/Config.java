/*
 * Add Copyright
 */
package com.amiyul.phantom.api.config;

import com.amiyul.phantom.api.Database;
import com.amiyul.phantom.api.logging.DriverLogger.LoggingApi;

/**
 * Marker interface for the driver configuration
 */
public interface Config {
	
	Database getDatabase();
	
	/**
	 * Gets the loggingApi
	 *
	 * @return the loggingApi
	 */
	LoggingApi getLoggingApi();
	
}
