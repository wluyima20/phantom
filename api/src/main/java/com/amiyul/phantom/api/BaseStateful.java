/*
 * Add Copyright
 */
package com.amiyul.phantom.api;

/**
 * Base class for {@link Stateful} implementations
 */
public abstract class BaseStateful implements Stateful {
	
	private Status status;
	
	public BaseStateful(Status status) {
		this.status = status;
	}
	
	@Override
	public Status getStatus() {
		return status;
	}
	
}
