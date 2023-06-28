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
