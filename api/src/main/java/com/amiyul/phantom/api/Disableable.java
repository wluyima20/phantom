/*
 * Add Copyright
 */
package com.amiyul.phantom.api;

import java.time.LocalDateTime;

/**
 * Marker interface for objects that can be disabled until a specified point in time
 */
public interface Disableable {
	
	default boolean isDisabledAsOf(LocalDateTime dateTime) {
		return getDisabledUntil() != null && dateTime.isBefore(getDisabledUntil());
	}
	
	/**
	 * Gets the disabledUntil
	 *
	 * @return the disabledUntil
	 */
	default LocalDateTime getDisabledUntil() {
		return null;
	}
	
	/**
	 * Sets the disabledUntil
	 *
	 * @param disabledUntil the disabledUntil to set
	 */
	default void setDisabledUntil(LocalDateTime disabledUntil) {
	}
	
}
