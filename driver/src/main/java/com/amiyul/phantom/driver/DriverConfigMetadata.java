/*
 * Add Copyright
 */
package com.amiyul.phantom.driver;

import com.amiyul.phantom.api.DatabaseProvider;
import com.amiyul.phantom.api.config.ConfigMetadata;

/**
 * Holds the metadata used to build a {@link DriverConfig} instance
 */
public interface DriverConfigMetadata extends ConfigMetadata {

    /**
	 * Gets the {@link DatabaseProvider} class object
	 *
	 * @return class
	 */
	Class<DatabaseProvider> getDatabaseProviderClass();
	
	/**
	 * @see com.amiyul.phantom.api.Maintainable
	 */
	String getUnderMaintenanceUntil();
	
}
