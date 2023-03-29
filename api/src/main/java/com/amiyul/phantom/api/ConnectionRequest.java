/*
 * Add Copyright
 */
package com.amiyul.phantom.api;

import static com.amiyul.phantom.api.PhantomProtocol.Command;

import lombok.Getter;

/**
 * Encapsulates data for a request for a connection object
 */
public class ConnectionRequest extends BaseRequest {
	
	@Getter
	private final String targetDatabaseKey;
	
	public ConnectionRequest(String targetDatabaseKey) {
		super(Command.CONNECT);
		this.targetDatabaseKey = targetDatabaseKey;
	}
	
}
