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
import com.amiyul.phantom.api.SystemUtils;
import com.amiyul.phantom.api.Utils;
import com.amiyul.phantom.api.logging.LoggerUtils;

/**
 * Contains config utilities
 */
public class ConfigUtils {
	
	protected static final String PROP_CONFIG_LOCATION = Constants.DATABASE_NAME + ".driver.config.location";
	
	private static String configFile;
	
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
	 * Gets the {@link Config} instance
	 *
	 * @return Config
	 */
	public synchronized static Config getConfig() {
		if (config == null) {
			DatabaseProvider provider;
			try {
				provider = getConfigMetadata(getConfigFile()).getDatabaseProviderClass().newInstance();
			}
			catch (Exception e) {
				throw new RuntimeException(e);
			}
			
			config = () -> provider.get();
		}
		
		return config;
	}
	
	/**
	 * Gets the path to the config file
	 *
	 * @return config file path
	 */
	protected synchronized static String getConfigFile() {
		if (configFile == null) {
			configFile = System.getProperty(PROP_CONFIG_LOCATION);
			
			if (Utils.isBlank(configFile)) {
				configFile = SystemUtils.getEnv(PROP_CONFIG_LOCATION);
			}
			
			if (Utils.isBlank(configFile)) {
				throw new RuntimeException("Found no defined location for the driver config file");
			}
			
			LoggerUtils.debug("Using driver config file located at -> " + configFile);
		}
		
		return configFile;
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
			
			configMetadata = ConfigUtils.getParser(configFile).parse(new FileInputStream(configFile));
		}
		
		return configMetadata;
	}
	
}
