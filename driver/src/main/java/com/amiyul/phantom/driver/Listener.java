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

/**
 * Marker interface for listeners that wish to be notified when a specific operation completes
 * successfully or fails.
 */
public interface Listener<T> {
	
	/**
	 * Called after a successful operation
	 * 
	 * @param object the result of the operation
	 */
	void onSuccess(T object);
	
	/**
	 * Called after a failed operation
	 *
	 * @param throwable the thrown exception
	 */
	void onFailure(Throwable throwable);
	
}
