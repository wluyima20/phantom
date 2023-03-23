/*
 * Add Copyright
 */
package com.amiyul.phantom.api.config;

import com.amiyul.phantom.api.Server;
import com.amiyul.phantom.api.logging.DriverLogger.LoggingApi;

/**
 * Factory class for {@link ConfigBuilder} instances
 */
public class ConfigBuilderFactory {
	
	private ConfigBuilderFactory() {
	}
	
	/**
	 * Gets the {@link ConfigBuilderFactory} instance
	 * 
	 * @return
	 */
	protected static ConfigBuilderFactory getInstance() {
		return ConfigBuilderFactoryHolder.INSTANCE;
	}
	
	protected ConfigBuilder createBuilder(Server server) {
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
					public Server getServer() {
						return server;
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
	
	private static class ConfigBuilderFactoryHolder {
		
		private static final ConfigBuilderFactory INSTANCE = new ConfigBuilderFactory();
		
	}
	
}
