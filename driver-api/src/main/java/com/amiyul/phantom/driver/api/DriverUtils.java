/*
 * Add Copyright
 */
package com.amiyul.phantom.driver.api;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import com.amiyul.phantom.api.Constants;
import com.amiyul.phantom.api.Database;
import com.amiyul.phantom.api.DatabaseProvider;
import com.amiyul.phantom.api.SystemUtils;
import com.amiyul.phantom.api.Utils;
import com.amiyul.phantom.api.config.Config;
import com.amiyul.phantom.api.config.ConfigFileParser;
import com.amiyul.phantom.api.config.ConfigFileParserFactory;
import com.amiyul.phantom.api.config.ConfigMetadata;
import com.amiyul.phantom.api.logging.DriverLogger;
import com.amiyul.phantom.api.logging.DriverLogger.LoggingApi;

public class DriverUtils {
	
	protected static DriverLogger LOGGER;
	
	protected static final String PROP_CONFIG_LOCATION = Utils.class.getPackage().getName() + ".config.location";
	
	private static String configFilePath;
	
	private static ConfigFileParser parser;
	
	private static ConfigMetadata configMetadata;
	
	private static Config config;
	
	private static Client client;
	
	/**
	 * Gets the {@link ConfigMetadata} instance
	 * 
	 * @return ConfigMetadata
	 * @throws FileNotFoundException
	 */
	private synchronized static ConfigMetadata getConfigMetadata() throws FileNotFoundException {
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
			
			if (parser == null) {
				parser = ConfigFileParserFactory.createParser(configFile);
			}
			
			if (parser == null) {
				throw new RuntimeException("No appropriate parser found for specified driver config file");
			}
			
			LOGGER.debug("Parsing driver config file with parser of type -> " + parser.getClass());
			
			configMetadata = parser.parse(new FileInputStream(configFile));
		}
		
		return configMetadata;
	}
	
	/**
	 * Gets the {@link Config} instance
	 *
	 * @return Config
	 * @throws FileNotFoundException
	 */
	protected synchronized static Config getConfig() throws FileNotFoundException {
		if (config == null) {
			DatabaseProvider provider;
			try {
				provider = getConfigMetadata().getDatabaseProviderClass().newInstance();
			}
			catch (ReflectiveOperationException e) {
				throw new RuntimeException(e);
			}
			
			final Database database = provider.get();
			final LoggingApi loggingApi = LoggingApi.valueOf(getConfigMetadata().getLoggingApi());
			
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
	
	protected static Client getClient() {
		//TODO Create client
		return client;
	}
	
}
