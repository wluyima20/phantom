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

import java.util.List;

import com.amiyul.phantom.api.config.ConfigMetadata;

/**
 * Holds the metadata used to build a {@link DatabaseConfig} instance
 */
public interface DatabaseConfigMetadata extends ConfigMetadata {
	
	/**
	 * Gets the {@link DatabaseDefinition} list for all configured target databases
	 * 
	 * @return list of database definitions
	 */
	List<DatabaseDefinition> getDatabaseDefinitions();
	
}
