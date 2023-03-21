/*
 * Add Copyright
 */
package com.amiyul.driver.phantom.config;

import com.amiyul.driver.phantom.DatabaseMetadata;
import com.amiyul.driver.phantom.logging.DriverLogger.LoggingApi;

/**
 * Marker interface for the driver configuration
 */
public interface Config {
	
	/**
	 * Gets the loggingApi
	 *
	 * @return the loggingApi
	 */
	LoggingApi getLoggingApi();
	
	/**
	 * Gets the {@link DatabaseMetadata} for the database matching the specified name
	 *
	 * @return name the database name
	 */
	DatabaseMetadata getDatabaseMetadata(String name);
	
}
