/*
 * Add Copyright
 */
package com.amiyul.phantom.api.logging;

import java.util.Iterator;

import com.amiyul.phantom.api.ServiceLoaderUtils;
import com.amiyul.phantom.api.config.ConfigUtils;

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
	protected synchronized static LoggerProvider getProvider() throws Exception {
		if (provider == null) {
			Iterator<LoggerProvider> providers = ServiceLoaderUtils.getProviders(LoggerProvider.class);
			while (providers.hasNext()) {
				LoggerProvider candidate = providers.next();
				if (ConfigUtils.getConfig().getLoggingApi() == candidate.getSupportedLoggingApi()) {
					provider = candidate;
				}
			}
			
			if (provider == null) {
				throw new RuntimeException("No appropriate provider found for the configured logging api");
			}
			
			new JavaLogger().debug("Found logging api provider -> " + provider.getClass());
		}
		
		return provider;
	}
	
}
