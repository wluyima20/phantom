/*
 * Add Copyright
 */
package com.amiyul.phantom.api.logging;

/**
 * Logger provider for slf4j
 */
public class Slf4jLoggerProvider implements LoggerProvider<Slf4jLogger> {
	
	private Slf4jLoggerProvider() {
	}
	
	public static Slf4jLoggerProvider getInstance() {
		return Slf4jLoggerProviderHolder.INSTANCE;
	}
	
	@Override
	public Slf4jLogger getLogger() {
		return Slf4jLogger.getInstance();
	}
	
	private static class Slf4jLoggerProviderHolder {
		
		private static final Slf4jLoggerProvider INSTANCE = new Slf4jLoggerProvider();
		
	}
	
}
