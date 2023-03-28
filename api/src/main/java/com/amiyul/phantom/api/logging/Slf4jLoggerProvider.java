/*
 * Add Copyright
 */
package com.amiyul.phantom.api.logging;

/**
 * Logger provider for slf4j
 */
public class Slf4jLoggerProvider implements LoggerProvider<Slf4jLogger> {
	
	@Override
	public Slf4jLogger getLogger() {
		return Slf4jLogger.getInstance();
	}
	
}
