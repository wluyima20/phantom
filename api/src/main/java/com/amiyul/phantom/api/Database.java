/*
 * Add Copyright
 */
package com.amiyul.phantom.api;

import java.sql.SQLException;

/**
 * Simulation of a database to process connection requests from the client, it's highly recommended
 * to extend {@link com.amiyul.phantom.api.BaseDatabase} instead of directly implementing this
 * interface for better compatibility in case the interface is changed.
 */
public interface Database {
	
	/**
	 * Processes a request from a client
	 *
	 * @param context {@link RequestContext} object
	 * @throws SQLException
	 */
	void process(RequestContext context) throws SQLException;
	
}
