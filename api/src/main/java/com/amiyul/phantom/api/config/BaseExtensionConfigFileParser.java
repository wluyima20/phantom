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
package com.amiyul.phantom.api.config;

import java.io.File;

/**
 * Base class for ConfigFileParsers that check the config file extension to determine if they can
 * parse the file
 */
public abstract class BaseExtensionConfigFileParser<T> implements ConfigFileParser<T> {
	
	@Override
	public boolean canParse(File configFile) {
		return configFile.getName().endsWith("." + getExtension(configFile));
	}
	
	/**
	 * Gets the supported file extension
	 * 
	 * @param configFile config file
	 * @return file extension
	 */
	public abstract String getExtension(File configFile);
	
}
