/*
 * Add Copyright
 */
package com.amiyul.driver.phantom.server;

import java.sql.SQLException;

import com.amiyul.driver.phantom.Request;
import com.amiyul.driver.phantom.RequestContext;
import com.amiyul.driver.phantom.Response;

/**
 * Simulation of a database server to process connection requests from the
 * {@link com.amiyul.driver.phantom.Client}
 */
public interface Server {
	
	/**
	 * Starts the server, implementation can use this method for initialization
	 */
	void start();
	
	/**
	 * Processes requests from the client
	 * 
	 * @param context {@link RequestContext} object
	 * @return Connection object
	 * @throws SQLException
	 */
	<TR, T extends Request<TR>, SR, S extends Response<SR>> void accept(RequestContext<TR, T, SR, S> context)
	    throws SQLException;
	
	/**
	 * Stops the server, implementation can use this method to clean up and release any resources
	 */
	void shutdown();
	
}
