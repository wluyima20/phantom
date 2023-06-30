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
package com.amiyul.phantom.driver;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ DefaultClient.class })
public class ShutdownHookTest {
	
	@Mock
	private DefaultClient mockClient;
	
	@Test
	public void run_shouldShutdownTheClient() {
		PowerMockito.mockStatic(DefaultClient.class);
		Mockito.when(DefaultClient.getInstance()).thenReturn(mockClient);
		ShutdownHook.getInstance().run();
		
		Mockito.verify(mockClient).shutdown();
	}
	
}
