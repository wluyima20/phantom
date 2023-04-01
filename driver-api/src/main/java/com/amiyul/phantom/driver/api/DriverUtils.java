/*
 * Add Copyright
 */
package com.amiyul.phantom.driver.api;

import java.sql.Connection;
import java.sql.SQLException;

import com.amiyul.phantom.api.Utils;
import com.amiyul.phantom.api.logging.LoggerUtils;

/**
 * Contains utilities used by the driver
 */
public class DriverUtils {
	
	/**
	 * Obtains a connection object to a target database matching the specified name
	 * 
	 * @param targetDbName the target database name
	 * @return Connection
	 * @throws SQLException
	 */
	protected static Connection connect(String targetDbName) throws SQLException {
		if (Utils.isBlank(targetDbName)) {
			throw new SQLException("No target database name defined in the database URL");
		}
		
		try {
			return DefaultClient.getInstance().connect(targetDbName);
		}
		catch (SQLException e) {
			LoggerUtils.debug(
			    "Reloading config after failed attempt to obtain a connection to the database named: " + targetDbName);
			
			DriverConfigUtils.discardConfig();
			
			return DefaultClient.getInstance().connect(targetDbName);
		}
	}
	
}
