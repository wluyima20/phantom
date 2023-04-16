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
	private final boolean async;
	
	@Getter
	private final ConnectionListener listener;
	
	public ConnectionRequestData(String targetDatabaseName, boolean async, ConnectionListener listener) {
		this.targetDatabaseName = targetDatabaseName;
		this.async = async;
		this.listener = listener;
	}
	
	@Override
	public String toString() {
		return "{" + "targetDatabaseName=" + targetDatabaseName + ", async=" + async + ", listener=" + listener + "}";
	}
	
}
