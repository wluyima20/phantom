/*
 * Add Copyright
 */
package com.amiyul.phantom.api.logging;

import java.util.logging.Logger;

import com.amiyul.phantom.api.logging.DriverLogger.LoggingApi;

/**
 * Logger provider for java logging api
 */
public class JavaLoggerProvider implements LoggerProvider<JavaLogger> {
	
	@Override
	public LoggingApi getSupportedLoggingApi() {
		return LoggingApi.JAVA;
	}
	
	@Override
	public JavaLogger getLogger(String name) {
		return new JavaLogger(Logger.getLogger(name));
	}
	
}
