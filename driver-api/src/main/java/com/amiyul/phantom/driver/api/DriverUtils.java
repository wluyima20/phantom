/*
 * Add Copyright
 */
package com.amiyul.phantom.driver.api;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

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
	
	private static Client client;
	
	private static Config getConfig() throws FileNotFoundException {
		if (config == null) {
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
	
	protected static Client getClient() {
		//TODO Create client
		return client;
	}
	
}
