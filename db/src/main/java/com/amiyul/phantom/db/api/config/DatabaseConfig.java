/*
 * Add Copyright
 */
package com.amiyul.phantom.db.api.config;

import java.util.Map;

import com.amiyul.phantom.api.config.Config;
import com.amiyul.phantom.db.api.DatabaseMetadata;

/**
 * Marker interface for the database configuration
 */
public interface DatabaseConfig extends Config {
	
	/**
	 * Gets the map of name and {@link DatabaseMetadata} for the target databases
	 * 
	 * @return Map
	 */
	Map<String, DatabaseMetadata> getDatabaseMetadata();
	
}
