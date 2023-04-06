/*
 * Add Copyright
 */
package com.amiyul.phantom.api;

import static com.amiyul.phantom.api.PhantomProtocol.Command;

import lombok.Getter;

/**
 * Encapsulates data for a request for a connection object
 */
public class ConnectionRequest extends DefaultRequest {
	
	@Getter
	private final String targetDatabaseName;
	
	public ConnectionRequest(String targetDatabaseName, RequestContext context) {
		super(Command.CONNECT, context);
		this.targetDatabaseName = targetDatabaseName;
	}
	
}
