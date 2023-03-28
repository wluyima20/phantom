/*
 * Add Copyright
 */
package com.amiyul.phantom.api.logging;

/**
 * Interface to be implemented by loggers used by the driver, if no logging API is configured, by
 * the driver defaults to slf4j if it is detected on the classpath otherwise Java logging.
 */
public interface DriverLogger {
	
	/**
	 * Logs a debug message
	 *
	 * @param message the message to log
	 */
	void debug(String message);
	
	/**
	 * Logs an info message
	 *
	 * @param message the message to log
	 */
	void info(String message);
	
	/**
	 * Logs a warning message
	 *
	 * @param message the message to log
	 */
	void warn(String message);
	
	/**
	 * Logs an error message
	 *
	 * @param message the message to log
	 * @param throwable the thrown {@link Throwable}
	 */
	void error(String message, Throwable throwable);
	
}
