/*
 * Add Copyright
 */
package com.amiyul.phantom.api.config;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.ServiceLoader;

/**
 * Factory class for {@link ConfigFileParser} instances
 */
public class ConfigFileParserFactory {
	
	private static List<ConfigFileParser> parsers;
	
	/**
	 * Creates and returns a {@link ConfigFileParser} for the specified config file
	 *
	 * @param configFile the driver config file
	 * @return ConfigFileParser instance
	 */
	public synchronized static ConfigFileParser createParser(File configFile) {
		if (parsers == null) {
			parsers = new ArrayList();
			ServiceLoader<ConfigFileParser> services = ServiceLoader.load(ConfigFileParser.class);
			services.iterator().forEachRemaining(service -> parsers.add(service));
		}
		
		for (ConfigFileParser candidate : parsers) {
			if (candidate.canParse(configFile)) {
				return candidate;
			}
		}
		
		return null;
	}
	
}
