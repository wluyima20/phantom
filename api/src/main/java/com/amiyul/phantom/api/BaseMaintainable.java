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
	
	@Override
	public LocalDateTime getUnderMaintenanceUntil() {
		return underMaintenanceUntil;
	}
	
	@Override
	public void setUnderMaintenanceUntil(LocalDateTime underMaintenanceUntil) {
		this.underMaintenanceUntil = underMaintenanceUntil;
	}
	
}
