/*
 * Add Copyright
 */
package com.amiyul.phantom.api.logging;

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
	
	@Override
	public void warn(String message) {
		LOGGER.warning(message);
	}
	
	/**
	 * @see DriverLogger#error(String, Throwable)
	 */
	@Override
	public void error(String message, Throwable throwable) {
		LOGGER.severe(message);
		throwable.printStackTrace();
	}
	
}
