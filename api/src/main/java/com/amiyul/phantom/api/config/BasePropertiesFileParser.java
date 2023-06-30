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
import java.io.FileInputStream;
import java.util.Properties;

/**
 * Base class for ConfigFileParsers for properties files
 */
public abstract class BasePropertiesFileParser<T> extends BaseExtensionConfigFileParser<T> {
	
	@Override
	public String getExtension(File configFile) {
		return "properties";
	}
	
	@Override
	public T parse(FileInputStream configInputStream) throws Exception {
		Properties props = new Properties();
		props.load(configInputStream);
		return createInstance(props);
	}
	
	/**
	 * Creates an instance using the specified properties
	 * 
	 * @param properties the properties to use
	 * @return created instance
	 */
	public abstract T createInstance(Properties properties) throws Exception;
	
}
