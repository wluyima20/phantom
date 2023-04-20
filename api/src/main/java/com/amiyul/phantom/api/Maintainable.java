/*
 * Add Copyright
 */
package com.amiyul.phantom.api;

import java.time.LocalDateTime;

/**
 * Marker interface for objects that can go in under maintenance mode until a specified point in
 * time
 */
public interface Maintainable {
	
	default boolean isUnderMaintenance(LocalDateTime asOfDateTime) {
		return getUnderMaintenanceUntil() != null && asOfDateTime.isBefore(getUnderMaintenanceUntil());
	}
	
	/**
	 * Gets the underMaintenanceUntil
	 *
	 * @return the underMaintenanceUntil
	 */
	LocalDateTime getUnderMaintenanceUntil();
	
	/**
	 * Sets the underMaintenanceUntil
	 *
	 * @param underMaintenanceUntil the underMaintenanceUntil to set
	 */
	void setUnderMaintenanceUntil(LocalDateTime underMaintenanceUntil);
	
}
