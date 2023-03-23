/*
 * Add Copyright
 */
package com.amiyul.phantom.api;

import static com.amiyul.phantom.api.Utils.isBlank;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverPropertyInfo;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.Properties;
import java.util.logging.Logger;

import com.amiyul.phantom.api.config.Config;
import com.amiyul.phantom.api.logging.DriverLogger;

public final class PhantomDriver implements Driver {
	
	private static DriverLogger LOGGER;
	
	protected static final String DRIVER_NAME = "phantom";
	
	protected static final String URL_PREFIX = "jdbc:" + DRIVER_NAME + "://";
	
	private static Config config;
	
	@Override
	public Connection connect(String url, Properties info) throws SQLException {
		if (!acceptsURL(url)) {
			//This is required as per the jdbc spec if a driver realizes it isn't appropriate for the url
			return null;
		}
		
		try {
			if (config == null) {
				LOGGER.info("Loading " + DRIVER_NAME + " driver configuration");
				config = Utils.loadConfig();
			}
			
			String id = url.substring(url.indexOf(URL_PREFIX) + URL_PREFIX.length());
			
			LOGGER.debug("Extracted database id: " + id);
			
			DatabaseMetadata metadata = config.getDatabaseMetadata(id);
			Connection connection;
			if (metadata != null) {
				connection = Utils.getConnection(metadata, true);
				if (connection != null) {
					return connection;
				}
			}
			
			LOGGER.debug("Failed to obtain Connection to " + metadata + ", reloading metadata");
			
			config = Utils.loadConfig();
			
			DatabaseMetadata reloadedMetadata = config.getDatabaseMetadata(id);
			//TODO get the timeout and keep trying again before failing
			if (reloadedMetadata == null || reloadedMetadata.equals(metadata)) {
				throw new SQLException("No database metadata found matching id: " + id);
			}
			
			return Utils.getConnection(reloadedMetadata, false);
		}
		catch (Exception e) {
			throw new SQLException(e);
		}
	}
	
	@Override
	public boolean acceptsURL(String url) {
		if (isBlank(url)) {
			return false;
		}
		
		return url.startsWith(URL_PREFIX);
	}
	
	@Override
	public DriverPropertyInfo[] getPropertyInfo(String url, Properties info) {
		return new DriverPropertyInfo[] {};
	}
	
	@Override
	public int getMajorVersion() {
		//TODO read it from project version info
		return 1;
	}
	
	@Override
	public int getMinorVersion() {
		//TODO read it from project version info
		return 0;
	}
	
	@Override
	public boolean jdbcCompliant() {
		return false;
	}
	
	@Override
	public Logger getParentLogger() throws SQLFeatureNotSupportedException {
		throw new SQLFeatureNotSupportedException();
	}
	
}
