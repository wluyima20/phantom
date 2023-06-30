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

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

import java.lang.reflect.Method;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

@RunWith(PowerMockRunner.class)
public class Slf4jLoggerTest {
	
	private Slf4jLogger logger;
	
	private Method debugEnabledMethod;
	
	private Method debugMethod;
	
	private Method infoMethod;
	
	private Method warnMethod;
	
	private Method errorMethod;
	
	@Before
	public void setup() throws Exception {
		logger = Mockito.spy(Slf4jLogger.getInstance());
		debugEnabledMethod = Whitebox.getInternalState(logger, "debugEnabledMethod");
		debugMethod = Whitebox.getInternalState(logger, "debugMethod");
		infoMethod = Whitebox.getInternalState(logger, "infoMethod");
		warnMethod = Whitebox.getInternalState(logger, "warnMethod");
		errorMethod = Whitebox.getInternalState(logger, "errorMethod");
	}
	
	@Test
	public void debug_shouldLogADebugMessage() {
		final String msg = "debug msg";
		doReturn(true).when(logger).invokeMethodSilently(debugEnabledMethod);
		
		logger.debug(msg);
		
		verify(logger).invokeMethodSilently(debugMethod, msg);
	}
	
	@Test
	public void info_shouldLogAnInfoMessage() {
		final String msg = "info msg";
		
		logger.info(msg);
		
		verify(logger).invokeMethodSilently(infoMethod, msg);
	}
	
	@Test
	public void warn_shouldLogAWarnMessage() {
		final String msg = "warn msg";
		
		logger.warn(msg);
		
		verify(logger).invokeMethodSilently(warnMethod, msg);
	}
	
	@Test
	public void error_shouldLogAnErrorMessage() {
		final String msg = "error msg";
		final Throwable throwable = new Exception();
		
		logger.error(msg, throwable);
		
		verify(logger).invokeMethodSilently(errorMethod, msg, throwable);
	}
	
}
