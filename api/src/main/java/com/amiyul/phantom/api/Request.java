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

import com.amiyul.phantom.api.PhantomProtocol.Command;

/**
 * Simulation of a request sent by the database client to the {@link Database} server
 */
public interface Request {
	
	/**
	 * Gets the command to be executed by the database
	 * 
	 * @return the command
	 */
	Command getCommand();
	
	/**
	 * Gets the request context object associated to this request
	 * 
	 * @return request context
	 */
	RequestContext getContext();
	
}
