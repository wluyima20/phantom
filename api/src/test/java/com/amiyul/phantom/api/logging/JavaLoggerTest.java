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

import java.util.logging.Logger;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.powermock.reflect.Whitebox;

public class JavaLoggerTest {
	
	@Mock
	private Logger mockNativeLogger;
	
	private JavaLogger logger = JavaLogger.getInstance();
	
	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		Whitebox.setInternalState(logger, "nativeLogger", mockNativeLogger);
	}
	
	@Test
	public void debug_shouldLogADebugMessage() {
		final String msg = "debug msg";
		
		logger.debug(msg);
		
		Mockito.verify(mockNativeLogger).config(msg);
	}
	
	@Test
	public void info_shouldLogAnInfoMessage() {
		final String msg = "info msg";
		
		logger.info(msg);
		
		Mockito.verify(mockNativeLogger).info(msg);
	}
	
	@Test
	public void warn_shouldLogAWarnMessage() {
		final String msg = "warn msg";
		
		logger.warn(msg);
		
		Mockito.verify(mockNativeLogger).warning(msg);
	}
	
	@Test
	public void error_shouldLogAnErrorMessage() {
		final String msg = "error msg";
		Throwable throwable = new Throwable();
		
		logger.error(msg, throwable);
		
		Mockito.verify(mockNativeLogger).severe(msg);
	}
	
}
