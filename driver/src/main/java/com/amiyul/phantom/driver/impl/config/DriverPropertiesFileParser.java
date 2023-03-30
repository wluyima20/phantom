/*
 * Add Copyright
 */
package com.amiyul.phantom.driver.impl.config;

import java.util.Properties;

import com.amiyul.phantom.api.Utils;
import com.amiyul.phantom.api.config.BasePropertiesFileParser;
import com.amiyul.phantom.api.config.ConfigFileParser;
import com.amiyul.phantom.driver.api.DriverConfigUtils;
import com.amiyul.phantom.driver.api.config.DriverConfigMetadata;
import com.amiyul.phantom.driver.api.config.DriverConfigFileParser;

/**
 * {@link ConfigFileParser} for a driver properties file
 */
public class DriverPropertiesFileParser extends BasePropertiesFileParser<DriverConfigMetadata> implements DriverConfigFileParser {
	
	@Override
	public DriverConfigMetadata createInstance(Properties properties) throws Exception {
		String providerClassName = properties.getProperty(DriverConfigMetadata.PROP_DB_PROVIDER_CLASS);
		if (Utils.isBlank(providerClassName)) {
			throw new RuntimeException(DriverConfigMetadata.PROP_DB_PROVIDER_CLASS + " is required");
		}
		
		return DriverConfigUtils.createMetadata(providerClassName);
	}
	
}
