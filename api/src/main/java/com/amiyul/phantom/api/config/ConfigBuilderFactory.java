/*
 * Add Copyright
 */
package com.amiyul.phantom.api.config;

import com.amiyul.phantom.api.DatabaseMetadata;
import com.amiyul.phantom.api.DatabaseMetadataProvider;
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
	
	/**
	 * Creates a {@link ConfigBuilder} instance with the specified {@link DatabaseMetadataProvider}
	 *
	 * @param provider the {@link DatabaseMetadataProvider} instance
	 * @return ConfigBuilder instance
	 */
	protected ConfigBuilder createBuilder(DatabaseMetadataProvider provider) {
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
					public DatabaseMetadata getDatabaseMetadata(String databaseId) {
						return provider.getDatabaseMetadata().get(databaseId);
					}
					
					@Override
					public LoggingApi getLoggingApi() {
						return loggingApi;
					}
					
				};
				
				provider.load();
				
				return config;
			}
		};
	}
	
	private static class ConfigBuilderFactoryHolder {
		
		private static final ConfigBuilderFactory INSTANCE = new ConfigBuilderFactory();
		
	}
	
}
