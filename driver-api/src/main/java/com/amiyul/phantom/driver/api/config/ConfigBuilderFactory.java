/*
 * Add Copyright
 */
package com.amiyul.phantom.driver.api.config;

import com.amiyul.phantom.api.Database;
import com.amiyul.phantom.api.DatabaseProvider;
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
	 * @return ConfigBuilderFactory object
	 */
	protected static ConfigBuilderFactory getInstance() {
		return ConfigBuilderFactoryHolder.INSTANCE;
	}
	
	/**
	 * Creates a {@link ConfigBuilder} with the specified {@link DatabaseProvider} instance
	 * 
	 * @param databaseProvider the {@link DatabaseProvider} object
	 * @return ConfigBuilder
	 */
	protected ConfigBuilder createBuilder(DatabaseProvider databaseProvider) {
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
						return databaseProvider.get();
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
