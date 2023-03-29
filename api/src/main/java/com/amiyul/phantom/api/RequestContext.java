/*
 * Add Copyright
 */
package com.amiyul.phantom.api;

/**
 * Holds contextual data about a single request sent by the client to the database
 * 
 * @param <R> The result type
 */
public interface RequestContext<R> {
	
	/**
	 * Gets request object
	 * 
	 * @return request
	 */
	Request getRequest();
	
	/**
	 * Gets response object
	 * 
	 * @return response
	 */
	Response<R> getResponse();
	
	/**
	 * Reads the result from the response object
	 * 
	 * @return result
	 */
	R readResult();
	
	/**
	 * Writes the result to the response object
	 * 
	 * @param result to write
	 */
	void writeResult(R result);
	
}
