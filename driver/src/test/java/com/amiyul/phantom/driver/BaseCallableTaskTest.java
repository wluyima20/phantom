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
import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
public class BaseCallableTaskTest {
	
	@Mock
	private Connection expectedResult;
	
	private BaseCallableTask task;
	
	@Test
	public void call_shouldInvokeDoCallAndReturnTheResult() throws Exception {
		final String dbName = "test";
		final String originalThreadName = Thread.currentThread().getName();
		ConnectionRequestData requestData = new ConnectionRequestData(dbName, false, null);
		task = Mockito.spy(new DelayedConnectTask(requestData));
		List<String> threadNames = new ArrayList<>();
		Mockito.doAnswer(invocation -> {
			threadNames.add(Thread.currentThread().getName());
			return expectedResult;
		}).when(task).doCall();
		
		Object result = task.call();
		
		Assert.assertEquals(expectedResult, result);
		Assert.assertEquals(1, threadNames.size());
		Assert.assertEquals(originalThreadName + ":delayed-connect-task:" + dbName, threadNames.get(0));
		Assert.assertEquals(originalThreadName, Thread.currentThread().getName());
	}
	
}
