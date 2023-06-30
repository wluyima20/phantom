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
 * Logger provider for java logging api
 */
public class JavaLoggerProvider implements LoggerProvider<JavaLogger> {
	
	private JavaLoggerProvider() {
	}
	
	public static JavaLoggerProvider getInstance() {
		return JavaLoggerProviderHolder.INSTANCE;
	}
	
	@Override
	public JavaLogger getLogger() {
		return JavaLogger.getInstance();
	}
	
	private static class JavaLoggerProviderHolder {
		
		private static final JavaLoggerProvider INSTANCE = new JavaLoggerProvider();
		
	}
	
}
