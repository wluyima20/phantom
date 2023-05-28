/*
 * Add Copyright
 */
package com.amiyul.phantom.driver;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.DriverPropertyInfo;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Logger;

import com.amiyul.phantom.api.Utils;
import com.amiyul.phantom.api.logging.JavaLogger;
import com.amiyul.phantom.api.logging.LoggerUtils;

public final class PhantomDriver implements Driver {
	
	private static Integer majorVersion;
	
	private static Integer minorVersion;
	
	static {
		try {
			DriverManager.registerDriver(new PhantomDriver());
			Runtime.getRuntime().addShutdownHook(new Thread(ShutdownHook.getInstance()));
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
			return DriverUtils.connect(url, info);
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
		
		return url.startsWith(DriverConstants.URL_PREFIX);
	}
	
	@Override
	public DriverPropertyInfo[] getPropertyInfo(String url, Properties info) {
		Map<DriverProperty, String> propValueMap = DriverUtils.createDriverPropertyAndValueMap(url, info);
		
		return propValueMap.entrySet().stream().map(e -> {
			DriverPropertyInfo propInfo = e.getKey().toDriverPropertyInfo();
			propInfo.value = e.getValue();
			return propInfo;
		}).toArray(DriverPropertyInfo[]::new);
	}
	
	@Override
	public int getMajorVersion() {
		if (majorVersion == null) {
			majorVersion = Integer.valueOf(Utils.getVersion().split("\\.")[0]);
		}
		
		return majorVersion;
	}
	
	@Override
	public int getMinorVersion() {
		if (minorVersion == null) {
			minorVersion = Integer.valueOf(Utils.getVersion().split("\\.")[1]);
		}
		
		return minorVersion;
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
