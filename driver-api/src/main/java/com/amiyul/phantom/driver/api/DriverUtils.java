/*
 * Add Copyright
 */
package com.amiyul.phantom.driver.api;

import java.sql.Connection;
import java.sql.SQLException;

import com.amiyul.phantom.api.config.ConfigUtils;
import com.amiyul.phantom.api.logging.LoggerUtils;

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
		try {
			return DefaultClient.getInstance().connect(targetDatabaseKey);
		}
		catch (SQLException e) {
			LoggerUtils.debug(
			    "Reloading config after failed attempt to obtain a connection to database with key: " + targetDatabaseKey);
			
			ConfigUtils.discardConfig();
			
			return DefaultClient.getInstance().connect(targetDatabaseKey);
		}
	}
	
}
