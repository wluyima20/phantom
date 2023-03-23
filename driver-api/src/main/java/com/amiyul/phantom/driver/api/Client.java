/*
 * Add Copyright
 */
package com.amiyul.phantom.driver.api;

import java.sql.Connection;
import java.sql.SQLException;

import com.amiyul.phantom.api.PhantomDriver;
import com.amiyul.phantom.api.Server;

/**
 * Simulation of a database client to be used by the {@link PhantomDriver} to request for
 * connections from the {@link Server}
 */
public interface Client {
	
	/**
	 * Requests connection from the server
	 *
	 * @param databaseKey the unique key for the database
	 * @return Connection object
	 * @throws SQLException
	 */
	Connection connect(String databaseKey) throws SQLException;
	
}
