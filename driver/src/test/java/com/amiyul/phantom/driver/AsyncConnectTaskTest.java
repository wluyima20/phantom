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
public class AsyncConnectTaskTest {
	
	@Mock
	private DefaultClient mockClient;
	
	@Mock
	private ConnectionRequestData mockRequestData;
	
	@Mock
	private Connection mockConnection;
	
	@Test
	public void getResult_shouldReturnTheConnectionObject() throws Exception {
		PowerMockito.mockStatic(DefaultClient.class);
		Mockito.when(DefaultClient.getInstance()).thenReturn(mockClient);
		Mockito.when(mockClient.doConnectInternal(mockRequestData)).thenReturn(mockConnection);
		AsyncConnectTask task = new AsyncConnectTask(mockRequestData);
		
		Assert.assertEquals(mockConnection, task.getResult());
	}
	
}
