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
package com.amiyul.phantom.api;

import java.sql.SQLException;

/**
 * Database facade that processes client requests, it's highly recommended to extend
 * {@link com.amiyul.phantom.api.BaseDatabase} instead of directly implementing this interface for
 * better compatibility in case the interface is changed.
 */
public interface Database {
	
	/**
	 * Processes a request from a client
	 *
	 * @param context {@link RequestContext} object
	 * @throws SQLException
	 */
	void process(RequestContext context) throws SQLException;
	
}
