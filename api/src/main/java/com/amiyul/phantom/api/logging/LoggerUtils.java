/*
 * Add Copyright
 */
package com.amiyul.phantom.api.logging;

import java.util.Iterator;

import com.amiyul.phantom.api.ServiceLoaderUtils;
import com.amiyul.phantom.api.config.ConfigUtils;
import com.amiyul.phantom.api.logging.DriverLogger.LoggingApi;

import lombok.SneakyThrows;

/**
 * Contains logging utilities
 */
public final class LoggerUtils {
	
	private static LoggerProvider provider;
	
	/**
	 * Gets the {@link LoggerProvider}
	 *
	 * @return LoggerProvider instance
	 * @throws Exception
	 */
	@SneakyThrows(Exception.class)
	protected synchronized static LoggerProvider getProvider() {
		if (provider == null) {
			Iterator<LoggerProvider> providers = ServiceLoaderUtils.getProviders(LoggerProvider.class);
			LoggingApi api = ConfigUtils.getConfig().getLoggingApi();
			while (providers.hasNext()) {
				LoggerProvider candidate = providers.next();
				if (api == candidate.getSupportedLoggingApi()) {
					provider = candidate;
				}
			}
			
			if (provider == null) {
				throw new RuntimeException("No appropriate provider found for the configured logging api");
			}
			
			debug("Found logging api provider -> " + provider.getClass());
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
