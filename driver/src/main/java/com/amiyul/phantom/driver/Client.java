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
package com.amiyul.phantom.driver;

import java.sql.Connection;
import java.sql.SQLException;

import com.amiyul.phantom.api.Status;

/**
 * An implementation of this class is used by the {@link PhantomDriver} to communicate with the
 * phantom {@link com.amiyul.phantom.api.Database} using the
 * {@link com.amiyul.phantom.api.PhantomProtocol}.
 */
public interface Client {
	
	/**
	 * Requests a connection from the database, i.e. implements the database
	 * {@link com.amiyul.phantom.api.PhantomProtocol.Command#CONNECT} command.
	 *
	 * @param requestData the {@link ConnectionRequestData} instance
	 * @return Connection object
	 * @throws SQLException
	 */
	Connection connect(ConnectionRequestData requestData) throws SQLException;
	
	/**
	 * Sends a reload signal to the database, i.e. implements the database
	 * {@link com.amiyul.phantom.api.PhantomProtocol.Command#RELOAD} command.
	 * 
	 * @throws SQLException
	 */
	void reload() throws SQLException;
	
	/**
	 * Gets the {@link Status} for the database matching the specified name, i.e. implements the
	 * database {@link com.amiyul.phantom.api.PhantomProtocol.Command#STATUS} command.
	 *
	 * @param targetDatabaseName the name of the target database
	 * @return the {@link Status}
	 * @throws SQLException
	 */
	Status getStatus(String targetDatabaseName) throws SQLException;
	
}
