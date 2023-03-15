package com.amiyul.phantom.driver;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Logger used by the driver
 */
public final class PhantomLogger {
	
	protected static final String PROP_ENABLE_LOGGING = PhantomLogger.class.getName() + ".enabled";
	
	private static final String LOGGER_NAME = PhantomLogger.class.getPackage().getName() + ".logger";
	
	protected static final Logger LOGGER;
	
	static {
		LOGGER = Logger.getLogger(LOGGER_NAME);
		if ("true".equalsIgnoreCase(System.getProperty(PROP_ENABLE_LOGGING))) {
			LOGGER.setLevel(Level.INFO);
		} else {
			LOGGER.setLevel(Level.OFF);
		}
	}
	
}
