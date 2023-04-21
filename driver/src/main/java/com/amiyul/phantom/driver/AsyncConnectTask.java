/*
 * Add Copyright
 */
package com.amiyul.phantom.driver;

import java.sql.Connection;

/**
 * Processes asynchronous connection requests and notifies the {@link Listener} on success otherwise
 * on failure.
 */
public class AsyncConnectTask extends BaseNotifyingTask<Connection> {
	
	private ConnectionRequestData requestData;
	
	public AsyncConnectTask(ConnectionRequestData requestData) {
		super("async-connect-task:" + requestData.getTargetDatabaseName(), requestData.getListener());
		this.requestData = requestData;
	}
	
	@Override
	public Connection getResult() throws Exception {
		return DefaultClient.getInstance().doConnect(requestData);
	}
	
}
