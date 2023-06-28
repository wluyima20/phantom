/*
 * Copyright [yyyy] Amiyul LLC
 * All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
		super(NAME_PREFIX + requestData.getTargetDbName(), requestData.getListener());
		this.requestData = requestData;
	}
	
	@Override
	public Connection getResult() throws Exception {
		return DefaultClient.getInstance().doConnectInternal(requestData);
	}
	
}
