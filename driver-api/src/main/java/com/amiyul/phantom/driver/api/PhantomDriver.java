/*
 * Add Copyright
 */
package com.amiyul.phantom.driver.api;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.DriverPropertyInfo;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.Properties;
import java.util.logging.Logger;

import com.amiyul.phantom.api.Constants;
import com.amiyul.phantom.api.Utils;
import com.amiyul.phantom.api.logging.JavaLogger;
import com.amiyul.phantom.api.logging.LoggerUtils;

public final class PhantomDriver implements Driver {
	
	protected static final String URL_PREFIX = "jdbc:" + Constants.DATABASE_NAME + "://";
	
	static {
		try {
			DriverManager.registerDriver(new PhantomDriver());
		}
		catch (SQLException e) {
			throw new RuntimeException("Failed to register driver");
		}
	}
	
	@Override
	public Connection connect(String url, Properties info) throws SQLException {
		if (!acceptsURL(url)) {
			//This is required as per the jdbc spec if a driver realizes it isn't appropriate for the url
			return null;
		}
		
		try {
			String dbKey = url.substring(url.indexOf(URL_PREFIX) + URL_PREFIX.length());
			
			LoggerUtils.debug("Extracted database key: " + dbKey);
			
			return DriverUtils.getClient().connect(dbKey);
		}
		catch (Exception e) {
			throw new SQLException(e);
		}
	}
	
	@Override
	public boolean acceptsURL(String url) {
		if (Utils.isBlank(url)) {
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
		if (LoggerUtils.isUsingJavaLogger()) {
			return JavaLogger.getInstance().getNativeLogger();
		}
		
		throw new SQLFeatureNotSupportedException();
	}
	
}
