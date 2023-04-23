/*
 * Add Copyright
 */
package com.amiyul.phantom.db;

import java.time.LocalDateTime;

import com.amiyul.phantom.api.Utils;

/**
 * Implemented by any class for objects that can temporarily be unavailable for a specific duration
 * of time.
 */
public interface Stateful {
	
	/**
	 * Checks if this instance is down as of the specified date and time.
	 * 
	 * @param asOfDateTime the reference date on the timeline
	 * @return true if is down otherwise false
	 */
	default boolean isDown(LocalDateTime asOfDateTime) {
		return Utils.isDateAfter(getDownUntil(), asOfDateTime);
	}
	
	/**
	 * Gets the downUntil
	 *
	 * @return the downUntil
	 */
	LocalDateTime getDownUntil();
	
}
