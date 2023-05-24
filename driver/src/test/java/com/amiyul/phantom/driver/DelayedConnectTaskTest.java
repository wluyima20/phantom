/*
 * Add Copyright
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
