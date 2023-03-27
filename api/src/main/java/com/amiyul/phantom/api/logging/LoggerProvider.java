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
	 * Gets the logger instance
	 *
	 * @return logger
	 */
	T getLogger();
	
}
