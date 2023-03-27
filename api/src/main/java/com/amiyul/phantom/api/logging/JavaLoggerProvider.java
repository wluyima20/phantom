/*
 * Add Copyright
 */
package com.amiyul.phantom.api.logging;

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
	public JavaLogger getLogger() {
		return JavaLogger.getInstance();
	}
	
}
