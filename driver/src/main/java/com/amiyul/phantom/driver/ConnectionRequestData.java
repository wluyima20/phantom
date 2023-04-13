/*
 * Add Copyright
 */
package com.amiyul.phantom.driver;

import lombok.Getter;

/**
 * Encapsulates information about a request for a connection
 */
public class ConnectionRequestData {
	
	@Getter
	private final String targetDatabaseName;
	
	@Getter
	private final Boolean async;
	
	public ConnectionRequestData(String targetDatabaseName, Boolean async) {
		this.targetDatabaseName = targetDatabaseName;
		this.async = async;
	}
	
}
