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
 * Shutdown hook that cleans up resources used by the driver.
 */
public class ShutdownHook implements Runnable {
	
	private ShutdownHook() {
	}
	
	protected static ShutdownHook getInstance() {
		return ShutdownHookHolder.INSTANCE;
	}
	
	@Override
	public void run() {
		DefaultClient.getInstance().shutdown();
	}
	
	private static class ShutdownHookHolder {
		
		private static final ShutdownHook INSTANCE = new ShutdownHook();
		
	}
	
}
