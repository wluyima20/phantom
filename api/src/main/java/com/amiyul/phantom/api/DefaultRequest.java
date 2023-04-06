/*
 * Add Copyright
 */
package com.amiyul.phantom.api;

import com.amiyul.phantom.api.PhantomProtocol.Command;

/**
 * Default {@link Request} implementation
 */
public class DefaultRequest implements Request {
	
	private final Command command;
	
	private final RequestContext context;
	
	public DefaultRequest(Command command, RequestContext context) {
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
