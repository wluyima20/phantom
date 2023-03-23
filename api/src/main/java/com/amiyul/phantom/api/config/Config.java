/*
 * Add Copyright
 */
package com.amiyul.phantom.api.config;

import com.amiyul.phantom.api.DatabaseMetadata;
import com.amiyul.phantom.api.logging.DriverLogger.LoggingApi;

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
