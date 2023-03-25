/*
 * Add Copyright
 */
package com.amiyul.phantom.api.config;

import com.amiyul.phantom.api.Database;

public interface ServerMetadata {
	
	Class<Database> getDatabaseClass();
	
	Object getProperties();
}
