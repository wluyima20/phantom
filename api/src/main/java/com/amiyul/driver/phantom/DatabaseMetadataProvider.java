/*
 * Add Copyright
 */
package com.amiyul.driver.phantom;

import java.util.Map;

/**
 * Super interface for {@link DatabaseMetadata} providers
 */
public interface DatabaseMetadataProvider extends Disableable {
	
	/**
	 * Gets a map of database id and {@link DatabaseMetadata} mappings
	 * 
	 * @return Map of database id and {@link DatabaseMetadata}
	 */
	Map<String, DatabaseMetadata> getDatabaseMetadata();
	
	/**
	 * Loads the database metadata
	 */
	void load();
	
}
