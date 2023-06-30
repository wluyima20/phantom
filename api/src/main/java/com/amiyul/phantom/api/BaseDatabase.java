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

import java.sql.Connection;
import java.sql.SQLException;

import com.amiyul.phantom.api.PhantomProtocol.Command;

/**
 * Base class for {@link Database} implementations.
 */
public abstract class BaseDatabase implements Database {
	
	@Override
	public void process(RequestContext context) throws SQLException {
		final Command command = context.getRequest().getCommand();
		switch (command) {
			case CONNECT:
				context.writeResult(getConnection(((ConnectionRequest) context.getRequest()).getTargetDatabaseName()));
				break;
			case RELOAD:
				reload();
				break;
			case STATUS:
				context.writeResult(getStatus(((StatusRequest) context.getRequest()).getTargetDatabaseName()));
				break;
			default:
				throw new SQLException("Don't know how to process protocol command: " + command);
		}
	}
	
	/**
	 * Gets a connection to a database matching the specified name
	 *
	 * @param targetDatabaseName the name of the target database
	 * @return Connection object
	 * @throws SQLException
	 */
	public abstract Connection getConnection(String targetDatabaseName) throws SQLException;
	
	/**
	 * Processes a reload request
	 */
	public abstract void reload() throws SQLException;
	
	/**
	 * Processes a status request for a database matching the specified name
	 *
	 * @param targetDatabaseName the name of the target database
	 * @return Status object
	 * @throws SQLException
	 */
	public abstract Status getStatus(String targetDatabaseName) throws SQLException;
	
}
