/*
 * Add Copyright
 */
package com.amiyul.phantom.api;

import static com.amiyul.phantom.api.PhantomProtocol.Command;

import lombok.Getter;

/**
 * Encapsulates data for a request to be sent to a specific target database
 */
public abstract class BaseTargetDatabaseRequest extends DefaultRequest {
	
	@Getter
	private final String targetDatabaseName;
	
	public BaseTargetDatabaseRequest(String targetDatabaseName, Command command, RequestContext context) {
		super(command, context);
		this.targetDatabaseName = targetDatabaseName;
	}
	
}
