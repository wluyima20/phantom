/*
 * Copyright [yyyy] Amiyul LLC
 * All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
