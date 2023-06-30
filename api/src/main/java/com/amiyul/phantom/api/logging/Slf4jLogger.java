/*
 * Copyright (C) Amiyul LLC - All Rights Reserved
 *
 * This source code is protected under international copyright law. All rights
 * reserved and protected by the copyright holder.
 * 
 * This file is confidential and only available to authorized individuals with the
 * permission of the copyright holder. If you encounter this file and do not have
 * permission, please contact the copyright holder and delete this file.
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
