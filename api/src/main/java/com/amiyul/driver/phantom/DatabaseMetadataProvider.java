/*
 * Add Copyright
 */
package com.amiyul.driver.phantom;

import java.sql.SQLException;
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
	
	/**
	 * Called when a connection is successfully obtained from the database
	 * 
	 * @param metadata {@link DatabaseMetadata} for the database
	 */
	default void onConnectionSuccess(DatabaseMetadata metadata) {
	}
	
	/**
	 * Called when an exception is encountered when attempting to obtain connection a connection to the
	 * database
	 * 
	 * @param metadata {@link DatabaseMetadata} for the database
	 * @param exception SQLException instance
	 */
	default void onConnectionFailure(DatabaseMetadata metadata, SQLException exception) {
	}
	
}
