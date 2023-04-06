/*
 * Add Copyright
 */
package com.amiyul.phantom.driver;

import com.amiyul.phantom.api.Database;
import com.amiyul.phantom.api.config.Config;

/**
 * Marker interface for the driver configuration
 */
public interface DriverConfig extends Config {
	
	Database getDatabase();
	
}
