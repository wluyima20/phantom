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
 * Interface to be implemented by loggers used by the driver, if no logging API is configured, by
 * the driver defaults to slf4j if it is detected on the classpath otherwise Java logging.
 */
public interface DriverLogger {
	
	/**
	 * Logs a debug message
	 *
	 * @param message the message to log
	 */
	void debug(String message);
	
	/**
	 * Logs an info message
	 *
	 * @param message the message to log
	 */
	void info(String message);
	
	/**
	 * Logs a warning message
	 *
	 * @param message the message to log
	 */
	void warn(String message);
	
	/**
	 * Logs an error message
	 *
	 * @param message the message to log
	 * @param throwable the thrown {@link Throwable}
	 */
	void error(String message, Throwable throwable);
	
}
