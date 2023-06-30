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

import java.util.Map;

import com.amiyul.phantom.api.config.Config;

/**
 * Marker interface for the database configuration
 */
public interface DatabaseConfig extends Config {
	
	/**
	 * Gets the map of name and {@link DatabaseDefinition} for the target databases
	 * 
	 * @return Map
	 */
	Map<String, DatabaseDefinition> getDatabaseDefinitions();
	
}
