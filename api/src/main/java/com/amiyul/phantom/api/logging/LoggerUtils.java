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
			
			debug(LoggerUtils.class, "Found logging api provider -> " + provider.getClass());
		}
		
		return provider;
	}
	
	public static void debug(Class<?> clazz, String message) {
		getProvider().getLogger(clazz).debug(message);
	}
	
	public static void info(Class<?> clazz, String message) {
		getProvider().getLogger(clazz).info(message);
	}
	
	public static void warn(Class<?> clazz, String message) {
		getProvider().getLogger(clazz).warn(message);
	}
	
	public static void error(Class<?> clazz, String message, Throwable throwable) {
		getProvider().getLogger(clazz).error(message, throwable);
	}
	
}
