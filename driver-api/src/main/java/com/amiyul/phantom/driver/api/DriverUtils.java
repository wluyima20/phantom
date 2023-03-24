/*
 * Add Copyright
 */
package com.amiyul.phantom.driver.api;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.SQLException;

import com.amiyul.phantom.api.SystemUtils;
import com.amiyul.phantom.api.Utils;
import com.amiyul.phantom.api.config.Config;
import com.amiyul.phantom.api.config.ConfigFileParser;
import com.amiyul.phantom.api.config.ConfigFileParserFactory;
import com.amiyul.phantom.api.logging.DriverLogger;

public class DriverUtils {
	
	protected static DriverLogger LOGGER;
	
	protected static final String PROP_CONFIG_LOCATION = Utils.class.getPackage().getName() + ".config.location";
	
	private static Client client;
	
	public synchronized static Config loadConfig() throws FileNotFoundException {
		String filePath = System.getProperty(PROP_CONFIG_LOCATION);
		if (Utils.isBlank(filePath)) {
			filePath = SystemUtils.getEnv(PROP_CONFIG_LOCATION);
		}
		
		if (Utils.isBlank(filePath)) {
			throw new RuntimeException("Found no defined location for the driver config file");
		}
		
		LOGGER.debug("Using driver config file located at -> " + filePath);
		
		File configFile = new File(filePath);
		
		ConfigFileParser parser = ConfigFileParserFactory.createParser(configFile);
		
		if (parser == null) {
			throw new RuntimeException("No appropriate parser found for specified driver config file");
		}
		
		LOGGER.debug("Parsing driver config file with parser of type -> " + parser.getClass());
		
		return parser.parse(new FileInputStream(configFile));
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
