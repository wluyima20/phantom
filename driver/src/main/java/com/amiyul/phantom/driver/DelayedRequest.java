/*
 * Add Copyright
 */
package com.amiyul.phantom.driver;

import java.util.concurrent.Delayed;

/**
 * This interface should be implemented by classes that encapsulate information about a delayed
 * request and the result after the request is processed.
 * 
 * @param <T> type of the request data
 */
public interface DelayedRequest<T, R> extends Delayed {
	
	/**
	 * Gets the result
	 * 
	 * @return the result
	 */
	R getResult();
	
}
