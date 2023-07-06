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
package com.amiyul.phantom.driver;

import com.amiyul.phantom.api.DatabaseProvider;
import com.amiyul.phantom.api.config.ConfigMetadata;

/**
 * Holds the metadata used to build a {@link DriverConfig} instance
 */
public interface DriverConfigMetadata extends ConfigMetadata {
	
	/**
	 * Gets the class name for the {@link DatabaseProvider}
	 *
	 * @return class
	 */
	String getDatabaseProviderClassName();
	
	/**
	 * Gets unavailableUntil
	 * 
	 * @return unavailableUntil
	 */
	String getUnavailableUntil();
	
	/**
	 * Gets the path to the license file
	 *
	 * @return the file path
	 */
	String getLicenseFilePath();
	
}
