package com.amiyul.phantom.driver;

import static com.amiyul.phantom.driver.PhantomLogger.LOGGER;
import static com.amiyul.phantom.driver.Utils.isBlank;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.DriverPropertyInfo;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Logger;

public final class PhantomDriver implements Driver {
	
	protected static final String URL_SUFFIX = "phantom:";
	
	protected static final String URL_PREFIX = "jdbc:" + URL_SUFFIX;
	
	static {
		LOGGER.info("Registering Phantom Driver...");
		
		try {
			DriverManager.registerDriver(new PhantomDriver());
			//TODO Spawn a new thread that periodically pings the db connection
			//service to keep local caches up to date and minimise failures
		}
		catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	@Override
	public Connection connect(String url, Properties info) throws SQLException {
		if (!acceptsURL(url)) {
			//This is required as per the jdbc spec if a driver realizes it isn't appropriate for the url
			return null;
		}
		
		/*
		 * Server info lookup order
		 *  1. Check in memory cache
		 *  2. Check File system cache - This is persistent between application restarts
		 *  3. Check the db server info service as the final source of truth
		 */
		try {
			String logicalName = Utils.extractNameFromUrl(url);
			
			LOGGER.info("Logical name: " + logicalName + " from url " + url);
			
			String hostAndPort = Utils.getServerInfo(logicalName);
			
			boolean isServerInfoFromService = false;
			boolean updateCaches = false;
			
			if (isBlank(hostAndPort)) {
				hostAndPort = Utils.lookupFromDbService(logicalName);
				
				if (isBlank(hostAndPort)) {
					throw new Exception("No DB server info found for logical name: " + logicalName);
				} else {
					isServerInfoFromService = true;
					
					LOGGER.info(
					    "Using DB server info from service: " + hostAndPort + " for " + "logical name: " + logicalName);
				}
			}
			
			String resolvedUrl = Utils.resolveUrl(url, logicalName, hostAndPort);
			
			LOGGER.info("Resolved Connection url: " + resolvedUrl);
			
			try {
				Connection conn = DriverManager.getDriver(resolvedUrl).connect(resolvedUrl, info);
				if (isServerInfoFromService) {
					updateCaches = true;
				}
				
				return conn;
				
			}
			catch (SQLException e) {
				if (!isServerInfoFromService) {
					LOGGER.warning("Failed to obtain Connection to url: " + resolvedUrl + ", attempting failover");
					
					String updatedHostAndPort = Utils.lookupFromDbService(logicalName);
					if (isBlank(updatedHostAndPort)) {
						throw new Exception("No DB server info found for logical name: " + logicalName);
					}
					
					if (hostAndPort.equalsIgnoreCase(updatedHostAndPort)) {
						throw e;
					}
					
					hostAndPort = updatedHostAndPort;
					
					LOGGER.info("Obtaining connection to DB with logical name: " + logicalName + " at new " + "server "
					        + hostAndPort);
					
					resolvedUrl = Utils.resolveUrl(url, logicalName, hostAndPort);
					
					LOGGER.info("New resolved Connection url: " + resolvedUrl);
					
					Connection conn = DriverManager.getDriver(resolvedUrl).connect(resolvedUrl, info);
					updateCaches = true;
					
					return conn;
				}
				
				throw e;
				
			}
			finally {
				if (updateCaches) {
					Utils.updateCaches(logicalName, hostAndPort);
				}
			}
			
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
		return 1;
	}
	
	@Override
	public int getMinorVersion() {
		return 0;
	}
	
	@Override
	public boolean jdbcCompliant() {
		return true;
	}
	
	@Override
	public Logger getParentLogger() {
		return LOGGER;
	}
	
}
