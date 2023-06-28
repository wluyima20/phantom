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

import lombok.Getter;

/**
 * Encapsulates information about a request for a connection
 */
public class ConnectionRequestData {
	
	@Getter
	private final String targetDbName;
	
	@Getter
	private final boolean async;
	
	@Getter
	private final ConnectionListener listener;
	
	public ConnectionRequestData(String targetDbName, boolean async, ConnectionListener listener) {
		this.targetDbName = targetDbName;
		this.async = async;
		this.listener = listener;
	}
	
	@Override
	public String toString() {
		return "{" + "targetDbName=" + targetDbName + ", async=" + async + ", listener=" + listener + "}";
	}
	
}
