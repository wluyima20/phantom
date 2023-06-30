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

import java.util.Iterator;

import com.amiyul.phantom.api.ServiceLoaderUtils;
import com.amiyul.phantom.api.Utils;

/**
 * Contains logging utilities
 */
public class LoggerUtils {
	
	private static LoggerProvider provider;
	
	/**
	 * Gets the {@link LoggerProvider}
	 *
	 * @return LoggerProvider instance
	 */
	protected synchronized static LoggerProvider getProvider() {
		if (provider == null) {
			Iterator<LoggerProvider> providers = ServiceLoaderUtils.getProviders(LoggerProvider.class);
			while (providers.hasNext()) {
				provider = providers.next();
				
				debug("Found logging api provider -> " + provider.getClass());
				
				break;
			}
			
			if (provider == null && isSlf4jPresent()) {
				provider = Slf4jLoggerProvider.getInstance();
				
				debug("No registered logging api provider found, defaulting to slf4j provider");
			}
			
			if (provider == null) {
				provider = JavaLoggerProvider.getInstance();
				debug("No logging api provider found, using java logging provider as the default");
			}
		}
		
		return provider;
	}
	
	/**
	 * @see DriverLogger#debug(String)
	 */
	public static void debug(String message) {
		getProvider().getLogger().debug(message);
	}
	
	/**
	 * @see DriverLogger#info(String)
	 */
	public static void info(String message) {
		getProvider().getLogger().info(message);
	}
	
	/**
	 * @see DriverLogger#warn(String)
	 */
	public static void warn(String message) {
		getProvider().getLogger().warn(message);
	}
	
	/**
	 * @see DriverLogger#error(String, Throwable)
	 */
	public static void error(String message, Throwable throwable) {
		getProvider().getLogger().error(message, throwable);
	}
	
	/**
	 * Checks if slf4j library is on the classpath
	 * 
	 * @return true if slf4j is present otherwise false
	 */
	protected static boolean isSlf4jPresent() {
		try {
			Utils.loadClass(Slf4jLogger.CLASS_LOGGER_FACTORY);
			return true;
		}
		catch (ClassNotFoundException e) {
			//Ignore
		}
		
		return false;
	}
	
	/**
	 * Checks if java logging is the api in use
	 * 
	 * @return true if java logging is in use otherwise false
	 */
	public static boolean isUsingJavaLogger() {
		return getProvider() instanceof JavaLoggerProvider;
	}
	
}
