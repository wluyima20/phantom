/*
 * Add Copyright
 */
package com.amiyul.phantom.db.config;

import com.amiyul.phantom.api.config.ConfigFileParser;

/**
 * Classes that implement this interface parse the contents of a database config file and generate a
 * {@link DatabaseConfigMetadata} instance
 */
public interface DatabaseConfigFileParser extends ConfigFileParser<DatabaseConfigMetadata> {}
