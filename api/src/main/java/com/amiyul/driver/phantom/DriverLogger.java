/*
 * Add Copyright
 */
package com.amiyul.driver.phantom;

/**
 * Interface to be implemented by loggers used by the driver, if no logging API is configured, by
 * the driver defaults to slf4j if it is detected on the classpath otherwise Java logging.
 */
public interface DriverLogger {
	
	enum LoggingApi {
		
		SLF4J("slf4j"), JAVA("java");
		
		private String value;
		
		LoggingApi(String value) {
			this.value = value;
		}
		
	}
	
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
	 * Logs an error message
	 *
	 * @param message the message to log
	 */
	void error(String message);
	
}
