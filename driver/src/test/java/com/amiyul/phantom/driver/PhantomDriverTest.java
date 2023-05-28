/*
 * Add Copyright
 */
package com.amiyul.phantom.driver;

import static com.amiyul.phantom.driver.DriverConstants.PROP_DRIVER_ASYNC;
import static com.amiyul.phantom.driver.DriverConstants.PROP_DRIVER_CONN_LISTENER_CLASS;
import static com.amiyul.phantom.driver.DriverConstants.PROP_DRIVER_TARGET_DB;
import static com.amiyul.phantom.driver.DriverConstants.URL_PREFIX;
import static org.junit.Assert.assertEquals;

import java.sql.DriverPropertyInfo;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.amiyul.phantom.api.Utils;
import com.amiyul.phantom.api.logging.JavaLogger;
import com.amiyul.phantom.api.logging.LoggerUtils;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ DriverUtils.class, LoggerUtils.class, Utils.class })
public class PhantomDriverTest {
	
	@Mock
	private Properties mockProps;
	
	private PhantomDriver driver;
	
	@Before
	public void setup() {
		PowerMockito.mockStatic(DriverUtils.class);
		PowerMockito.mockStatic(LoggerUtils.class);
		PowerMockito.mockStatic(Utils.class);
		Mockito.when(Utils.isBlank(ArgumentMatchers.any())).thenCallRealMethod();
		driver = new PhantomDriver();
	}
	
	@Test
	public void connect_shouldGetAConnection() throws Exception {
		final String url = URL_PREFIX + "test";
		
		driver.connect(url, mockProps);
		
		PowerMockito.verifyStatic(DriverUtils.class);
		DriverUtils.connect(url, mockProps);
	}
	
	@Test
	public void connect_shouldReturnNullForAnInvalidUrl() throws Exception {
		Assert.assertNull(driver.connect("jdbc:mysql://localhost:3306/test", null));
	}
	
	@Test
	public void connect_shouldThrowASqlExceptionIfAnErrorIsEncountered() throws Exception {
		final String url = URL_PREFIX + "test";
		final String errorMsg = "test error msg";
		PowerMockito.when(DriverUtils.connect(url, mockProps)).thenThrow(new RuntimeException(errorMsg));
		
		SQLException ex = Assert.assertThrows(SQLException.class, () -> driver.connect(url, mockProps));
		
		assertEquals(errorMsg, ex.getCause().getMessage());
	}
	
	@Test
	public void acceptsURL_shouldReturnTrueForAValidUrl() {
		Assert.assertTrue(driver.acceptsURL(URL_PREFIX + "test"));
	}
	
	@Test
	public void acceptsURL_shouldReturnNullIfUrlIsNull() {
		Assert.assertFalse(driver.acceptsURL(null));
	}
	
	@Test
	public void acceptsURL_shouldReturnNullForAnInvalidUrl() {
		Assert.assertFalse(driver.acceptsURL("jdbc:mysql://localhost:3306/test"));
	}
	
	@Test
	public void getParentLogger_shouldReturnTheJavaNativeLoggerIfItIsTheEnabled() throws Exception {
		Mockito.when(LoggerUtils.isUsingJavaLogger()).thenReturn(true);
		
		assertEquals(JavaLogger.getInstance().getNativeLogger(), driver.getParentLogger());
	}
	
	@Test
	public void getParentLogger_shouldFailIfJavaLoggerIsNotTheEnabled() {
		Assert.assertThrows(SQLFeatureNotSupportedException.class, () -> driver.getParentLogger());
	}
	
	@Test
	public void getPropertyInfo_shouldReturnAnArrayOfDriverPropertyInfo() {
		final String dbName = "test";
		final String url = URL_PREFIX + dbName;
		final String async = "true";
		final String clazz = "someClass";
		Map<DriverProperty, String> propValueMap = new HashMap<>();
		propValueMap.put(DriverProperty.TARGET_DB, dbName);
		propValueMap.put(DriverProperty.ASYNC, async);
		propValueMap.put(DriverProperty.CONNECTION_LISTENER, clazz);
		Mockito.when(DriverUtils.createDriverPropertyAndValueMap(url, mockProps)).thenReturn(propValueMap);
		
		DriverPropertyInfo[] info = driver.getPropertyInfo(url, mockProps);
		
		assertEquals(3, info.length);
		DriverPropertyInfo targetDbInfo = null;
		DriverPropertyInfo asyncInfo = null;
		DriverPropertyInfo listenerInfo = null;
		for (DriverPropertyInfo i : info) {
			if (i.name.equals(PROP_DRIVER_TARGET_DB)) {
				targetDbInfo = i;
			} else if (i.name.equals(PROP_DRIVER_ASYNC)) {
				asyncInfo = i;
			} else if (i.name.equals(PROP_DRIVER_CONN_LISTENER_CLASS)) {
				listenerInfo = i;
			}
		}
		
		assertEquals(dbName, targetDbInfo.value);
		assertEquals(async, asyncInfo.value);
		assertEquals(clazz, listenerInfo.value);
	}
	
	@Test
	public void getMajorVersion_shouldReturnTheMajor() {
		final int majorVersion = 2;
		Mockito.when(Utils.getVersion()).thenReturn(majorVersion + ".3");
		assertEquals(majorVersion, driver.getMajorVersion());
	}
	
	@Test
	public void getMinorVersion_shouldReturnTheMinorVersion() {
		final int minorVersion = 3;
		Mockito.when(Utils.getVersion()).thenReturn("2." + minorVersion);
		assertEquals(minorVersion, driver.getMinorVersion());
	}
	
}
