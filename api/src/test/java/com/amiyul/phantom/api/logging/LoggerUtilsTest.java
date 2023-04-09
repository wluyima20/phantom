/*
 * Add Copyright
 */
package com.amiyul.phantom.api.logging;

import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

import com.amiyul.phantom.api.ServiceLoaderUtils;
import com.amiyul.phantom.api.Utils;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ ServiceLoaderUtils.class, Utils.class, LoggerUtils.class })
public class LoggerUtilsTest {
	
	@Mock
	private LoggerProvider mockProvider;
	
	@Mock
	private DriverLogger mockLogger;
	
	@Before
	public void setup() {
		PowerMockito.mockStatic(ServiceLoaderUtils.class);
		PowerMockito.mockStatic(Utils.class);
		PowerMockito.mockStatic(LoggerUtils.class);
		Whitebox.setInternalState(LoggerUtils.class, "provider", (Object) null);
		when(mockProvider.getLogger()).thenReturn(mockLogger);
		when(LoggerUtils.getProvider()).thenCallRealMethod();
	}
	
	@Test
	public void getProvider_shouldGetTheLoggerProvider() {
		Iterator providerIterator = Arrays.asList(mockProvider).iterator();
		when(ServiceLoaderUtils.getProviders(LoggerProvider.class)).thenReturn(providerIterator);
		
		Assert.assertEquals(mockProvider, LoggerUtils.getProvider());
	}
	
	@Test
	public void getProvider_shouldGetTheCachedLoggerProvider() {
		Whitebox.setInternalState(LoggerUtils.class, "provider", mockProvider);
		
		Assert.assertEquals(mockProvider, LoggerUtils.getProvider());
	}
	
	@Test
	public void getProvider_shouldDefaultToSlf4jLoggerProviderIfTheJarsAreOnTheClasspath() throws Exception {
		Iterator providerIterator = Collections.emptyList().iterator();
		when(ServiceLoaderUtils.getProviders(LoggerProvider.class)).thenReturn(providerIterator);
		when(LoggerUtils.isSlf4jPresent()).thenCallRealMethod();
		when(Utils.loadClass(Slf4jLogger.CLASS_LOGGER_FACTORY)).thenReturn(null);
		
		Assert.assertTrue(LoggerUtils.getProvider() instanceof Slf4jLoggerProvider);
	}
	
	@Test
	public void getProvider_shouldDefaultToJavaLoggerProvider() {
		Iterator providerIterator = Collections.emptyList().iterator();
		when(ServiceLoaderUtils.getProviders(LoggerProvider.class)).thenReturn(providerIterator);
		
		Assert.assertTrue(LoggerUtils.getProvider() instanceof JavaLoggerProvider);
	}
	
	@Test
	public void debug_shouldLogADebugMessage() {
		Whitebox.setInternalState(LoggerUtils.class, "provider", mockProvider);
		final String msg = "debug msg";
		PowerMockito.spy(LoggerUtils.class);
		
		LoggerUtils.debug(msg);
		
		Mockito.verify(mockLogger).debug(msg);
	}
	
	@Test
	public void info_shouldLogAnInfoMessage() {
		Whitebox.setInternalState(LoggerUtils.class, "provider", mockProvider);
		final String msg = "info msg";
		PowerMockito.spy(LoggerUtils.class);
		
		LoggerUtils.info(msg);
		
		Mockito.verify(mockLogger).info(msg);
	}
	
	@Test
	public void warn_shouldLogAWarnMessage() {
		Whitebox.setInternalState(LoggerUtils.class, "provider", mockProvider);
		final String msg = "warn msg";
		PowerMockito.spy(LoggerUtils.class);
		
		LoggerUtils.warn(msg);
		
		Mockito.verify(mockLogger).warn(msg);
	}
	
	@Test
	public void error_shouldLogAnErrorMessage() {
		Whitebox.setInternalState(LoggerUtils.class, "provider", mockProvider);
		final String msg = "error msg";
		Throwable throwable = new Throwable();
		PowerMockito.spy(LoggerUtils.class);
		
		LoggerUtils.error(msg, throwable);
		
		Mockito.verify(mockLogger).error(msg, throwable);
	}
	
	@Test
	public void isUsingJavaLogger_shouldReturnTueIfTheProviderIsForJavaLogging() {
		Whitebox.setInternalState(LoggerUtils.class, "provider", JavaLoggerProvider.getInstance());
		when(LoggerUtils.isUsingJavaLogger()).thenCallRealMethod();
		
		Assert.assertTrue(LoggerUtils.isUsingJavaLogger());
	}
	
	@Test
	public void isUsingJavaLogger_shouldReturnTueIfTheProviderIsNotForJavaLogging() {
		Whitebox.setInternalState(LoggerUtils.class, "provider", Slf4jLoggerProvider.getInstance());
		when(LoggerUtils.isUsingJavaLogger()).thenCallRealMethod();
		
		Assert.assertFalse(LoggerUtils.isUsingJavaLogger());
	}
	
}
