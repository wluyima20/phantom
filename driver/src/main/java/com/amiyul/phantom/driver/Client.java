/*
 * Add Copyright
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
