/*
 * Add Copyright
 */
package com.amiyul.phantom.driver;

import static com.amiyul.phantom.driver.DriverConstants.DEFAULT_THREAD_SIZE;
import static com.amiyul.phantom.driver.DriverConstants.PROP_DRIVER_ASYNC;
import static com.amiyul.phantom.driver.DriverConstants.PROP_DRIVER_CONN_LISTENER;
import static com.amiyul.phantom.driver.DriverConstants.URL_PREFIX;
import static com.amiyul.phantom.driver.DriverConstants.URL_SEPARATOR_DB_PARAM;
import static com.amiyul.phantom.driver.DriverConstants.URL_SEPARATOR_PARAMS;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.amiyul.phantom.api.RuntimeUtils;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ DriverConfigUtils.class, DefaultClient.class, DriverUtils.class, RuntimeUtils.class })
public class DriverUtilsTest {
	
	public static class MockListener implements ConnectionListener {
		
		@Override
		public void onSuccess(Connection object) {
		}
		
		@Override
		public void onFailure(Throwable throwable) {
		}
		
	}
	
	@Mock
	private DefaultClient mockClient;
	
	@Mock
	private Connection mockConnection;
	
	@Mock
	private ConnectionRequestData mockRequestData;
	
	@Mock
	private Properties mockProps;
	
	@Before
	public void setup() {
		PowerMockito.mockStatic(DefaultClient.class);
		PowerMockito.mockStatic(DriverConfigUtils.class);
		PowerMockito.mockStatic(DriverUtils.class);
		PowerMockito.mockStatic(RuntimeUtils.class);
		when(DefaultClient.getInstance()).thenReturn(mockClient);
		PowerMockito.spy(DriverUtils.class);
	}
	
	@Test
	public void connect_shouldCallTheClientToObtainTheConnection() throws Exception {
		final String url = "jdbc:db://localhost";
		when(mockClient.connect(mockRequestData)).thenReturn(mockConnection);
		when(mockClient.connect(any(ConnectionRequestData.class))).thenReturn(mockConnection);
		
		assertEquals(mockConnection, DriverUtils.connect(url, mockProps));
		PowerMockito.verifyStatic(DriverUtils.class);
		DriverUtils.createRequest(url, mockProps);
	}
	
	@Test
	public void connect_shouldTrimTheUrlBeforeCallingTheClientToObtainTheConnection() throws Exception {
		final String url = "jdbc:db://localhost";
		when(mockClient.connect(mockRequestData)).thenReturn(mockConnection);
		when(mockClient.connect(any(ConnectionRequestData.class))).thenReturn(mockConnection);
		
		assertEquals(mockConnection, DriverUtils.connect(url + " ", mockProps));
		PowerMockito.verifyStatic(DriverUtils.class);
		DriverUtils.createRequest(url, mockProps);
	}
	
	@Test
	public void getDefaultAsyncExecutorThreadCount_shouldMatchTheAvailableProcessorCount() {
		final int processorCount = 4;
		when(RuntimeUtils.getAvailableProcessors()).thenReturn(processorCount);
		
		assertEquals(processorCount, DriverUtils.getDefaultAsyncExecutorThreadCount());
	}
	
	@Test
	public void getDefaultAsyncExecutorThreadCount_shouldGetTheDefaultValueIfAvailableProcessorsIsLessThanOne() {
		when(RuntimeUtils.getAvailableProcessors()).thenReturn(1);
		
		assertEquals(DEFAULT_THREAD_SIZE, DriverUtils.getDefaultAsyncExecutorThreadCount());
	}
	
	@Test
	public void getDefaultAsyncExecutorThreadCount_shouldDefaultToFiftyIfAvailableProcessorsIsMore() {
		when(RuntimeUtils.getAvailableProcessors()).thenReturn(51);
		
		assertEquals(50, DriverUtils.getDefaultAsyncExecutorThreadCount());
	}
	
	@Test
	public void getDefaultDelayedExecutorThreadCount_shouldMatchTheAvailableProcessorCount() {
		final int processorCount = 3;
		when(RuntimeUtils.getAvailableProcessors()).thenReturn(processorCount);
		
		assertEquals(processorCount, DriverUtils.getDefaultDelayedExecutorThreadCount());
	}
	
