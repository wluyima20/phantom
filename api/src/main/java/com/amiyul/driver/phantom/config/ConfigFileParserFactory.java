/*
 * Add Copyright
 */
package com.amiyul.driver.phantom.config;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.ServiceLoader;

/**
 * Factory class for {@link ConfigFileParser} instances
 */
public class ConfigFileParserFactory {
	
	/**
	 * Creates and returns a {@link ConfigFileParser} for the specified config file
	 * 
	 * @param configFile the driver config file
	 * @return ConfigFileParser instance
	 */
	public synchronized static ConfigFileParser createParser(File configFile) {
		List<ConfigFileParser> parsers = new ArrayList();
		ServiceLoader<ConfigFileParser> services = ServiceLoader.load(ConfigFileParser.class);
		services.iterator().forEachRemaining(service -> parsers.add(service));
		
		for (ConfigFileParser candidate : services) {
			if (candidate.canParse(configFile)) {
				return candidate;
			}
		}
		
		return null;
	}
	
}
