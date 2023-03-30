/*
 * Add Copyright
 */
package com.amiyul.phantom.api.config;

import java.io.File;
import java.io.FileInputStream;
import java.util.Iterator;

import com.amiyul.phantom.api.Constants;
import com.amiyul.phantom.api.DatabaseProvider;
import com.amiyul.phantom.api.ServiceLoaderUtils;
import com.amiyul.phantom.api.Utils;
import com.amiyul.phantom.api.logging.LoggerUtils;

/**
 * Contains driver config utilities
 */
public class DriverConfigUtils {
	
	protected static final String PROP_CONFIG_LOCATION = Constants.DATABASE_NAME + ".driver.config.location";
	
	private static String configFilePath;
	
	private static DriverConfigFileParser parser;
	
	private static ConfigMetadata configMetadata;
	
	private static DriverConfig config;
	
	/**
	 * Creates and returns a {@link DriverConfigFileParser} for the specified driver config file
	 *
	 * @param configFile the driver config file
	 * @return DriverConfigFileParser instance
	 */
	protected synchronized static DriverConfigFileParser getParser(File configFile) {
		if (parser == null) {
			Iterator<DriverConfigFileParser> parsers = ServiceLoaderUtils.getProviders(DriverConfigFileParser.class);
			while (parsers.hasNext()) {
				DriverConfigFileParser candidate = parsers.next();
				if (candidate.canParse(configFile)) {
					parser = candidate;
				}
			}
			
			if (parser == null) {
				throw new RuntimeException("No appropriate parser found for specified driver config file");
			}
			
			LoggerUtils.debug("Found driver config file parser -> " + parser.getClass());
		}
		
		return parser;
	}
	
	/**
	 * Creates a {@link ConfigMetadata} instance
	 *
	 * @param dbProviderClass the database provider class name
	 * @return ConfigMetadata object
	 * @throws Exception
	 */
	public static ConfigMetadata createMetadata(String dbProviderClass) throws Exception {
		Class<DatabaseProvider> providerClass = (Class) Thread.currentThread().getContextClassLoader()
		        .loadClass(dbProviderClass);
		
		return () -> providerClass;
	}
	
	/**
	 * Gets the {@link DriverConfig} instance
	 *
	 * @return Config
	 */
	public synchronized static DriverConfig getConfig() {
		if (config == null) {
			DatabaseProvider provider;
			try {
				provider = getConfigMetadata(getDriverConfigFile()).getDatabaseProviderClass().newInstance();
			}
			catch (Exception e) {
				throw new RuntimeException(e);
			}
			
			config = () -> provider.get();
		}
		
		return config;
	}
	
	/**
	 * Discards the cached driver config
	 */
	public synchronized static void discardConfig() {
		configMetadata = null;
		config = null;
	}
	
	/**
	 * Gets the path to the driver config file
	 *
	 * @return config file path
	 */
	protected synchronized static String getDriverConfigFile() {
		if (configFilePath == null) {
			configFilePath = Utils.getConfigFile(PROP_CONFIG_LOCATION);
			
			if (Utils.isBlank(configFilePath)) {
				throw new RuntimeException("Found no defined location for the driver config file");
			}
			
			LoggerUtils.debug("Using driver config file located at -> " + configFilePath);
		}
		
		return configFilePath;
	}
	
	/**
	 * Gets the {@link ConfigMetadata} instance
	 *
	 * @param configFilePath path to config file
	 * @return ConfigMetadata
	 * @throws Exception
	 */
	protected synchronized static ConfigMetadata getConfigMetadata(String configFilePath) throws Exception {
		if (configMetadata == null) {
			LoggerUtils.info("Loading " + Constants.DATABASE_NAME + " driver configuration");
			
			File configFile = new File(configFilePath);
			
			configMetadata = DriverConfigUtils.getParser(configFile).parse(new FileInputStream(configFile));
		}
		
		return configMetadata;
	}
	
}
