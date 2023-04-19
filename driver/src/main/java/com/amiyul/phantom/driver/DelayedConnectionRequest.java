/*
 * Add Copyright
 */
package com.amiyul.phantom.driver;

import java.sql.Connection;
import java.time.LocalDateTime;

/**
 * Encapsulate information about a delayed connection request.
 */
public class DelayedConnectionRequest extends BaseDelayedRequest<ConnectionRequestData, Connection> {
	
	public DelayedConnectionRequest(ConnectionRequestData requestData, LocalDateTime startTime) {
		super(requestData, startTime);
	}
	
}
