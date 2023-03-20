/*
 * Add Copyright
 */
package com.amiyul.driver.phantom.logging;

import java.util.logging.Logger;

/**
 * {@link DriverLogger} implementation that uses java logging api
 */
public final class JavaLogger implements DriverLogger {
	
	private static final String LOGGER_NAME = JavaLogger.class.getPackage().getName() + ".logger";
	
	private static final Logger LOGGER = Logger.getLogger(LOGGER_NAME);
	
	/**
	 * @see DriverLogger#debug(String)
	 */
	@Override
	public void debug(String message) {
		LOGGER.config(message);
	}
	
	/**
	 * @see DriverLogger#info(String)
	 */
	@Override
	public void info(String message) {
		LOGGER.info(message);
	}
	
	/**
	 * @see DriverLogger#error(String)
	 */
	@Override
	public void error(String message) {
		LOGGER.severe(message);
	}
	
}
