/*
 * Add Copyright
 */
package com.amiyul.phantom.api;

import java.util.Map;

import com.amiyul.phantom.api.PhantomProtocol.Command;

/**
 * Simulation of a request sent by the database client to the {@link Database} server
 */
public interface Request {
	
	/**
	 * Gets the command to be executed by the database
	 * 
	 * @return the command
	 */
	Command getCommand();
	
	/**
	 * Gets the request attributes to send to the database
	 * 
	 * @return map
	 */
	Map<Object, Object> getAttributes();
	
}
