/*
 * Add Copyright
 */
package com.amiyul.phantom.api.logging;

import java.util.Iterator;

import com.amiyul.phantom.api.ServiceLoaderUtils;

/**
 * Contains logging utilities
 */
public final class LoggerUtils {
	
	private static LoggerProvider provider;
	
	/**
	 * Gets the {@link LoggerProvider}
	 *
	 * @return LoggerProvider instance
	 */
	protected synchronized static LoggerProvider getProvider() {
		if (provider == null) {
			Iterator<LoggerProvider> providers = ServiceLoaderUtils.getProviders(LoggerProvider.class);
			while (providers.hasNext()) {
				provider = providers.next();
				break;
			}
			
			if (provider == null) {
				provider = new JavaLoggerProvider();
				debug("No logging api provider found, using java logging provider as the default");
			} else {
				debug("Found logging api provider -> " + provider.getClass());
			}
		}
		
		return provider;
	}
	
	/**
	 * @see DriverLogger#debug(String)
	 */
	public static void debug(String message) {
		getProvider().getLogger().debug(message);
	}
	
	/**
	 * @see DriverLogger#info(String)
	 */
	public static void info(String message) {
		getProvider().getLogger().info(message);
	}
	
	/**
	 * @see DriverLogger#warn(String)
	 */
	public static void warn(String message) {
		getProvider().getLogger().warn(message);
	}
	
	/**
	 * @see DriverLogger#error(String, Throwable)
	 */
	public static void error(String message, Throwable throwable) {
		getProvider().getLogger().error(message, throwable);
	}
	
}
