/*
 * Add Copyright
 */
package com.amiyul.phantom.api;

/**
 * Default implementation of a {@link Response}
 */
public class DefaultResponse implements Response {
	
	private final Object result;
	
	public DefaultResponse(Object result) {
		this.result = result;
	}
	
	@Override
	public <T> T getResult() {
		return (T) result;
	}
	
}
