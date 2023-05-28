/*
 * Add Copyright
 */
package com.amiyul.phantom.driver;

import static com.amiyul.phantom.driver.DriverConstants.DEFAULT_THREAD_SIZE;
import static com.amiyul.phantom.driver.DriverConstants.PROP_DRIVER_ASYNC;
import static com.amiyul.phantom.driver.DriverConstants.PROP_DRIVER_CONN_LISTENER_CLASS;
import static com.amiyul.phantom.driver.DriverConstants.URL_PREFIX;
import static com.amiyul.phantom.driver.DriverConstants.URL_SEPARATOR_DB_PARAM;
import static com.amiyul.phantom.driver.DriverConstants.URL_SEPARATOR_PARAMS;
import static com.amiyul.phantom.driver.DriverProperty.ASYNC;
import static com.amiyul.phantom.driver.DriverProperty.CONNECTION_LISTENER;
import static com.amiyul.phantom.driver.DriverProperty.TARGET_DB;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;
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
	public void createDriverPropertyAndValueMap_shouldCreateAConnectionRequestDataObject() {
		final String dbName = "mysql-test";
		
		Map<DriverProperty, String> map = DriverUtils.createDriverPropertyAndValueMap(URL_PREFIX + dbName, mockProps);
		
		assertEquals(dbName, map.get(TARGET_DB));
		assertNull(map.get(ASYNC));
		assertNull(map.get(CONNECTION_LISTENER));
	}
	
	@Test
	public void createDriverPropertyAndValueMap_shouldReadAndSetConfigsFromThePropertiesObject() {
		Properties props = new Properties();
		final String async = "true";
		props.setProperty(PROP_DRIVER_ASYNC, async);
		props.setProperty(PROP_DRIVER_CONN_LISTENER_CLASS, MockListener.class.getName());
		
		Map<DriverProperty, String> map = DriverUtils.createDriverPropertyAndValueMap(URL_PREFIX + "test", props);
		
		assertEquals(async, map.get(ASYNC));
		assertEquals(MockListener.class.getName(), map.get(CONNECTION_LISTENER));
	}
	
	@Test
	public void createDriverPropertyAndValueMap_shouldReadAndSetConfigsFromTheUrl() {
		final String async = "true";
		
		Map<DriverProperty, String> map = DriverUtils.createDriverPropertyAndValueMap(
		    URL_PREFIX + "test" + URL_SEPARATOR_DB_PARAM + PROP_DRIVER_ASYNC + "=" + async + URL_SEPARATOR_PARAMS
		            + PROP_DRIVER_CONN_LISTENER_CLASS + "=" + MockListener.class.getName(),
		    mockProps);
		
		assertEquals(async, map.get(ASYNC));
		assertEquals(MockListener.class.getName(), map.get(CONNECTION_LISTENER));
	}
	
	@Test
	public void createDriverPropertyAndValueMap_propertyArgumentShouldTakePrecedenceOverThoseSetOnTheUrl() {
		final String async = "true";
		Properties props = new Properties();
		props.setProperty(PROP_DRIVER_ASYNC, async);
		props.setProperty(PROP_DRIVER_CONN_LISTENER_CLASS, MockListener.class.getName());
		
		Map<DriverProperty, String> map = DriverUtils
		        .createDriverPropertyAndValueMap(URL_PREFIX + "test" + URL_SEPARATOR_DB_PARAM + PROP_DRIVER_ASYNC + "=false"
		                + URL_SEPARATOR_PARAMS + PROP_DRIVER_CONN_LISTENER_CLASS + "=someClass",
		            props);
		
		assertEquals(async, map.get(ASYNC));
		assertEquals(MockListener.class.getName(), map.get(CONNECTION_LISTENER));
	}
	
	@Test
	public void createDriverPropertyAndValueMap_shouldFailForAnInvalidProperty() {
		final String propName = "testProp";
		Properties props = new Properties();
		props.setProperty(propName, "");
		
		Throwable thrown = Assert.assertThrows(RuntimeException.class,
		    () -> DriverUtils.createDriverPropertyAndValueMap(URL_PREFIX + "test", props));
		
		assertEquals("Found an invalid driver property named: " + propName, thrown.getMessage());
	}
	
	@Test
	public void createDriverPropertyAndValueMap_shouldFailForAnInvalidUrlParam() {
		final String propName = "testProp";
		Throwable thrown = Assert.assertThrows(RuntimeException.class, () -> DriverUtils
		        .createDriverPropertyAndValueMap(URL_PREFIX + "test" + URL_SEPARATOR_DB_PARAM + propName + "=", mockProps));
		
		assertEquals("Found an invalid driver property named: " + propName, thrown.getMessage());
	}
	
	@Test
	public void createRequest_shouldFailForAnAsyncCallAndNoListenerIsSpecified() {
		Throwable thrown = Assert.assertThrows(SQLException.class, () -> DriverUtils
		        .createRequest(URL_PREFIX + "test" + URL_SEPARATOR_DB_PARAM + PROP_DRIVER_ASYNC + "=true", mockProps));
		
		assertEquals(PROP_DRIVER_CONN_LISTENER_CLASS + " is required for asynchronous connection requests", thrown.getMessage());
	}
	
	@Test
	public void createRequest_shouldFailIfNoTargetDbIsSpecified() {
		Throwable thrown = Assert.assertThrows(SQLException.class, () -> DriverUtils.createRequest(URL_PREFIX, mockProps));
		
		assertEquals("No target database name defined in the database URL", thrown.getMessage());
	}
	
}
