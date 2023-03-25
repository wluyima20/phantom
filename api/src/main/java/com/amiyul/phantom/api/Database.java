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
	 * @return Connection object
	 * @throws SQLException
	 */
	<TR, T extends Request<TR>, SR, S extends Response<SR>> void process(RequestContext<TR, T, SR, S> context)
	    throws SQLException;
	
}
