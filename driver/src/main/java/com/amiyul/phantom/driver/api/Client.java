/*
 * Add Copyright
 */
package com.amiyul.phantom.driver.api;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Simulation of a database client to be used by the {@link PhantomDriver} to send requests to the
 * {@link com.amiyul.phantom.api.Database}
 */
public interface Client {
	
	/**
	 * Requests connection from the database server
	 *
	 * @param targetDatabaseName the unique name for the target database
	 * @return Connection object
	 * @throws SQLException
	 */
	Connection connect(String targetDatabaseName) throws SQLException;
	
}
