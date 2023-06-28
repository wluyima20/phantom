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
