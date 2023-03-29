/*
 * Add Copyright
 */
package com.amiyul.phantom.api;

/**
 * Holds contextual data about a single request sent by the client to the database
 */
public interface RequestContext {
	
	/**
	 * Gets request object
	 * 
	 * @return request
	 */
	Request getRequest();
	
	/**
	 * Reads the result from the response
	 * 
	 * @return result
	 */
	<T> T readResult();
	
	/**
	 * Writes the result to the response
	 * 
	 * @param result to write
	 */
	void writeResult(Object result);
	
}
