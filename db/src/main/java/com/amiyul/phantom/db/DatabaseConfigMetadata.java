/*
 * Add Copyright
 */
package com.amiyul.phantom.db;

import java.util.List;

import com.amiyul.phantom.api.config.ConfigMetadata;

/**
 * Holds the metadata used to build a {@link DatabaseConfig} instance
 */
public interface DatabaseConfigMetadata extends ConfigMetadata {
	
	/**
	 * Gets the {@link DatabaseDefinition} list for all configured target databases
	 * 
	 * @return list of database definitions
	 */
	List<DatabaseDefinition> getDatabaseDefinitions();
	
}
