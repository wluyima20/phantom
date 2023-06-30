/*
 * Copyright (C) Amiyul LLC - All Rights Reserved
 *
 * This source code is protected under international copyright law. All rights
 * reserved and protected by the copyright holder.
 * 
 * This file is confidential and only available to authorized individuals with the
 * permission of the copyright holder. If you encounter this file and do not have
 * permission, please contact the copyright holder and delete this file.
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
