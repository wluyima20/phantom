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
	
	/**
	 * Checks if this instance is under maintenance as of the specified date and time.
	 * 
	 * @param asOfDateTime the reference date on the timeline
	 * @return true if under maintenance otherwise false
	 */
	default boolean isUnderMaintenance(LocalDateTime asOfDateTime) {
		return Utils.isDateAfter(getUnderMaintenanceUntil(), asOfDateTime);
	}
	
	/**
	 * Gets the underMaintenanceUntil
	 *
	 * @return the underMaintenanceUntil
	 */
	LocalDateTime getUnderMaintenanceUntil();
	
}
