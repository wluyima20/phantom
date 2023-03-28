/*
 * Add Copyright
 */
package com.amiyul.phantom.api.logging;

/**
 * Logger provider for java logging api
 */
public class JavaLoggerProvider implements LoggerProvider<JavaLogger> {
	
	@Override
	public JavaLogger getLogger() {
		return JavaLogger.getInstance();
	}
	
}
