/*
 * Add Copyright
 */
package com.amiyul.phantom.api.config;

import com.amiyul.phantom.api.Server;

public interface ServerMetadata {
	
	Class<Server> getServerClass();
	
	Object getProperties();
}
