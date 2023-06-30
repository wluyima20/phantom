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
