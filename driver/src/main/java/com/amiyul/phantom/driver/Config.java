/*
 * Add Copyright
 */
package com.amiyul.phantom.driver;

import com.amiyul.phantom.driver.DriverLogger.LoggingApi;

import lombok.Getter;
import lombok.Setter;

/**
 * Encapsulates driver configuration info
 */
@Getter
@Setter
public class Config {
	
	private LoggingApi loggingApi;
	
	private DatabaseMetadataProvider dbMetadataProviders;
	
}
