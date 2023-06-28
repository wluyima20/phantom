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
package com.amiyul.phantom.api.logging;

/**
 * Logger provider for slf4j
 */
public class Slf4jLoggerProvider implements LoggerProvider<Slf4jLogger> {
	
	private Slf4jLoggerProvider() {
	}
	
	public static Slf4jLoggerProvider getInstance() {
		return Slf4jLoggerProviderHolder.INSTANCE;
	}
	
	@Override
	public Slf4jLogger getLogger() {
		return Slf4jLogger.getInstance();
	}
	
	private static class Slf4jLoggerProviderHolder {
		
		private static final Slf4jLoggerProvider INSTANCE = new Slf4jLoggerProvider();
		
	}
	
}
