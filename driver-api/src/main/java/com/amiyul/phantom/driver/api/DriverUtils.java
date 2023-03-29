/*
 * Add Copyright
 */
package com.amiyul.phantom.driver.api;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Contains utilities used by the driver
 */
public class DriverUtils {
	
	/**
	 * Obtains a connection object to a target database matching the specified key
	 * 
	 * @param targetDatabaseKey the target database key
	 * @return Connection
	 * @throws SQLException
	 */
	protected static Connection connect(String targetDatabaseKey) throws SQLException {
		return DefaultClient.getInstance().connect(targetDatabaseKey);
	}
	
}
