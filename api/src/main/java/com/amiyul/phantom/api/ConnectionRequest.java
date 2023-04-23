/*
 * Add Copyright
 */
package com.amiyul.phantom.api;

import static com.amiyul.phantom.api.PhantomProtocol.Command;

/**
 * Encapsulates data for a request for a connection object
 */
public class ConnectionRequest extends BaseTargetDatabaseRequest {
	
	public ConnectionRequest(String targetDatabaseName, RequestContext context) {
		super(targetDatabaseName, Command.CONNECT, context);
	}
	
}
