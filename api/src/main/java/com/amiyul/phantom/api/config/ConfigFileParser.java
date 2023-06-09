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

/**
 * Classes that implement this interface parse the contents of a config file and generate a
 * {@link ConfigMetadata} instance
 */
public interface ConfigFileParser<T> {
	
	/**
	 * Checks whether this parser can parse the specified file
	 * 
	 * @param configFile the config file to pass
	 * @return true if the file can be parsed otherwise false
	 */
	boolean canParse(File configFile);
	
	/**
	 * Parses the specified file input stream and generates an instance encapsulating info read from the
	 * input stream
	 * 
	 * @param configInputStream the input stream for the config file to parse
	 * @return {@link ConfigMetadata}
	 * @throws Exception
	 */
	T parse(FileInputStream configInputStream) throws Exception;
	
}
