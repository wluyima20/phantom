/*
 * Add Copyright
 */
package com.amiyul.phantom.api.logging;

import java.util.logging.Logger;

/**
 * {@link DriverLogger} implementation that delegates to a java logging api logger
 */
public final class JavaLogger extends BaseDelegatingDriverLogger<Logger> {
	
	public JavaLogger(Logger nativeLogger) {
		super(nativeLogger);
	}
	
	/**
	 * @see DriverLogger#debug(String)
	 */
	@Override
	public void debug(String message) {
		getNativeLogger().config(message);
	}
	
	/**
	 * @see DriverLogger#info(String)
	 */
	@Override
	public void info(String message) {
		getNativeLogger().info(message);
	}
	
	@Override
	public void warn(String message) {
		getNativeLogger().warning(message);
	}
	
	/**
	 * @see DriverLogger#error(String, Throwable)
	 */
	@Override
	public void error(String message, Throwable throwable) {
		getNativeLogger().severe(message);
		throwable.printStackTrace();
	}
	
}
