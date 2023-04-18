/*
 * Add Copyright
 */
package com.amiyul.phantom.driver;

import static com.amiyul.phantom.driver.DriverConstants.PROP_ASYNC;
import static com.amiyul.phantom.driver.DriverConstants.PROP_ASYNC_LISTENER;
import static com.amiyul.phantom.driver.DriverConstants.URL_PREFIX;

import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;
import java.util.concurrent.CompletableFuture;

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
		
		if (!requestData.isAsync()) {
			return DefaultClient.getInstance().connect(requestData);
		}
		
		CompletableFuture.runAsync(new ConnectTask(requestData));
		//TODO Keep references to all futures for clean up during shutdown
		//TODO include the future on the proxy
		return (Connection) Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(),
		    new Class[] { Connection.class }, new FailingConnectionInvocationHandler());
	}
	
	@SneakyThrows
	private static ConnectionRequestData createRequest(String url, Properties props) throws SQLException {
		final int qnMarkIndex = url.indexOf(DriverConstants.URL_DB_PARAM_SEPARATOR);
		String prefixAndName;
		String asyncStr = null;
		if (props.contains(PROP_ASYNC)) {
			asyncStr = props.getProperty(PROP_ASYNC);
		}
		
		ConnectionListener listener = null;
		if (qnMarkIndex > -1) {
			prefixAndName = url.substring(0, qnMarkIndex);
			String urlParams = url.substring(qnMarkIndex + 1);
			if (!Utils.isBlank(urlParams)) {
				String[] params = urlParams.split(DriverConstants.URL_PARAMS_SEPARATOR);
				for (String pair : params) {
					String[] keyAndValue = pair.split(DriverConstants.URL_PARAM_KEY_VALUE_SEPARATOR);
					String key = keyAndValue[0];
					if (!DriverConstants.PROP_NAMES.contains(key)) {
						throw new SQLException("Connection URL contains unsupported parameter named: " + key);
					}
					
					String value = keyAndValue[1];
					if (asyncStr == null && PROP_ASYNC.equals(key)) {
						asyncStr = value;
					} else if (PROP_ASYNC_LISTENER.equals(key)) {
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
			throw new SQLException(PROP_ASYNC_LISTENER + " is required for asynchronous get connection calls");
		}
		
		String targetDbName = prefixAndName.substring(prefixAndName.indexOf(URL_PREFIX) + URL_PREFIX.length());
		
		if (Utils.isBlank(targetDbName)) {
			throw new SQLException("No target database name defined in the database URL");
		}
		
		return new ConnectionRequestData(targetDbName, async, listener);
	}
	
}
