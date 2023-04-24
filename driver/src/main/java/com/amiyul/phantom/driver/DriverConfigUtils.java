/*
 * Add Copyright
 */
package com.amiyul.phantom.driver;

import java.io.File;
import java.sql.SQLException;
import java.time.LocalDateTime;

import com.amiyul.phantom.api.Constants;
import com.amiyul.phantom.api.Database;
import com.amiyul.phantom.api.DatabaseProvider;
import com.amiyul.phantom.api.Status;
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
	 * @param dbProviderClassName the database provider class name
	 * @param unavailableUntil time when the database will be available
	 * @return ConfigMetadata object
	 * @throws Exception
	 */
	protected static DriverConfigMetadata createMetadata(String dbProviderClassName, String unavailableUntil) {
		return new DriverConfigMetadata() {
			
			@Override
			public String getDatabaseProviderClassName() {
				return dbProviderClassName;
			}
			
			@Override
			public String getUnavailableUntil() {
				return unavailableUntil;
			}
			
		};
	}
	
	/**
	 * Gets the {@link DriverConfig} instance
	 *
	 * @return DriverConfig
	 */
	protected synchronized static DriverConfig getConfig() {
		if (config == null) {
			DatabaseProvider<Database> provider;
			LocalDateTime unavailableUntil = null;
			
			try {
				DriverConfigMetadata metadata = getConfigMetadata();
				Class<? extends DatabaseProvider> clazz = null;
				if (metadata != null) {
					if (!Utils.isBlank(metadata.getDatabaseProviderClassName())) {
						clazz = Utils.loadClass(metadata.getDatabaseProviderClassName());
					}
					
					if (!Utils.isBlank(metadata.getUnavailableUntil())) {
						unavailableUntil = Utils.parseDateString(metadata.getUnavailableUntil());
					}
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
			
			config = new DefaultDriverConfig(provider.get(), new Status(unavailableUntil));
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
