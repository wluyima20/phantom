/*
 * Add Copyright
 */
package com.amiyul.phantom.db.api.config;

import java.io.File;
import java.io.FileInputStream;

import com.amiyul.phantom.api.Constants;
import com.amiyul.phantom.api.Utils;
import com.amiyul.phantom.api.logging.LoggerUtils;
import com.amiyul.phantom.db.api.DatabaseMetadata;

/**
 * Contains database utilities
 */
public class DatabaseConfigUtils {
	
	protected static final String PROP_CONFIG_LOCATION = Constants.DATABASE_NAME + ".db.config.location";
	
	private static String configFilePath;
	
	private static DatabaseConfigFileParser parser;
	
	private static DatabaseMetadata dbMetadata;
	
	private static DatabaseConfig config;
	
	/**
	 * Gets the {@link DatabaseConfig} instance
	 *
	 * @return Config
	 */
	public synchronized static DatabaseConfig getConfig() {
		if (config == null) {
			/*DatabaseProvider provider;
			try {
			    provider = getDatabaseMetadata(getDatabaseConfigFile()).getDatabaseProviderClass().newInstance();
			}
			catch (Exception e) {
			    throw new RuntimeException(e);
			}
			
			config = () -> provider.get();*/
		}
		
		return config;
	}
	
	/**
	 * Gets a {@link DatabaseConfigFileParser} via the service loader mechanism that can parse the
	 * specified database config file.
	 *
	 * @param configFile the database config file
	 * @return DatabaseConfigFileParser instance
	 */
	protected synchronized static DatabaseConfigFileParser getParser(File configFile) {
		if (parser == null) {
			parser = Utils.getParser(DatabaseConfigFileParser.class, configFile);
			if (parser == null) {
				throw new RuntimeException("No appropriate parser found for specified database config file");
			}
			
			LoggerUtils.debug("Found database config file parser -> " + parser.getClass());
		}
		
		return parser;
	}
	
	/**
	 * Gets the path to the database config file
	 *
	 * @return config file path
	 */
	protected synchronized static String getConfigFile() {
		if (configFilePath == null) {
			configFilePath = Utils.getFilePath(PROP_CONFIG_LOCATION);
			
			if (Utils.isBlank(configFilePath)) {
				throw new RuntimeException("Found no defined location for the database config file");
			}
			
			LoggerUtils.debug("Using database config file located at -> " + configFilePath);
		}
		
		return configFilePath;
	}
	
	/**
	 * Gets the {@link DatabaseMetadata} instance
	 *
	 * @param configFilePath path to database config file
	 * @return DatabaseMetadata
	 * @throws Exception
	 */
	protected synchronized static DatabaseMetadata getDatabaseMetadata(String configFilePath) throws Exception {
		if (dbMetadata == null) {
			LoggerUtils.info("Loading " + Constants.DATABASE_NAME + " database configuration");
			
			File configFile = new File(configFilePath);
			
			dbMetadata = DatabaseConfigUtils.getParser(configFile).parse(new FileInputStream(configFile));
		}
		
		return dbMetadata;
	}
	
}
