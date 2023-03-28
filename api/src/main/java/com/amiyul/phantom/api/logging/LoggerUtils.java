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
				
				debug("Found logging api provider -> " + provider.getClass());
				
				break;
			}
			
			if (provider == null && isSlf4jPresent()) {
				provider = new Slf4jLoggerProvider();
				
				debug("No registered logging api provider found, defaulting to slf4j provider");
			}
			
			if (provider == null) {
				provider = new JavaLoggerProvider();
				debug("No logging api provider found, using java logging provider as the default");
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
	
	/**
	 * Checks if slf4j library is on the classpath
	 * 
	 * @return true if slf4j is present otherwise false
	 */
	private static boolean isSlf4jPresent() {
		try {
			Thread.currentThread().getContextClassLoader().loadClass(Slf4jLogger.CLASS_LOGGER_FACTORY);
			return true;
		}
		catch (ClassNotFoundException e) {
			//Ignore
		}
		
		return false;
	}
	
}
