/*
 * Add Copyright
 */
package com.amiyul.phantom.driver;

import static com.amiyul.phantom.api.logging.LoggerUtils.debug;

import java.sql.Connection;

/**
 * Processes delayed connection requests.
 */
public class DelayedConnectTask extends BaseCallableTask<Connection> {
	
	private ConnectionRequestData requestData;
	
	public DelayedConnectTask(ConnectionRequestData requestData) {
		super("delayed-connect-task:" + requestData.getTargetDatabaseName());
		this.requestData = requestData;
	}
	
	@Override
	public Connection doCall() throws Exception {
		debug("Processing delayed connection request");
		
		return DefaultClient.getInstance().doConnect(requestData);
	}
	
}
