/*
 * Add Copyright
 */
package com.amiyul.phantom.db;

import java.sql.DriverManager;
import java.sql.SQLException;

import com.amiyul.phantom.api.ConnectionRequest;
import com.amiyul.phantom.api.logging.LoggerUtils;

/**
 * Contains utilities used by the {@link FileDatabase}
 */
public class DatabaseUtils {
	
	/**
	 * Obtains a connection object to a target database defined on the specified
	 * {@link ConnectionRequest} object and writes the obtained connection as the response result.
	 *
	 * @param request {@link ConnectionRequest}
	 * @throws SQLException
	 */
	protected static void getConnection(ConnectionRequest request) throws SQLException {
		final String dbName = request.getTargetDatabaseName();
		DatabaseDefinition ref = DatabaseConfigUtils.getConfig().getDatabaseDefinitions().get(dbName);
		if (ref == null) {
			throw new SQLException("No target database found matching the name: " + dbName);
		}
		
		LoggerUtils.debug("Obtaining connection to target database at -> " + ref.getUrl());
		
		request.getContext().writeResult(DriverManager.getConnection(ref.getUrl(), ref.getProperties()));
	}
	
}
