/*
 * Add Copyright
 */
package com.amiyul.phantom.driver.api.config;

import com.amiyul.phantom.api.Database;
import com.amiyul.phantom.api.config.Config;

/**
 * Marker interface for the driver configuration
 */
public interface DriverConfig extends Config {
	
	Database getDatabase();
	
}
