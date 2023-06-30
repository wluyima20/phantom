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
package com.amiyul.phantom.driver;

import static com.amiyul.phantom.api.logging.LoggerUtils.debug;

import java.sql.Connection;

/**
 * Processes delayed connection requests.
 */
public class DelayedConnectTask extends BaseCallableTask<Connection> {
	
	protected static final String NAME_PREFIX = "delayed-connect-task:";
	
	private ConnectionRequestData requestData;
	
	public DelayedConnectTask(ConnectionRequestData requestData) {
		super(NAME_PREFIX + requestData.getTargetDbName());
		this.requestData = requestData;
	}
	
	@Override
	public Connection doCall() throws Exception {
		debug("Processing delayed connection request");
		
		return DefaultClient.getInstance().doConnect(requestData);
	}
	
}
