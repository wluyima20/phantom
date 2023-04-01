/*
 * Add Copyright
 */
package com.amiyul.phantom.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import com.amiyul.phantom.api.ConnectionRequest;

/**
 * Contains utilities used by the {@link FileDatabase}
 */
public class DatabaseUtils {
	
	/**
	 * Obtains a connection object to a target database defined on the specified
	 * {@link ConnectionRequest} object
	 *
	 * @param connectionRequest {@link ConnectionRequest}
	 * @return Connection
	 * @throws SQLException
	 */
	protected static Connection getConnection(ConnectionRequest connectionRequest) throws SQLException {
		final String dbName = connectionRequest.getTargetDatabaseName();
		DatabaseMetadata dbMetadata = DatabaseConfigUtils.getConfig().getDatabaseMetadata().get(dbName);
		if (dbMetadata == null) {
			throw new SQLException("No target database found matching the name: " + dbName);
		}
		
		return DriverManager.getConnection(dbMetadata.getUrl(), dbMetadata.getProperties());
	}
	
}
