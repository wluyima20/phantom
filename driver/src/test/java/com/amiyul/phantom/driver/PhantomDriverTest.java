/*
 * Add Copyright
 */
package com.amiyul.phantom.driver;

import static com.amiyul.phantom.driver.PhantomDriver.URL_PREFIX;
import static org.junit.Assert.assertEquals;

import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.amiyul.phantom.api.logging.JavaLogger;
import com.amiyul.phantom.api.logging.LoggerUtils;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ DriverUtils.class, LoggerUtils.class })
public class PhantomDriverTest {
	
	private PhantomDriver driver;
	
	@Before
	public void setup() {
		PowerMockito.mockStatic(DriverUtils.class);
		PowerMockito.mockStatic(LoggerUtils.class);
		driver = new PhantomDriver();
	}
	
	@Test
	public void connect_shouldGetAConnection() throws Exception {
		final String dbName = "test";
		
		driver.connect(URL_PREFIX + dbName, null);
		
		PowerMockito.verifyStatic(DriverUtils.class);
		DriverUtils.connect(dbName);
	}
	
	@Test
	public void connect_shouldReturnNullForAnInvalidUrl() throws Exception {
		Assert.assertNull(driver.connect("jdbc:mysql://localhost:3306/test", null));
	}
	
	@Test
	public void connect_shouldThrowASqlExceptionIfAnErrorIsEncountered() throws Exception {
		final String dbName = "test";
		final String errorMsg = "test error msg";
		PowerMockito.when(DriverUtils.connect(dbName)).thenThrow(new RuntimeException(errorMsg));
		
		SQLException ex = Assert.assertThrows(SQLException.class, () -> driver.connect(URL_PREFIX + dbName, null));
		
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
	public void getPropertyInfo_shouldReturnAnEMortyArray() {
		assertEquals(0, driver.getPropertyInfo(null, null).length);
	}
	
}
