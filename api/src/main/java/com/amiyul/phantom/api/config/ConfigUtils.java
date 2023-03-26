/*
 * Add Copyright
 */
package com.amiyul.phantom.api.config;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.amiyul.phantom.api.DatabaseProvider;
import com.amiyul.phantom.api.ServiceLoaderUtils;

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
			Iterator<ConfigFileParser> foundParsers = ServiceLoaderUtils.getProviders(ConfigFileParser.class);
			foundParsers.forEachRemaining(parser -> parsers.add(parser));
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
		Class<DatabaseProvider> providerClass = (Class) Thread.currentThread().getContextClassLoader()
		        .loadClass(dbProviderClass);
		
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
