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
	
	default boolean isUnderMaintenanceAsOf(LocalDateTime dateTime) {
		return getUnderMaintenanceUntil() != null && dateTime.isBefore(getUnderMaintenanceUntil());
	}
	
	/**
	 * Gets the underMaintenanceUntil
	 *
	 * @return the underMaintenanceUntil
	 */
	default LocalDateTime getUnderMaintenanceUntil() {
		return null;
	}
	
	/**
	 * Sets the underMaintenanceUntil
	 *
	 * @param underMaintenanceUntil the underMaintenanceUntil to set
	 */
	default void setUnderMaintenanceUntil(LocalDateTime underMaintenanceUntil) {
	}
	
}
