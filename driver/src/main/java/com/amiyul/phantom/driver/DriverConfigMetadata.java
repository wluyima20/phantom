/*
 * Add Copyright
 */
package com.amiyul.phantom.driver;

import com.amiyul.phantom.api.DatabaseProvider;
import com.amiyul.phantom.api.config.ConfigMetadata;
import com.amiyul.phantom.api.Stateful;

/**
 * Holds the metadata used to build a {@link DriverConfig} instance
 */
public interface DriverConfigMetadata extends ConfigMetadata {
	
	/**
	 * Gets the class name for the {@link DatabaseProvider}
	 *
	 * @return class
	 */
	String getDatabaseProviderClassName();
	
	/**
	 * @see Stateful
	 */
	String getDownUntil();
	
}
