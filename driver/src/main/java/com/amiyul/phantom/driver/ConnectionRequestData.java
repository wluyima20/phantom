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

import lombok.Getter;

/**
 * Encapsulates information about a request for a connection
 */
public class ConnectionRequestData {
	
	@Getter
	private final String targetDbName;
	
	@Getter
	private final boolean async;
	
	@Getter
	private final ConnectionListener listener;
	
	public ConnectionRequestData(String targetDbName, boolean async, ConnectionListener listener) {
		this.targetDbName = targetDbName;
		this.async = async;
		this.listener = listener;
	}
	
	@Override
	public String toString() {
		return "{" + "targetDbName=" + targetDbName + ", async=" + async + ", listener=" + listener + "}";
	}
	
}
