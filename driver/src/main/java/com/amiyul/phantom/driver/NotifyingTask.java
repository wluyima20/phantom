/*
 * Add Copyright
 */
package com.amiyul.phantom.driver;

/**
 * This interface should be implemented by a task that notifies a listener of the value returned by
 * the task after completion.
 *
 * @param <T> type of the value returned by the task
 */
public interface NotifyingTask<T> extends Task {
	
	/**
	 * Get the {@link Listener} object
	 *
	 * @return the {@link Listener} object
	 */
	Listener<T> getListener();
	
}
