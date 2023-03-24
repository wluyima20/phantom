/*
 * Add Copyright
 */
package com.amiyul.phantom.api;

import com.amiyul.phantom.api.logging.DriverLogger;

public class Utils {
	
	protected static DriverLogger LOGGER;
	
	/**
	 * Checks if the specified string is null or an empty string or a white space character.
	 * 
	 * @param s the string to check
	 * @return true if the string is blank otherwise false
	 */
	public static boolean isBlank(String s) {
		return s == null || s.trim().isEmpty();
	}
	
	/*protected static Connection getConnection(DatabaseMetadata metadata, boolean suppressExceptions) throws SQLException {
		LOGGER.debug("Obtaining connection for url: " + metadata.getUrl());
		
		try {
			Connection connection = DriverManager.getConnection(metadata.getUrl(), metadata.getProperties());
			//TODO call this asynchronously to avoid blocking for slow running provider listeners
			try {
				metadata.getProvider().onConnectionSuccess(metadata);
			}
			catch (Throwable t) {
				LOGGER.error(
				    "An error occurred while notifying a database metadata provider of a successful " + "connection", t);
			}
			
			return connection;
		}
		catch (SQLException e) {
			//TODO call this asynchronously to avoid blocking for slow running provider listeners
			try {
				metadata.getProvider().onConnectionFailure(metadata, e);
			}
			catch (Throwable t) {
				LOGGER.error("An error occurred while notifying a database metadata provider of a failed attempt "
				        + "to obtain a connection",
				    t);
			}
			
			if (!suppressExceptions) {
				throw e;
			}
			
			return null;
		}
	}*/
	
}
