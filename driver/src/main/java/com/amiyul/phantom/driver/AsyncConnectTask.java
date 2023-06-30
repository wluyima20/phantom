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

import java.sql.Connection;

/**
 * Processes asynchronous connection requests and notifies the {@link Listener} on success otherwise
 * on failure.
 */
public class AsyncConnectTask extends BaseNotifyingTask<Connection> {
	
	protected static final String NAME_PREFIX = "async-connect-task:";
	
	private ConnectionRequestData requestData;
	
	public AsyncConnectTask(ConnectionRequestData requestData) {
		super(NAME_PREFIX + requestData.getTargetDbName(), requestData.getListener());
		this.requestData = requestData;
	}
	
	@Override
	public Connection getResult() throws Exception {
		return DefaultClient.getInstance().doConnectInternal(requestData);
	}
	
}
