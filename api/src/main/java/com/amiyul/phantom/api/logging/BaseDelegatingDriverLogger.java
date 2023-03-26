/*
 * Add Copyright
 */
package com.amiyul.phantom.api.logging;

import lombok.Getter;

/**
 * Superclass for driver logger implementations that delegate to a native framework logger
 */
@Getter
public abstract class BaseDelegatingDriverLogger<T> implements DriverLogger {
	
	private T nativeLogger;
	
	protected BaseDelegatingDriverLogger(T nativeLogger) {
		this.nativeLogger = nativeLogger;
	}
	
}
