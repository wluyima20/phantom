/*
 * Add Copyright
 */
package com.amiyul.phantom.api.logging;

/**
 * Logger provider for java logging api
 */
public class JavaLoggerProvider implements LoggerProvider<JavaLogger> {
	
	private JavaLoggerProvider() {
	}
	
	public static JavaLoggerProvider getInstance() {
		return JavaLoggerProviderHolder.INSTANCE;
	}
	
	@Override
	public JavaLogger getLogger() {
		return JavaLogger.getInstance();
	}
	
	private static class JavaLoggerProviderHolder {
		
		private static final JavaLoggerProvider INSTANCE = new JavaLoggerProvider();
		
	}
	
}
