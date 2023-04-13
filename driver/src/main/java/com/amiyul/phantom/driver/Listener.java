/*
 * Add Copyright
 */
package com.amiyul.phantom.driver;

/**
 * Marker interface for listeners that wish to be notified when a specific operation completes
 * successfully or fails.
 */
public interface Listener<T> {
	
	/**
	 * Called after a successful operation
	 * 
	 * @param object the result of the operation
	 */
	void onSuccess(T object);
	
	/**
	 * Called after a failed operation
	 *
	 * @param throwable the thrown exception
	 */
	void onFailure(Throwable throwable);
	
}
