/*
 * Add Copyright
 */
package com.amiyul.phantom.driver;

import static com.amiyul.phantom.driver.DriverConstants.PROP_ASYNC;
import static com.amiyul.phantom.driver.DriverConstants.PROP_ASYNC_LISTENER;
import static com.amiyul.phantom.driver.DriverConstants.URL_PREFIX;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import com.amiyul.phantom.api.Utils;
import com.amiyul.phantom.api.logging.LoggerUtils;

/**
 * Contains utilities used by the driver
 */
public class DriverUtils {
	
	/**
	 * Obtains a connection object to the target database
	 * 
	 * @param url database URL
	 * @param props Properties object
	 * @return Connection object
	 * @throws SQLException
	 */
	protected static Connection connect(String url, Properties props) throws SQLException {
		ConnectionRequestData connReqData = createRequest(url.trim(), props);
		final String targetDbName = connReqData.getTargetDatabaseName();
		Connection connection;
		
		try {
			connection = DefaultClient.getInstance().connect(connReqData);
		}
		catch (SQLException e) {
			LoggerUtils.debug(
			    "Reloading config after failed attempt to obtain a connection to the database named: " + targetDbName);
			
			DriverConfigUtils.reloadConfig();
			
			connection = DefaultClient.getInstance().connect(connReqData);
		}
		
		if (connection == null) {
			throw new SQLException("No connection obtained to the database named: " + targetDbName);
		}
		
		return connection;
	}
	
	private static ConnectionRequestData createRequest(String url, Properties props) throws SQLException {
		//Example URL -> //jdbc:phantom://test?async=true;asyncConnectionListener=myClass
		final int qnMarkIndex = URL_PREFIX.indexOf(DriverConstants.URL_DB_PARAM_SEPARATOR);
		String prefixAndName;
		Boolean async = null;
		if (props.contains(PROP_ASYNC)) {
			async = "true".equalsIgnoreCase(props.getProperty(PROP_ASYNC));
		}
		
		if (qnMarkIndex > -1) {
			prefixAndName = url.substring(0, qnMarkIndex);
			String[] params = url.substring(qnMarkIndex).split(DriverConstants.URL_PARAMS_SEPARATOR);
			for (String pair : params) {
				String[] keyAndValue = pair.split(DriverConstants.URL_PARAM_KEY_VALUE_SEPARATOR);
				String key = keyAndValue[0];
				if (!DriverConstants.PROP_NAMES.contains(key)) {
					throw new SQLException("Connection URL contains unsupported parameter named: " + key);
				}
				
				String value = keyAndValue[1];
				if (async == null && PROP_ASYNC.equals(key)) {
					async = Boolean.valueOf(value);
				} else if (PROP_ASYNC_LISTENER.equals(key)) {
					//TODO Set listener
				}
			}
		} else {
			prefixAndName = url;
		}
		
		String targetDbName = prefixAndName.substring(prefixAndName.indexOf(URL_PREFIX) + URL_PREFIX.length());
		
		LoggerUtils.debug("Extracted target database name: " + targetDbName);
		
		if (Utils.isBlank(targetDbName)) {
			throw new SQLException("No target database name defined in the database URL");
		}
		
		return new ConnectionRequestData(targetDbName, async);
	}
	
}
