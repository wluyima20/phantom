/*
 * Add Copyright
 */
package com.amiyul.phantom.api;

/**
 * Simulation of a response sent by the {@link Database} server back to the client
 */
public interface Response {
	
	/**
	 * Gets the result.
	 *
	 * @return the result
	 */
	<T> T getResult();
	
}
