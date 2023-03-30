/*
 * Add Copyright
 */
package com.amiyul.phantom.api.config;

import com.amiyul.phantom.api.DatabaseProvider;

/**
 * Holds the raw metadata read from a config file and can be used to build a {@link DriverConfig}
 * instance
 */
public interface ConfigMetadata {
	
	String PROP_DB_PROVIDER_CLASS = "databaseProviderClass";
	
	/**
	 * Gets the {@link DatabaseProvider} class object
	 *
	 * @return class
	 */
	Class<DatabaseProvider> getDatabaseProviderClass();
	
}
