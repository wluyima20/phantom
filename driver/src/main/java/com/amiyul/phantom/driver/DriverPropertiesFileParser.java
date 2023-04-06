/*
 * Add Copyright
 */
package com.amiyul.phantom.driver;

import static com.amiyul.phantom.driver.DriverConfigMetadata.PROP_DB_PROVIDER_CLASS;

import java.util.Properties;

import com.amiyul.phantom.api.config.BasePropertiesFileParser;
import com.amiyul.phantom.api.config.ConfigFileParser;

/**
 * {@link ConfigFileParser} for a driver properties file
 */
public class DriverPropertiesFileParser extends BasePropertiesFileParser<DriverConfigMetadata> implements DriverConfigFileParser {
	
	@Override
	public DriverConfigMetadata createInstance(Properties properties) throws Exception {
		return DriverConfigUtils.createMetadata(properties.getProperty(PROP_DB_PROVIDER_CLASS));
	}
	
}
