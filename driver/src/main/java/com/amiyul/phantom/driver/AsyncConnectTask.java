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
	
	protected static final String NAME_PREFIX = "async-connect-task:";
	
	private ConnectionRequestData requestData;
	
	public AsyncConnectTask(ConnectionRequestData requestData) {
		super(NAME_PREFIX + requestData.getTargetDatabaseName(), requestData.getListener());
		this.requestData = requestData;
	}
	
	@Override
	public Connection getResult() throws Exception {
		return DefaultClient.getInstance().doConnectInternal(requestData);
	}
	
}
