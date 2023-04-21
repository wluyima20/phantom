/*
 * Add Copyright
 */
package com.amiyul.phantom.driver;

import static com.amiyul.phantom.driver.DriverConstants.DEFAULT_THREAD_SIZE;
import static com.amiyul.phantom.driver.DriverConstants.URL_PARAM_ASYNC;
import static com.amiyul.phantom.driver.DriverConstants.URL_PARAM_ASYNC_LISTENER;
import static com.amiyul.phantom.driver.DriverConstants.URL_PREFIX;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

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
		int threadCount = Runtime.getRuntime().availableProcessors();
		if (threadCount == 1) {
			threadCount = DEFAULT_THREAD_SIZE;
		}
		
		return threadCount;
	}
	
	@SneakyThrows
	private static ConnectionRequestData createRequest(String url, Properties props) throws SQLException {
		final int qnMarkIndex = url.indexOf(DriverConstants.URL_SEPARATOR_DB_PARAM);
		String prefixAndName;
		String asyncStr = null;
		if (props.contains(URL_PARAM_ASYNC)) {
			asyncStr = props.getProperty(URL_PARAM_ASYNC);
		}
		
		ConnectionListener listener = null;
		if (qnMarkIndex > -1) {
			prefixAndName = url.substring(0, qnMarkIndex);
			String urlParams = url.substring(qnMarkIndex + 1);
			if (!Utils.isBlank(urlParams)) {
				String[] params = urlParams.split(DriverConstants.URL_SEPARATOR_PARAMS);
				for (String pair : params) {
					String[] keyAndValue = pair.split(DriverConstants.URL_SEPARATOR_PARAM_KEY_VALUE);
					String key = keyAndValue[0];
					if (!DriverConstants.PROP_NAMES.contains(key)) {
						throw new SQLException("Connection URL contains unsupported parameter named: " + key);
					}
					
					String value = keyAndValue[1];
					if (asyncStr == null && URL_PARAM_ASYNC.equals(key)) {
						asyncStr = value;
					} else if (URL_PARAM_ASYNC_LISTENER.equals(key)) {
						Class<ConnectionListener> clazz = Utils.loadClass(value);
						listener = clazz.newInstance();
					}
				}
			}
		} else {
			prefixAndName = url;
		}
		
		//TODO read async and listener from the driver config if configured
		
		boolean async = Boolean.valueOf(asyncStr);
		if (async && listener == null) {
			throw new SQLException(URL_PARAM_ASYNC_LISTENER + " is required for asynchronous get connection calls");
		}
		
		String targetDbName = prefixAndName.substring(prefixAndName.indexOf(URL_PREFIX) + URL_PREFIX.length());
		
		if (Utils.isBlank(targetDbName)) {
			throw new SQLException("No target database name defined in the database URL");
		}
		
		return new ConnectionRequestData(targetDbName, async, listener);
	}
	
}
