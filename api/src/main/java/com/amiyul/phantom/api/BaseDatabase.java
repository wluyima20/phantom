/*
 * Add Copyright
 */
package com.amiyul.phantom.api;

import java.time.LocalDateTime;

/**
 * Base class for {@link Database} implementations.
 */
public abstract class BaseDatabase implements Database {
	
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
