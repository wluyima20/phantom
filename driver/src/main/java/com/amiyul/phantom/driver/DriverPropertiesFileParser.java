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

import static com.amiyul.phantom.driver.DriverConstants.PROP_DB_PROVIDER_CLASS;
import static com.amiyul.phantom.driver.DriverConstants.PROP_DB_UNAVAILABLE_UNTIL;

import java.util.Properties;

import com.amiyul.phantom.api.config.BasePropertiesFileParser;
import com.amiyul.phantom.api.config.ConfigFileParser;

/**
 * {@link ConfigFileParser} for a driver properties file
 */
public class DriverPropertiesFileParser extends BasePropertiesFileParser<DriverConfigMetadata> implements DriverConfigFileParser {
	
	@Override
	public DriverConfigMetadata createInstance(Properties properties) throws Exception {
		final String providerClass = properties.getProperty(PROP_DB_PROVIDER_CLASS);
		final String unavailableUntil = properties.getProperty(PROP_DB_UNAVAILABLE_UNTIL);
		return DriverConfigUtils.createMetadata(providerClass, unavailableUntil);
	}
	
}
