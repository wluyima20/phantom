/*
 * Add Copyright
 */
package com.amiyul.phantom.driver.api;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.SQLException;

import com.amiyul.phantom.api.Constants;
import com.amiyul.phantom.api.SystemUtils;
import com.amiyul.phantom.api.Utils;
import com.amiyul.phantom.api.config.Config;
import com.amiyul.phantom.api.config.ConfigFileParser;
import com.amiyul.phantom.api.config.ConfigFileParserFactory;
import com.amiyul.phantom.api.logging.DriverLogger;

public class DriverUtils {
	
	protected static DriverLogger LOGGER;
	
	protected static final String PROP_CONFIG_LOCATION = Utils.class.getPackage().getName() + ".config.location";
	
	private static String configFilePath;
	
	private static Config config;
	
	private static ServerManager serverManager;
	
	private static Client client;
	
	private static Config getConfig(boolean reload) throws FileNotFoundException {
		if (config == null || reload) {
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
			
			ConfigFileParser parser = ConfigFileParserFactory.createParser(configFile);
			
			if (parser == null) {
				throw new RuntimeException("No appropriate parser found for specified driver config file");
			}
			
			LOGGER.debug("Parsing driver config file with parser of type -> " + parser.getClass());
			
			config = parser.parse(new FileInputStream(configFile));
		}
		
		return config;
	}
	
	private static synchronized void initServerManagerIfNecessary(boolean reload) throws FileNotFoundException {
		if (serverManager == null || reload) {
			if (serverManager != null) {
				try {
					serverManager.stopServer();
				}
				catch (Exception e) {
					LOGGER.warn("Failed to stop database server with error");
				}
			}
			
			serverManager = ServerManagerFactory.getInstance().createManager(getConfig(reload).getServer());
			serverManager.startServer();
		}
	}
	
	protected static Connection getConnection(String databaseKey) throws FileNotFoundException, SQLException {
		initServerManagerIfNecessary(false);
		
		Connection connection = getConnection(databaseKey, true);
		if (connection != null) {
			return connection;
		}
		
		LOGGER.debug("Failed to obtain Connection to database with key " + databaseKey + ", refreshing config");
		
		//TODO If server is disabled, wait to try again
		//TODO get the timeout and keep trying again before failing
		initServerManagerIfNecessary(true);
		
		return getConnection(databaseKey, false);
	}
	
	protected static Connection getConnection(String databaseKey, boolean suppressExceptions) throws SQLException {
		LOGGER.debug("Obtaining connection to database with key: " + databaseKey);
		
		try {
			Connection connection = client.connect(databaseKey);
			//TODO call this asynchronously to avoid blocking for slow running provider listeners
			/*try {
			    metadata.getProvider().onConnectionSuccess(metadata);
			}
			catch (Throwable t) {
			    LOGGER.error(
			            "An error occurred while notifying a database metadata provider of a successful " + "connection", t);
			}*/
			
			return connection;
		}
		catch (SQLException e) {
			//TODO call this asynchronously to avoid blocking for slow running provider listeners
			/*try {
			    metadata.getProvider().onConnectionFailure(metadata, e);
			}
			catch (Throwable t) {
			    LOGGER.error("An error occurred while notifying a database metadata provider of a failed attempt "
			                    + "to obtain a connection",
			            t);
			}*/
			
			if (!suppressExceptions) {
				throw e;
			}
			
			return null;
		}
	}
	
}
