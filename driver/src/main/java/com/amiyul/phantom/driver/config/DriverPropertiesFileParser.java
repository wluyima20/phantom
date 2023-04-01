/*
 * Add Copyright
 */
package com.amiyul.phantom.driver.config;

import static com.amiyul.phantom.driver.config.DriverConfigMetadata.PROP_DB_PROVIDER_CLASS;

import java.util.Properties;

import com.amiyul.phantom.api.config.BasePropertiesFileParser;
import com.amiyul.phantom.api.config.ConfigFileParser;
import com.amiyul.phantom.driver.DriverConfigUtils;

/**
 * {@link ConfigFileParser} for a driver properties file
 */
public class DriverPropertiesFileParser extends BasePropertiesFileParser<DriverConfigMetadata> implements DriverConfigFileParser {
	
	@Override
	public DriverConfigMetadata createInstance(Properties properties) throws Exception {
		return DriverConfigUtils.createMetadata(properties.getProperty(PROP_DB_PROVIDER_CLASS));
	}
	
}
