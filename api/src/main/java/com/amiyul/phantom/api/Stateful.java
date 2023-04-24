/*
 * Add Copyright
 */
package com.amiyul.phantom.api;

import java.time.LocalDateTime;

/**
 * Implemented by any class for objects that can temporarily be down for a specific duration of
 * time.
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
