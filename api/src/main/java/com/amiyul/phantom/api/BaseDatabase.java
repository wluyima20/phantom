/*
 * Add Copyright
 */
package com.amiyul.phantom.api;

import java.sql.Connection;
import java.sql.SQLException;

import com.amiyul.phantom.api.PhantomProtocol.Command;

/**
 * Base class for {@link Database} implementations.
 */
public abstract class BaseDatabase implements Database {
	
	@Override
	public void process(RequestContext context) throws SQLException {
		final Command command = context.getRequest().getCommand();
		switch (command) {
			case CONNECT:
				context.writeResult(getConnection(((ConnectionRequest) context.getRequest()).getTargetDatabaseName()));
				break;
			case RELOAD:
				reload();
				break;
			default:
				throw new SQLException("Don't know how to process protocol command: " + command);
		}
	}
	
	/**
	 * Gets a connection to a database matching the specified name
	 *
	 * @param targetDatabaseName the name of the target database
	 * @return Connection object
	 * @throws SQLException
	 */
	public abstract Connection getConnection(String targetDatabaseName) throws SQLException;
	
	/**
	 * Processes a reload request
	 */
	public abstract void reload() throws SQLException;
	
}
