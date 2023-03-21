/*
 * Add Copyright
 */
package com.amiyul.driver.phantom;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import com.amiyul.driver.phantom.config.Config;
import com.amiyul.driver.phantom.config.ConfigFileParser;
import com.amiyul.driver.phantom.config.ConfigFileParserFactory;
import com.amiyul.driver.phantom.logging.DriverLogger;

public class Utils {
	
	protected static DriverLogger LOGGER;
	
	protected static final String PROP_CONFIG_LOCATION = Utils.class.getPackage().getName() + ".config.location";
	
	protected Config loadConfig() throws FileNotFoundException {
		String filePath = System.getProperty(PROP_CONFIG_LOCATION);
		if (isBlank(filePath)) {
			filePath = SystemUtils.getEnv(PROP_CONFIG_LOCATION);
		}
		
		if (isBlank(filePath)) {
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
	
	/**
	 * Checks if the specified string is null or an empty string or a white space character.
	 * 
	 * @param s the string to check
	 * @return true if the string is blank otherwise false
	 */
	protected static boolean isBlank(String s) {
		return s == null || s.trim().isEmpty();
	}
	
}
