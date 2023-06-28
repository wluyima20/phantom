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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import com.amiyul.phantom.api.Utils;

/**
 * {@link DriverLogger} implementation that delegates to slf4j
 */
public class Slf4jLogger implements DriverLogger {
	
	protected static final String CLASS_LOGGER_FACTORY = "org.slf4j.LoggerFactory";
	
	private final Object nativeLogger;
	
	private final Method debugEnabledMethod;
	
	private final Method debugMethod;
	
	private final Method infoMethod;
	
	private final Method warnMethod;
	
	private final Method errorMethod;
	
	private Slf4jLogger() {
		try {
			Class<?> factoryClass = Utils.loadClass(CLASS_LOGGER_FACTORY);
			Method getLoggerMethod = factoryClass.getDeclaredMethod("getLogger", Class.class);
			nativeLogger = getLoggerMethod.invoke(null, getClass());
			debugMethod = nativeLogger.getClass().getDeclaredMethod("debug", String.class);
			infoMethod = nativeLogger.getClass().getDeclaredMethod("info", String.class);
			warnMethod = nativeLogger.getClass().getDeclaredMethod("warn", String.class);
			errorMethod = nativeLogger.getClass().getDeclaredMethod("error", String.class, Throwable.class);
			debugEnabledMethod = nativeLogger.getClass().getDeclaredMethod("isDebugEnabled");
		}
		catch (ReflectiveOperationException e) {
			throw new RuntimeException(e);
		}
	}
	
	public static Slf4jLogger getInstance() {
		return Slf4jLoggerHolder.INSTANCE;
	}
	
	@Override
	public void debug(String message) {
		if (isDebugEnabled()) {
			invokeMethodSilently(debugMethod, message);
		}
	}
	
	@Override
	public void info(String message) {
		invokeMethodSilently(infoMethod, message);
	}
	
	@Override
	public void warn(String message) {
		invokeMethodSilently(warnMethod, message);
	}
	
	@Override
	public void error(String message, Throwable throwable) {
		invokeMethodSilently(errorMethod, message, throwable);
	}
	
	private boolean isDebugEnabled() {
		return invokeMethodSilently(debugEnabledMethod);
	}
	
	protected <T> T invokeMethodSilently(Method method, Object... args) {
		try {
			return (T) method.invoke(nativeLogger, args);
		}
		catch (IllegalAccessException | InvocationTargetException e) {
			//Ignore
		}
		
		return null;
	}
	
	private static class Slf4jLoggerHolder {
		
		private static final Slf4jLogger INSTANCE = new Slf4jLogger();
		
	}
	
}
