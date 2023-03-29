/*
 * Add Copyright
 */
package com.amiyul.phantom.driver.api;

import java.sql.Connection;
import java.sql.SQLException;

public class DriverUtils {
	
	private static Client client;
	
	private synchronized static Client getClient() {
		//TODO Create client
		return client;
	}
	
	/**
	 * Obtains a connection object to a target database matching the specified key
	 * 
	 * @param targetDatabaseKey the target database key
	 * @return Connection
	 * @throws SQLException
	 */
	protected static Connection connect(String targetDatabaseKey) throws SQLException {
		return getClient().connect(targetDatabaseKey);
	}
	
}
