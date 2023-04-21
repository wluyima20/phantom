/*
 * Add Copyright
 */
package com.amiyul.phantom.driver;

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
		return DefaultClient.getInstance().doConnectInternal(requestData);
	}
	
}
