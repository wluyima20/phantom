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

import java.util.logging.Logger;

import lombok.Getter;

/**
 * {@link DriverLogger} implementation that delegates to the java logging api logger
 */
public final class JavaLogger implements DriverLogger {
	
	@Getter
	private final Logger nativeLogger;
	
	private JavaLogger() {
		nativeLogger = Logger.getLogger(getClass().getName());
	}
	
	public static JavaLogger getInstance() {
		return JavaLoggerHolder.INSTANCE;
	}
	
	@Override
	public void debug(String message) {
		nativeLogger.config(message);
	}
	
	@Override
	public void info(String message) {
		nativeLogger.info(message);
	}
	
	@Override
	public void warn(String message) {
		nativeLogger.warning(message);
	}
	
	@Override
	public void error(String message, Throwable throwable) {
		nativeLogger.severe(message);
		throwable.printStackTrace();
	}
	
	private static class JavaLoggerHolder {
		
		private static final JavaLogger INSTANCE = new JavaLogger();
		
	}
	
}
