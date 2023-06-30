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

import java.sql.Connection;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest(DefaultClient.class)
public class DelayedConnectTaskTest {
	
	@Mock
	private DefaultClient mockClient;
	
	@Mock
	private Connection mockConnection;
	
	@Test
	public void shouldSetTheTaskNameFromTheConstructor() {
		final String dbName = "test";
		DelayedConnectTask task = new DelayedConnectTask(new ConnectionRequestData(dbName, false, null));
		Assert.assertEquals(DelayedConnectTask.NAME_PREFIX + dbName, task.getName());
	}
	
	@Test
	public void doCall_should() throws Exception {
		PowerMockito.mockStatic(DefaultClient.class);
		Mockito.when(DefaultClient.getInstance()).thenReturn(mockClient);
		ConnectionRequestData requestData = new ConnectionRequestData(null, false, null);
		Mockito.when(mockClient.doConnect(requestData)).thenReturn(mockConnection);
		
		Assert.assertEquals(mockConnection, new DelayedConnectTask(requestData).doCall());
	}
	
}
