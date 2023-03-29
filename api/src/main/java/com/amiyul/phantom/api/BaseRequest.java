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
	
	public BaseRequest(Command command) {
		this.command = command;
	}
	
	@Override
	public Command getCommand() {
		return command;
	}
	
}
