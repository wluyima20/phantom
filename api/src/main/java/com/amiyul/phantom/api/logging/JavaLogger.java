/*
 * Add Copyright
 */
package com.amiyul.phantom.api.logging;

import java.util.logging.Logger;

/**
 * {@link DriverLogger} implementation that delegates to the java logging api logger
 */
public final class JavaLogger extends BaseDelegatingLogger<Logger> {
	
	private JavaLogger() {
		super(Logger.getLogger(JavaLogger.class.getName()));
	}
	
	public static JavaLogger getInstance() {
		return JavaLoggerHolder.INSTANCE;
	}
	
	@Override
	public void debug(String message) {
		getNativeLogger().config(message);
	}
	
	@Override
	public void info(String message) {
		getNativeLogger().info(message);
	}
	
	@Override
	public void warn(String message) {
		getNativeLogger().warning(message);
	}
	
	@Override
	public void error(String message, Throwable throwable) {
		getNativeLogger().severe(message);
		throwable.printStackTrace();
	}
	
	private static class JavaLoggerHolder {
		
		private static final JavaLogger INSTANCE = new JavaLogger();
		
	}
	
}
