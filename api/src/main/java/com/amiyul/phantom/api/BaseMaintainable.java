/*
 * Add Copyright
 */
package com.amiyul.phantom.api;

import java.time.LocalDateTime;

/**
 * Base class for {@link Maintainable} implementations
 */
public abstract class BaseMaintainable implements Maintainable {
	
	private LocalDateTime underMaintenanceUntil;
	
	public BaseMaintainable(LocalDateTime underMaintenanceUntil) {
		this.underMaintenanceUntil = underMaintenanceUntil;
	}
	
	@Override
	public LocalDateTime getUnderMaintenanceUntil() {
		return underMaintenanceUntil;
	}
	
}
