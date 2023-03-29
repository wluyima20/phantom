/*
 * Add Copyright
 */
package com.amiyul.phantom.api;

import java.sql.SQLException;

/**
 * Simulation of a database to process connection requests from the client
 */
public interface Database extends Disableable {
	
	/**
	 * Processes a request from a client
	 *
	 * @param context {@link RequestContext} object
	 * @throws SQLException
	 */
	void process(RequestContext context) throws SQLException;
	
}
