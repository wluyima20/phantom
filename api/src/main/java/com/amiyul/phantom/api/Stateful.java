/*
 * Add Copyright
 */
package com.amiyul.phantom.api;

/**
 * Implemented by any class for objects that can temporarily be unavailable for a specific duration
 * of time.
 */
public interface Stateful {
	
	/**
	 * Gets the status
	 *
	 * @return the status
	 */
	Status getStatus();
	
}
