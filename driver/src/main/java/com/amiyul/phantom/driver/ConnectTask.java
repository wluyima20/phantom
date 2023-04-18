/*
 * Add Copyright
 */
package com.amiyul.phantom.driver;

import java.sql.Connection;

/**
 * Processes a database connection request and notifies the {@link Listener} on success otherwise on
 * failure.
 */
public class ConnectTask extends BaseNotifyingTask<Connection> {
	
	private ConnectionRequestData requestData;
	
	public ConnectTask(ConnectionRequestData requestData) {
		super("connect-task:" + requestData.getTargetDatabaseName(), requestData.getListener());
		this.requestData = requestData;
	}
	
	@Override
	public Connection doRun() throws Exception {
		return DefaultClient.getInstance().doConnect(requestData);
	}
	
}
