/*
 * Copyright (C) Amiyul LLC - All Rights Reserved
 *
 * This source code is protected under international copyright law. All rights
 * reserved and protected by the copyright holder.
 * 
 * This file is confidential and only available to authorized individuals with the
 * permission of the copyright holder. If you encounter this file and do not have
 * permission, please contact the copyright holder and delete this file.
 */
package com.amiyul.phantom.db;

import java.io.File;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.amiyul.phantom.api.Constants;
import com.amiyul.phantom.api.Utils;
import com.amiyul.phantom.api.logging.LoggerUtils;

/**
 * Contains database utilities
 */
public class FileDatabaseConfigUtils {
	
	public static final String PROP_CONFIG_LOCATION = Constants.PROJECT_NAME.toUpperCase(Locale.ENGLISH)
	        + "_DB_CONFIG_LOCATION";
	
	private static String configFilePath;
	
	private static DatabaseConfigFileParser parser;
	
	private static DatabaseConfigMetadata configMetadata;
	
	private static DatabaseConfig config;
	
	/**
	 * Gets the {@link DatabaseConfig} instance
	 *
	 * @return DatabaseConfig
	 */
	protected synchronized static DatabaseConfig getConfig() {
		if (config == null) {
			List<DatabaseDefinition> dbs;
			try {
				dbs = getConfigMetadata().getDatabaseDefinitions();
			}
			catch (Exception e) {
				throw new RuntimeException(e);
			}
			
			Map<String, DatabaseDefinition> dbNameAndDefMap = dbs.stream()
			        .collect(Collectors.toMap(def -> def.getName(), Function.identity()));
			
			config = () -> dbNameAndDefMap;
		}
		
		return config;
	}
	
	/**
	 * Discards the cached database config
	 */
	protected static void discardConfig() {
		LoggerUtils.debug("Discarding database config");
		
		configMetadata = null;
		config = null;
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
			
			LoggerUtils.debug("Found database config file parser");
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
	 * Gets the {@link DatabaseConfigMetadata} instance
	 *
	 * @return DatabaseConfigMetadata
	 * @throws Exception
	 */
	protected synchronized static DatabaseConfigMetadata getConfigMetadata() throws Exception {
		if (configMetadata == null) {
			LoggerUtils.debug("Loading database configuration");
			
			File configFile = new File(getConfigFile());
			configMetadata = getParser(configFile).parse(Utils.getInputStream(configFile));
		}
		
		return configMetadata;
	}
	
}
