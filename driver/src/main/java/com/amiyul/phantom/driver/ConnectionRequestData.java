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
	private final String targetDbName;
	
	@Getter
	private final boolean async;
	
	@Getter
	private final ConnectionListener listener;
	
	public ConnectionRequestData(String targetDbName, boolean async, ConnectionListener listener) {
		this.targetDbName = targetDbName;
		this.async = async;
		this.listener = listener;
	}
	
	@Override
	public String toString() {
		return "{" + "targetDbName=" + targetDbName + ", async=" + async + ", listener=" + listener + "}";
	}
	
}
