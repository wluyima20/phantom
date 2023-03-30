/*
 * Add Copyright
 */
package com.amiyul.phantom.db.api.config;

import java.io.File;
import java.io.FileInputStream;
import java.util.Iterator;

import com.amiyul.phantom.api.Constants;
import com.amiyul.phantom.api.ServiceLoaderUtils;
import com.amiyul.phantom.api.Utils;
import com.amiyul.phantom.api.config.ConfigUtils;
import com.amiyul.phantom.api.logging.LoggerUtils;

/**
 * Contains database utilities
 */
public class DatabaseUtils {
	
	protected static final String PROP_CONFIG_LOCATION = Constants.DATABASE_NAME + ".db.config.location";
	
	private static String configFilePath;
	
	private static DatabaseConfigFileParser parser;
	
	private static DatabaseMetadata dbMetadata;
	
	private static DatabaseConfig config;
	
	/**
	 * Creates and returns a {@link DatabaseConfigFileParser} for the specified database config file
	 *
	 * @param configFile the database config file
	 * @return DatabaseConfigFileParser instance
	 */
	protected synchronized static DatabaseConfigFileParser getParser(File configFile) {
		if (parser == null) {
			Iterator<DatabaseConfigFileParser> parsers = ServiceLoaderUtils.getProviders(DatabaseConfigFileParser.class);
			while (parsers.hasNext()) {
				DatabaseConfigFileParser candidate = parsers.next();
				if (candidate.canParse(configFile)) {
					parser = candidate;
				}
			}
			
			if (parser == null) {
				throw new RuntimeException("No appropriate parser found for specified database config file");
			}
			
			LoggerUtils.debug("Found database config file parser -> " + parser.getClass());
		}
		
		return parser;
	}
	
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
	 * Gets the path to the database config file
	 *
	 * @return config file path
	 */
	protected synchronized static String getDatabaseConfigFile() {
		if (configFilePath == null) {
			configFilePath = ConfigUtils.getConfigFile(PROP_CONFIG_LOCATION);
			
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
			
			dbMetadata = DatabaseUtils.getParser(configFile).parse(new FileInputStream(configFile));
		}
		
		return dbMetadata;
	}
	
}
