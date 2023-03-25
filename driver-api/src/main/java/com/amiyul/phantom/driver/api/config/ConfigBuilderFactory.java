/*
 * Add Copyright
 */
package com.amiyul.phantom.driver.api.config;

import com.amiyul.phantom.api.Database;
import com.amiyul.phantom.api.config.Config;
import com.amiyul.phantom.api.logging.DriverLogger.LoggingApi;

/**
 * Factory class for {@link ConfigBuilder} instances
 */
public class ConfigBuilderFactory {
	
	private ConfigBuilderFactory() {
	}
	
	private static class ConfigBuilderFactoryHolder {
		
		private static final ConfigBuilderFactory INSTANCE = new ConfigBuilderFactory();
		
	}
	
	/**
	 * Gets the {@link ConfigBuilderFactory} instance
	 * 
	 * @return
	 */
	protected static ConfigBuilderFactory getInstance() {
		return ConfigBuilderFactoryHolder.INSTANCE;
	}
	
	protected ConfigBuilder createBuilder(Database database) {
		return new ConfigBuilder() {
			
			private LoggingApi loggingApi;
			
			@Override
			public ConfigBuilder withLoggingApi(LoggingApi loggingApi) {
				this.loggingApi = loggingApi;
				return this;
			}
			
			@Override
			public Config build() {
				Config config = new Config() {
					
					@Override
					public Database getDatabase() {
						return database;
					}
					
					@Override
					public LoggingApi getLoggingApi() {
						return loggingApi;
					}
					
				};
				
				return config;
			}
		};
	}
	
}
