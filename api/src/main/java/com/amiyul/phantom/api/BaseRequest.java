/*
 * Add Copyright
 */
package com.amiyul.phantom.api;

import com.amiyul.phantom.api.PhantomProtocol.Command;

/**
 * Base class for Requests
 */
public abstract class BaseRequest implements Request {
	
	private final Command command;
	
	private final RequestContext context;
	
	public BaseRequest(Command command, RequestContext context) {
		this.command = command;
		this.context = context;
	}
	
	@Override
	public Command getCommand() {
		return command;
	}
	
	@Override
	public RequestContext getContext() {
		return context;
	}
	
}
