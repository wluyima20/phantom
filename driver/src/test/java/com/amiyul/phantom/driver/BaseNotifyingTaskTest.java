/*
 * Add Copyright
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
public class BaseNotifyingTaskTest {
	
	@Mock
	private ConnectionListener mockListener;
	
	@Mock
	private Connection expectedResult;
	
	private BaseNotifyingTask task;
	
	@Test
	public void run_shouldGetTheResultAndNotifyTheListener() throws Exception {
		final String dbName = "test";
		final String originalThreadName = Thread.currentThread().getName();
		ConnectionRequestData requestData = new ConnectionRequestData(dbName, false, mockListener);
		task = Mockito.spy(new AsyncConnectTask(requestData));
		List<String> threadNames = new ArrayList<>();
		Mockito.doAnswer(invocation -> {
			threadNames.add(Thread.currentThread().getName());
			return expectedResult;
		}).when(task).getResult();
		
		task.run();
		
		Mockito.verify(mockListener).onSuccess(expectedResult);
		Assert.assertEquals(1, threadNames.size());
		Assert.assertEquals(originalThreadName + ":async-connect-task:" + dbName, threadNames.get(0));
		Assert.assertEquals(originalThreadName, Thread.currentThread().getName());
	}
	
	@Test
	public void run_shouldNotifyTheListenerWhenAnExceptionIsEncountered() throws Exception {
		final String dbName = "test";
		ConnectionRequestData requestData = new ConnectionRequestData(dbName, false, mockListener);
		task = Mockito.spy(new AsyncConnectTask(requestData));
		Throwable throwable = new Exception();
		Mockito.doThrow(throwable).when(task).getResult();
		
		task.run();
		
		Mockito.verify(mockListener).onFailure(throwable);
	}
	
}
