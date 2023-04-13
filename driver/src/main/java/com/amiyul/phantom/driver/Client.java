/*
 * Add Copyright
 */
package com.amiyul.phantom.driver;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Simulation of a database client to be used by the {@link PhantomDriver} to send requests to the
 * {@link com.amiyul.phantom.api.Database}
 */
public interface Client {
	
	/**
	 * Requests connection from the database
	 *
	 * @param requestData the {@link ConnectionRequestData} instance
	 * @return Connection object
	 * @throws SQLException
	 */
	Connection connect(ConnectionRequestData requestData) throws SQLException;
	
	/**
	 * Sends a reload signal to the database
	 * 
	 * @throws SQLException
	 */
	void reload() throws SQLException;
	
}
