/*
 * Add Copyright
 */
package com.amiyul.phantom.api.config;

import java.io.File;
import java.io.FileInputStream;

/**
 * Classes that implement this interface parse the contents of a driver config file and generate a
 * {@link Config} instance
 */
public interface ConfigFileParser {
	
	/**
	 * Checks whether this parser can parse the specified file
	 * 
	 * @param configFile the config file to pass
	 * @return true if the file can be parsed otherwise false
	 */
	boolean canParse(File configFile);
	
	/**
	 * Parses the specified file input stream and generates a {@link Config} instance
	 * 
	 * @param configInputStream the input stream for the config file to parse
	 * @return the generated config
	 */
	Config parse(FileInputStream configInputStream);
	
}
