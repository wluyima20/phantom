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
	 * Gets the {@link DatabaseMetadata} for the database matching the specified id
	 *
	 * @return id the database id
	 */
	DatabaseMetadata getDatabaseMetadata(String databaseId);
	
	/**
	 * Gets the loggingApi
	 *
	 * @return the loggingApi
	 */
	LoggingApi getLoggingApi();
	
}
