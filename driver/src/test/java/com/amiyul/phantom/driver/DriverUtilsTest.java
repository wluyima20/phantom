/*
 * Add Copyright
 */
package com.amiyul.phantom.driver;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ DriverConfigUtils.class, DefaultClient.class })
public class DriverUtilsTest {
	
	@Mock
	private DefaultClient mockClient;
	
	@Mock
	private Connection mockConnection;
	
	@Before
	public void setup() {
		PowerMockito.mockStatic(DefaultClient.class);
		PowerMockito.mockStatic(DriverConfigUtils.class);
		when(DefaultClient.getInstance()).thenReturn(mockClient);
	}
	
	@Test
	public void connect_shouldCallTheClientToConnectToTheTargetDb() throws Exception {
		final String dbName = "db";
		when(mockClient.connect(dbName)).thenReturn(mockConnection);
		
		assertEquals(mockConnection, DriverUtils.connect(dbName));
	}
	
	@Test
	public void connect_shouldReconnectToTheDbInCaseOfAnExceptionOnTheFirstAttempt() throws Exception {
		final String dbName = "db";
		final AtomicInteger callCount = new AtomicInteger();
		when(mockClient.connect(dbName)).thenAnswer(invocation -> {
			if (callCount.get() == 0) {
				callCount.getAndDecrement();
				throw new SQLException();
			}
			
			return mockConnection;
		});
		
		assertEquals(mockConnection, DriverUtils.connect(dbName));
		Mockito.verify(mockClient, Mockito.times(2)).connect(dbName);
		PowerMockito.verifyStatic(DriverConfigUtils.class);
		DriverConfigUtils.reloadConfig();
	}
	
	@Test
	public void connect_shouldFailIfNoTargetDbIsSpecified() {
		Exception thrown = Assert.assertThrows(SQLException.class, () -> DriverUtils.connect(""));
		
		assertEquals("No target database name defined in the database URL", thrown.getMessage());
	}
	
}
