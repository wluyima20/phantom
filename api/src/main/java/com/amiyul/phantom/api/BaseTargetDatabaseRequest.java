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
