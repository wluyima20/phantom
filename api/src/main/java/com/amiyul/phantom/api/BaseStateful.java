/*
 * Add Copyright
 */
package com.amiyul.phantom.api;

import java.time.LocalDateTime;

/**
 * Base class for {@link Stateful} implementations
 */
public abstract class BaseStateful implements Stateful {
	
	private LocalDateTime downUntil;
	
	public BaseStateful(LocalDateTime downUntil) {
		this.downUntil = downUntil;
	}
	
	@Override
	public LocalDateTime getDownUntil() {
		return downUntil;
	}
	
}
