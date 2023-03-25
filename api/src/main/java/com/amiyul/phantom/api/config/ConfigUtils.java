/*
 * Add Copyright
 */
package com.amiyul.phantom.api.config;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.ServiceLoader;

import com.amiyul.phantom.api.DatabaseProvider;
import com.amiyul.phantom.api.Utils;

/**
 * Contains config utilities
 */
public class ConfigUtils {
	
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
	
	/**
	 * Creates a {@link ConfigMetadata} instance
	 * 
	 * @param dbProviderClass the database provider class name
	 * @param loggingApi the name of the logging api
	 * @return ConfigMetadata object
	 * @throws Exception
	 */
	public static ConfigMetadata createMetadata(String dbProviderClass, String loggingApi) throws Exception {
		if (Utils.isBlank(dbProviderClass)) {
			throw new RuntimeException("Database provider class name is required");
		}
		
		Class<DatabaseProvider> providerClass = (Class) ConfigUtils.class.getClassLoader().loadClass(dbProviderClass);
		
		return new ConfigMetadata() {
			
			@Override
			public Class<DatabaseProvider> getDatabaseProviderClass() {
				return providerClass;
			}
			
			@Override
			public String getLoggingApi() {
				return loggingApi;
			}
			
		};
	}
	
}
