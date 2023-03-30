/*
 * Add Copyright
 */
package com.amiyul.phantom.db.api.config;

import com.amiyul.phantom.api.config.ConfigFileParser;

/**
 * Classes that implement this interface parse the contents of a database config file and generate a
 * {@link DatabaseMetadata} instance
 */
public interface DatabaseConfigFileParser extends ConfigFileParser<DatabaseMetadata> {}
