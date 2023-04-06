/*
 * Add Copyright
 */
package com.amiyul.phantom.db;

import java.util.Map;

import com.amiyul.phantom.api.config.Config;

/**
 * Marker interface for the database configuration
 */
public interface DatabaseConfig extends Config {
	
	/**
	 * Gets the map of name and {@link DatabaseDefinition} for the target databases
	 * 
	 * @return Map
	 */
	Map<String, DatabaseDefinition> getDatabaseDefinitions();
	
}
