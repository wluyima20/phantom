/*
 * Add Copyright
 */
package com.amiyul.phantom.api.config;

import com.amiyul.phantom.api.DatabaseProvider;

/**
 * Holds the raw metadata read from a config file and can be used to build a {@link Config} instance
 */
public interface ConfigMetadata {
	
	/**
	 * Gets the {@link DatabaseProvider} class object
	 *
	 * @return
	 */
	Class<DatabaseProvider> getDatabaseProviderClass();
	
	/**
	 * Gets the loggingApi name
	 *
	 * @return the loggingApi name
	 */
	String getLoggingApi();
	
}