	@Test
	public void getDefaultDelayedExecutorThreadCount_shouldGetTheDefaultValueIfAvailableProcessorsIsLessThanOne() {
		when(RuntimeUtils.getAvailableProcessors()).thenReturn(1);
		
		assertEquals(DEFAULT_THREAD_SIZE, DriverUtils.getDefaultDelayedExecutorThreadCount());
	}
	
	@Test
	public void getDefaultDelayedExecutorThreadCount_shouldDefaultToTenIfAvailableProcessorsIsMore() {
		when(RuntimeUtils.getAvailableProcessors()).thenReturn(11);
		
		assertEquals(10, DriverUtils.getDefaultDelayedExecutorThreadCount());
	}
	
	@Test
	public void createRequest_shouldCreateAConnectionRequestDataObject() throws Exception {
		final String dbName = "mysql-test";
		
		ConnectionRequestData reqData = DriverUtils.createRequest(URL_PREFIX + dbName, mockProps);
		
		assertEquals(dbName, reqData.getTargetDbName());
		assertFalse(reqData.isAsync());
		assertNull(reqData.getListener());
	}
	
	@Test
	public void createRequest_shouldReadAndSetConfigsFromThePropertiesObject() throws Exception {
		Properties props = new Properties();
		props.setProperty(PROP_DRIVER_ASYNC, "true");
		props.setProperty(PROP_DRIVER_CONN_LISTENER, MockListener.class.getName());
		ConnectionRequestData reqData = DriverUtils.createRequest(URL_PREFIX + "test", props);
		
		assertTrue(reqData.isAsync());
		assertEquals(MockListener.class, reqData.getListener().getClass());
	}
	
	@Test
	public void createRequest_shouldReadAndSetConfigsFromTheUrl() throws Exception {
		ConnectionRequestData reqData = DriverUtils
		        .createRequest(URL_PREFIX + "test" + URL_SEPARATOR_DB_PARAM + PROP_DRIVER_ASYNC + "=true"
		                + URL_SEPARATOR_PARAMS + PROP_DRIVER_CONN_LISTENER + "=" + MockListener.class.getName(),
		            mockProps);
		
		assertTrue(reqData.isAsync());
		assertEquals(MockListener.class, reqData.getListener().getClass());
	}
	
	@Test
	public void createRequest_propertyArgumentShouldTakePrecedenceOverThoseSetOnTheUrl() throws Exception {
		Properties props = new Properties();
		props.setProperty(PROP_DRIVER_ASYNC, "true");
		props.setProperty(PROP_DRIVER_CONN_LISTENER, MockListener.class.getName());
		ConnectionRequestData reqData = DriverUtils.createRequest(URL_PREFIX + "test" + URL_SEPARATOR_DB_PARAM
		        + PROP_DRIVER_ASYNC + "=false" + URL_SEPARATOR_PARAMS + PROP_DRIVER_CONN_LISTENER + "=someClass",
		    props);
		
		assertTrue(reqData.isAsync());
		assertEquals(MockListener.class, reqData.getListener().getClass());
	}
	
	@Test
	public void createRequest_shouldFailForAnInvalidUrlParam() {
		final String propName = "testProp";
		Throwable thrown = Assert.assertThrows(SQLException.class,
		    () -> DriverUtils.createRequest(URL_PREFIX + "test" + URL_SEPARATOR_DB_PARAM + propName + "=", mockProps));
		
		assertEquals("Connection URL contains unsupported parameter named: " + propName, thrown.getMessage());
	}
	
	@Test
	public void createRequest_shouldFailForAnAsyncCallAndNoListenerIsSpecified() {
		Throwable thrown = Assert.assertThrows(SQLException.class, () -> DriverUtils
		        .createRequest(URL_PREFIX + "test" + URL_SEPARATOR_DB_PARAM + PROP_DRIVER_ASYNC + "=true", mockProps));
		
		assertEquals(PROP_DRIVER_CONN_LISTENER + " is required for asynchronous get connection calls", thrown.getMessage());
	}
	
	@Test
	public void createRequest_shouldFailIfNoTargetDbIsSpecified() throws Exception {
		Throwable thrown = Assert.assertThrows(SQLException.class, () -> DriverUtils.createRequest(URL_PREFIX, mockProps));
		
		assertEquals("No target database name defined in the database URL", thrown.getMessage());
	}
	
}
