/*
 * Add Copyright
 */
package com.amiyul.phantom.api;

import static com.amiyul.phantom.api.PhantomProtocol.Command;

/**
 * Encapsulates data for a request to be sent to check if a target database is available.
 */
public class StatusRequest extends BaseTargetDatabaseRequest {
	
	public StatusRequest(String targetDatabaseName, RequestContext context) {
		super(targetDatabaseName, Command.STATUS, context);
	}
	
}
