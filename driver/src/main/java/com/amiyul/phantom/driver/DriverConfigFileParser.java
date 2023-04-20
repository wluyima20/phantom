/*
 * Add Copyright
 */
package com.amiyul.phantom.driver;

import com.amiyul.phantom.api.config.ConfigFileParser;

/**
 * Classes that implement this interface parse the contents of a driver config file and generate a
 * {@link DriverConfigMetadata} instance
 */
public interface DriverConfigFileParser extends ConfigFileParser<DriverConfigMetadata> {}