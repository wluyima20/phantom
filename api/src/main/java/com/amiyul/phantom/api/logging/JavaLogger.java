/*
 * Add Copyright
 */
package com.amiyul.phantom.api.logging;

import java.util.logging.Logger;

/**
 * {@link DriverLogger} implementation that delegates to the java logging api logger
 */
public final class JavaLogger implements DriverLogger {
	
	private final Logger nativeLogger;
	
	private JavaLogger() {
		nativeLogger = Logger.getLogger(getClass().getName());
	}
	
	public static JavaLogger getInstance() {
		return JavaLoggerHolder.INSTANCE;
	}
	
	@Override
	public void debug(String message) {
		nativeLogger.config(message);
	}
	
	@Override
	public void info(String message) {
		nativeLogger.info(message);
	}
	
	@Override
	public void warn(String message) {
		nativeLogger.warning(message);
	}
	
	@Override
	public void error(String message, Throwable throwable) {
		nativeLogger.severe(message);
		throwable.printStackTrace();
	}
	
	private static class JavaLoggerHolder {
		
		private static final JavaLogger INSTANCE = new JavaLogger();
		
	}
	
}
