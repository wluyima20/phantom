/*
 * Add Copyright
 */
package com.amiyul.phantom.api.logging;

/**
 * Implementations are used to create driver loggers
 */
public interface LoggerProvider<T extends DriverLogger> {
	
	/**
	 * Gets the logger instance
	 *
	 * @return logger
	 */
	T getLogger();
	
}
