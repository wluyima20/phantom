/*
 * Add Copyright
 */
package com.amiyul.phantom.driver;

import java.sql.Connection;

import com.amiyul.phantom.api.RequestContext;
import com.amiyul.phantom.api.logging.LoggerUtils;

/**
 * Processes a request for a connection and notifies the {@link Listener} on success otherwise on
 * failure.
 */
public class ConnectionTask extends BaseNotifyingTask<Connection> {
	
	private final RequestContext requestContext;
	
	public ConnectionTask(RequestContext requestContext, ConnectionListener listener) {
		super("connection task", listener);
		this.requestContext = requestContext;
	}
	
	@Override
	public Connection doRun() throws Exception {
		DriverConfigUtils.getConfig().getDatabase().process(requestContext);
		
		LoggerUtils.debug("Done processing connection request");
		
		return requestContext.readResult();
	}
	
}
