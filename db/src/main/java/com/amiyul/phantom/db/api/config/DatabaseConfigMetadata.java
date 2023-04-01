/*
 * Add Copyright
 */
package com.amiyul.phantom.db.api.config;

import java.util.List;

import com.amiyul.phantom.api.config.ConfigMetadata;
import com.amiyul.phantom.db.api.DatabaseMetadata;

/**
 * Holds the metadata used to build a {@link DatabaseConfig} instance
 */
public interface DatabaseConfigMetadata extends ConfigMetadata {
	
	/**
	 * Gets the {@link DatabaseMetadata} list for all configured target databases
	 * 
	 * @return list
	 */
	List<DatabaseMetadata> getDatabaseMetadata();
	
}
