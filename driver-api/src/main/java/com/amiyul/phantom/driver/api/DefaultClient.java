/*
 * Add Copyright
 */
package com.amiyul.phantom.driver.api;

import java.sql.Connection;
import java.sql.SQLException;

import com.amiyul.phantom.api.ConnectionRequest;
import com.amiyul.phantom.api.DefaultRequestContext;
import com.amiyul.phantom.api.RequestContext;
import com.amiyul.phantom.api.config.ConfigUtils;

/**
 * Default implementation of a {@link Client}
 */
public class DefaultClient implements Client {
	
	@Override
	public Connection connect(String targetDatabaseKey) throws SQLException {
		RequestContext requestContext = new DefaultRequestContext(new ConnectionRequest(targetDatabaseKey));
		sendRequest(requestContext);
		return requestContext.readResult();
	}
	
	/**
	 * Sends a request to the database
	 *
	 * @param context {@link RequestContext} object
	 * @throws SQLException
	 */
	protected void sendRequest(RequestContext context) throws SQLException {
		ConfigUtils.getConfig().getDatabase().process(context);
	}
	
}