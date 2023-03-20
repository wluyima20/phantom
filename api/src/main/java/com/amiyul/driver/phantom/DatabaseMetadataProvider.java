/*
 * Add Copyright
 */
package com.amiyul.driver.phantom;

import java.util.List;

/**
 * Super interface for {@link DatabaseMetadata} providers
 */
public interface DatabaseMetadataProvider extends Named, Disableable {
	
	/**
	 * Gets the {@link DatabaseMetadata} list of the databases known to this provider
	 * 
	 * @return list of {@link DatabaseMetadata}
	 */
	List<DatabaseMetadata> getMetadata();
	
	/**
	 * Reloads all the metadata
	 */
	void reload();
	
}
