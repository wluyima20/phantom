/*
 * Add Copyright
 */
package com.amiyul.phantom.driver;

import static com.amiyul.phantom.driver.DriverConstants.DEFAULT_THREAD_SIZE;
import static com.amiyul.phantom.driver.DriverConstants.PROP_DRIVER_CONN_LISTENER_CLASS;
import static com.amiyul.phantom.driver.DriverConstants.URL_PREFIX;
import static com.amiyul.phantom.driver.DriverProperty.ASYNC;
import static com.amiyul.phantom.driver.DriverProperty.CONNECTION_LISTENER;
import static com.amiyul.phantom.driver.DriverProperty.TARGET_DB;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;
import java.util.Properties;

import com.amiyul.phantom.api.RuntimeUtils;
import com.amiyul.phantom.api.Utils;
import com.amiyul.phantom.api.logging.LoggerUtils;

import lombok.SneakyThrows;

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
		ConnectionRequestData requestData = createRequest(url.trim(), props);
		
		LoggerUtils.debug("Connection request data -> " + requestData);
		
		return DefaultClient.getInstance().connect(requestData);
	}
	
	/**
	 * Gets the default thread count for the executor for async request processor
	 *
	 * @return thread count
	 */
	protected static int getDefaultAsyncExecutorThreadCount() {
		int threadCount = getDefaultThreadCount();
		if (threadCount > 50) {
			threadCount = 50;
		}
		
		return threadCount;
	}
	
	/**
	 * Gets the default thread count for the executor for delayed request processor
	 * 
	 * @return thread count
	 */
	protected static int getDefaultDelayedExecutorThreadCount() {
		int threadCount = getDefaultThreadCount();
		if (threadCount > 10) {
			threadCount = 10;
		}
		
		return threadCount;
	}
	
	private static int getDefaultThreadCount() {
		int threadCount = RuntimeUtils.getAvailableProcessors();
		if (threadCount == 1) {
			threadCount = DEFAULT_THREAD_SIZE;
		}
		
		return threadCount;
	}
	
	/**
	 * Creates a {@link ConnectionRequestData} object from the specified database URL and
	 * {@link Properties} object
	 * 
	 * @param url the database url
	 * @param props {@link Properties} object
	 * @return ConnectionRequestData object
	 */
	@SneakyThrows
	protected static ConnectionRequestData createRequest(String url, Properties props) {
		Map<DriverProperty, String> propValueMap = createDriverPropertyAndValueMap(url, props);
		String asyncStr = propValueMap.get(ASYNC);
		String listenerClassname = propValueMap.get(CONNECTION_LISTENER);
		//TODO read async and listener from the driver config if configured
		Boolean async = Boolean.valueOf(asyncStr);
		ConnectionListener listener = null;
		if (async) {
			if (Utils.isBlank(listenerClassname)) {
				throw new SQLException(PROP_DRIVER_CONN_LISTENER_CLASS + " is required for asynchronous connection requests");
			}
			
			Class<ConnectionListener> listenerClass = Utils.loadClass(listenerClassname);
			listener = listenerClass.newInstance();
		}
		
		String targetDbName = propValueMap.get(TARGET_DB);
		if (Utils.isBlank(targetDbName)) {
			throw new SQLException("No target database name defined in the database URL");
		}
		
		return new ConnectionRequestData(targetDbName, async, listener);
	}
	
	/**
	 * Generates a mappings between {@link DriverProperty} and value from the specified database URL and
	 * {@link Properties} object
	 *
	 * @param url the database url
	 * @param props {@link Properties} object
	 * @return Map
	 */
	protected static Map<DriverProperty, String> createDriverPropertyAndValueMap(String url, Properties props) {
		Map<DriverProperty, String> propValueMap = DriverProperty.toPropertyValueMap(props);
		String asyncStr = propValueMap.get(ASYNC);
		String listenerClassname = propValueMap.get(CONNECTION_LISTENER);
		
		final int qnMarkIndex = url.indexOf(DriverConstants.URL_SEPARATOR_DB_PARAM);
		String prefixAndName;
		if (qnMarkIndex > -1) {
			prefixAndName = url.substring(0, qnMarkIndex);
			String urlParams = url.substring(qnMarkIndex + 1);
			if (!Utils.isBlank(urlParams)) {
				String[] params = urlParams.split(DriverConstants.URL_SEPARATOR_PARAMS);
				for (String pair : params) {
					String[] keyAndValue = pair.split(DriverConstants.URL_SEPARATOR_PARAM_KEY_VALUE);
					String key = keyAndValue[0];
					DriverProperty driverProp = DriverProperty.findByName(key);
					String value = keyAndValue[1];
					if (driverProp == ASYNC && Utils.isBlank(asyncStr)) {
						asyncStr = value;
					} else if (driverProp == CONNECTION_LISTENER && Utils.isBlank(listenerClassname)) {
						listenerClassname = value;
					}
				}
			}
		} else {
			prefixAndName = url;
		}
		
		String targetDbName = prefixAndName.substring(prefixAndName.indexOf(URL_PREFIX) + URL_PREFIX.length());
		propValueMap.put(TARGET_DB, targetDbName);
		propValueMap.put(ASYNC, asyncStr);
		propValueMap.put(CONNECTION_LISTENER, listenerClassname);
		
		return propValueMap;
	}
	
}
