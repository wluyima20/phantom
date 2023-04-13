/*
 * Add Copyright
 */
package com.amiyul.phantom.driver;

import java.io.File;
import java.sql.SQLException;

import com.amiyul.phantom.api.Constants;
import com.amiyul.phantom.api.Database;
import com.amiyul.phantom.api.DatabaseProvider;
import com.amiyul.phantom.api.Utils;
import com.amiyul.phantom.api.logging.LoggerUtils;
import com.amiyul.phantom.db.FileDatabaseProvider;

/**
 * Contains driver config utilities
 */
public class DriverConfigUtils {
	
	public static final String PROP_CONFIG_LOCATION = Constants.DATABASE_NAME + ".driver.config.location";
	
	private static String configFilePath;
	
	private static DriverConfigFileParser parser;
	
	private static DriverConfigMetadata configMetadata;
	
	private static DriverConfig config;
	
	/**
	 * Gets {@link DriverConfigFileParser} via the service loader mechanism that can parse the specified
	 * driver config file.
	 *
	 * @param configFile the driver config file
	 * @return DriverConfigFileParser instance
	 */
	protected synchronized static DriverConfigFileParser getParser(File configFile) {
		if (parser == null) {
			parser = Utils.getParser(DriverConfigFileParser.class, configFile);
			if (parser == null) {
				throw new RuntimeException("No appropriate parser found for specified driver config file");
			}
			
			LoggerUtils.debug("Found driver config file parser -> " + parser.getClass());
		}
		
		return parser;
	}
	
	/**
	 * Creates a {@link DriverConfigMetadata} instance
	 *
	 * @param dbProviderClass the database provider class name
	 * @return ConfigMetadata object
	 * @throws Exception
	 */
	protected static DriverConfigMetadata createMetadata(String dbProviderClass) throws Exception {
		Class<DatabaseProvider> clazz = null;
		if (!Utils.isBlank(dbProviderClass)) {
			clazz = Utils.loadClass(dbProviderClass);
		}
		
		final Class<DatabaseProvider> providerClazz = clazz;
		
		return () -> providerClazz;
	}
	
	/**
	 * Gets the {@link DriverConfig} instance
	 *
	 * @return DriverConfig
	 */
	protected synchronized static DriverConfig getConfig() {
		if (config == null) {
			DatabaseProvider<Database> provider;
			try {
				DriverConfigMetadata metadata = getConfigMetadata();
				Class<? extends DatabaseProvider> clazz = null;
				if (metadata != null) {
					clazz = metadata.getDatabaseProviderClass();
				}
				
				if (clazz == null) {
					clazz = FileDatabaseProvider.class;
					LoggerUtils.debug("No configured database provider, defaulting to file database provider");
				}
				
				provider = clazz.newInstance();
			}
			catch (Exception e) {
				throw new RuntimeException(e);
			}
			
			config = () -> provider.get();
		}
		
		return config;
	}
	
	/**
	 * Reloads the driver and database configurations
	 * 
	 * @throws SQLException
	 */
	protected static void reloadConfig() throws SQLException {
		LoggerUtils.debug("Discarding driver config");
		
		configMetadata = null;
		config = null;
		DefaultClient.getInstance().reload();
	}
	
	/**
	 * Gets the path to the driver config file
	 *
	 * @return config file path
	 */
	protected synchronized static String getConfigFile() {
		if (configFilePath == null) {
			configFilePath = Utils.getFilePath(PROP_CONFIG_LOCATION);
			
			if (!Utils.isBlank(configFilePath)) {
				LoggerUtils.debug("Using driver config file located at -> " + configFilePath);
			} else {
				LoggerUtils.debug("Found no defined location for the driver config file");
			}
		}
		
		return configFilePath;
	}
	
	/**
	 * Gets the {@link DriverConfigMetadata} instance
	 *
	 * @return DriverConfigMetadata
	 * @throws Exception
	 */
	protected synchronized static DriverConfigMetadata getConfigMetadata() throws Exception {
		if (configMetadata == null) {
			LoggerUtils.debug("Loading " + Constants.DATABASE_NAME + " driver configuration");
			
			String filePath = getConfigFile();
			if (!Utils.isBlank(filePath)) {
				File configFile = new File(filePath);
				configMetadata = getParser(configFile).parse(Utils.getInputStream(configFile));
			}
		}
		
		return configMetadata;
	}
	
}
