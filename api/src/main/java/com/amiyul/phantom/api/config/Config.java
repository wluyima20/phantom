/*
 * Add Copyright
 */
package com.amiyul.phantom.api.config;

import com.amiyul.phantom.api.Database;

/**
 * Marker interface for the driver configuration
 */
public interface Config {
	
	Database getDatabase();
	
}
