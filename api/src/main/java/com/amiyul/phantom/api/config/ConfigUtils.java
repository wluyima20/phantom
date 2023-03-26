/*
 * Add Copyright
 */
package com.amiyul.phantom.api.config;

import java.io.File;
import java.io.FileInputStream;
import java.util.Iterator;

import com.amiyul.phantom.api.Constants;
import com.amiyul.phantom.api.Database;
import com.amiyul.phantom.api.DatabaseProvider;
import com.amiyul.phantom.api.ServiceLoaderUtils;
import com.amiyul.phantom.api.SystemUtils;
import com.amiyul.phantom.api.Utils;
import com.amiyul.phantom.api.logging.DriverLogger;
import com.amiyul.phantom.api.logging.DriverLogger.LoggingApi;

/**
 * Contains config utilities
 */
public class ConfigUtils {
	
	private static DriverLogger LOGGER;
	
	protected static final String PROP_CONFIG_LOCATION = Constants.DATABASE_NAME + ".driver.config.location";
	
	private static String configFilePath;
	
	private static ConfigFileParser parser;
	
	private static ConfigMetadata configMetadata;
	
	private static Config config;
	
	/**
	 * Creates and returns a {@link ConfigFileParser} for the specified config file
	 *
	 * @param configFile the driver config file
	 * @return ConfigFileParser instance
	 */
	public synchronized static ConfigFileParser getParser(File configFile) {
		if (parser == null) {
			Iterator<ConfigFileParser> parsers = ServiceLoaderUtils.getProviders(ConfigFileParser.class);
			while (parsers.hasNext()) {
				ConfigFileParser candidate = parsers.next();
				if (candidate.canParse(configFile)) {
					parser = candidate;
				}
			}
			
			if (parser == null) {
				throw new RuntimeException("No appropriate parser found for specified driver config file");
			}
			
			LOGGER.debug("Found driver config file parser -> " + parser.getClass());
		}
		
		return parser;
	}
	
	/**
	 * Creates a {@link ConfigMetadata} instance
	 * 
	 * @param dbProviderClass the database provider class name
	 * @param loggingApi the name of the logging api
	 * @return ConfigMetadata object
	 * @throws Exception
	 */
	public static ConfigMetadata createMetadata(String dbProviderClass, String loggingApi) throws Exception {
		Class<DatabaseProvider> providerClass = (Class) Thread.currentThread().getContextClassLoader()
		        .loadClass(dbProviderClass);
		
		return new ConfigMetadata() {
			
			@Override
			public Class<DatabaseProvider> getDatabaseProviderClass() {
				return providerClass;
			}
			
			@Override
			public String getLoggingApi() {
				return loggingApi;
			}
			
		};
	}
	
	/**
	 * Gets the {@link Config} instance
	 *
	 * @return Config
	 * @throws Exception
	 */
	public synchronized static Config getConfig() throws Exception {
		if (config == null) {
			DatabaseProvider provider;
			try {
				provider = getConfigMetadata().getDatabaseProviderClass().newInstance();
			}
			catch (ReflectiveOperationException e) {
				throw new RuntimeException(e);
			}
			
			final Database database = provider.get();
			final LoggingApi loggingApi = LoggingApi.valueOf(getConfigMetadata().getLoggingApi().toUpperCase());
			
			return new Config() {
				
				@Override
				public Database getDatabase() {
					return database;
				}
				
				@Override
				public LoggingApi getLoggingApi() {
					return loggingApi;
				}
				
			};
		}
		
		return config;
	}
	
	/**
	 * Gets the {@link ConfigMetadata} instance
	 *
	 * @return ConfigMetadata
	 * @throws Exception
	 */
	private synchronized static ConfigMetadata getConfigMetadata() throws Exception {
		if (configMetadata == null) {
			LOGGER.info("Loading " + Constants.DATABASE_NAME + " driver configuration");
			
			if (configFilePath == null) {
				configFilePath = System.getProperty(PROP_CONFIG_LOCATION);
				
				if (Utils.isBlank(configFilePath)) {
					configFilePath = SystemUtils.getEnv(PROP_CONFIG_LOCATION);
				}
				
				if (Utils.isBlank(configFilePath)) {
					throw new RuntimeException("Found no defined location for the driver config file");
				}
				
				LOGGER.debug("Using driver config file located at -> " + configFilePath);
			}
			
			File configFile = new File(configFilePath);
			
			configMetadata = ConfigUtils.getParser(configFile).parse(new FileInputStream(configFile));
		}
		
		return configMetadata;
	}
	
}
