/*
 * Add Copyright
 */
package com.amiyul.phantom.api.logging;

import com.amiyul.phantom.api.logging.DriverLogger.LoggingApi;

/**
 * Implementations are used to create driver loggers
 */
public interface LoggerProvider<T extends DriverLogger> {
	
	/**
	 * Gets the logging api that this provider supports
	 * 
	 * @return logging api
	 */
	LoggingApi getSupportedLoggingApi();
	
	/**
	 * Gets a logger named according to the specified name
	 *
	 * @return logger
	 */
	T getLogger(String name);
	
	/**
	 * Gets a logger named according to the specified class
	 *
	 * @return logger
	 */
	default T getLogger(Class<?> clazz) {
		return getLogger(clazz.getName());
	}
	
}
