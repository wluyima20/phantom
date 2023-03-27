/*
 * Add Copyright
 */
package com.amiyul.phantom.api.logging;

import lombok.Getter;

/**
 * Superclass for driver logger implementations that delegate to a native framework logger
 */
@Getter
public abstract class BaseDelegatingLogger<T> implements DriverLogger {
	
	private T nativeLogger;
	
	protected BaseDelegatingLogger(T nativeLogger) {
		this.nativeLogger = nativeLogger;
	}
	
}
