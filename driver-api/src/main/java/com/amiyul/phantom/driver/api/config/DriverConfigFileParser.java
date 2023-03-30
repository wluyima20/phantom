/*
 * Add Copyright
 */
package com.amiyul.phantom.driver.api.config;

import com.amiyul.phantom.api.config.ConfigFileParser;

/**
 * Classes that implement this interface parse the contents of a driver config file and generate a
 * {@link ConfigMetadata} instance
 */
public interface DriverConfigFileParser extends ConfigFileParser<ConfigMetadata> {}
