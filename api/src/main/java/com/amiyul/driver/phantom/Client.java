/*
 * Add Copyright
 */
package com.amiyul.driver.phantom;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Simulation of a database client to be used by the {@link PhantomDriver} to request for
 * connections from the {@link com.amiyul.driver.phantom.server.Server}
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
