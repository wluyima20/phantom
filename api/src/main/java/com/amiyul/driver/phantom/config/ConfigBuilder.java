/*
 * Add Copyright
 */
package com.amiyul.driver.phantom.config;

import com.amiyul.driver.phantom.logging.DriverLogger.LoggingApi;

/**
 * Super interface for builders for {@link Config} instance
 */
public interface ConfigBuilder {
	
	/**
	 * Specifies the {@link LoggingApi} to set on the {@link Config} instance
	 * 
	 * @param loggingApi the logging api
	 * @return this builder instance
	 */
	ConfigBuilder withLoggingApi(LoggingApi loggingApi);
	
	/**
	 * Creates a {@link Config} instance
	 * 
	 * @return {@link Config} instance
	 */
	Config build();
	
}
