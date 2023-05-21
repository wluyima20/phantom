/*
 * Add Copyright
 */
package com.amiyul.phantom.api;

import static java.time.LocalDateTime.of;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import java.io.File;
import java.sql.Connection;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Properties;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

import com.amiyul.phantom.api.config.ConfigFileParser;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ SystemUtils.class, ServiceLoaderUtils.class })
public class UtilsTest {
	
	private static final String PROP_NAME = "test_prop";
	
	private static final String TEST_VERSION = "1.2.3";
	
	@Before
	public void setup() {
		PowerMockito.mockStatic(SystemUtils.class);
		PowerMockito.mockStatic(ServiceLoaderUtils.class);
	}
	
	@After
	public void tearDown() {
		System.clearProperty(PROP_NAME);
	}
	
	@Test
	public void isBlank_shouldReturnTrueForNull() {
		Assert.assertTrue(Utils.isBlank(null));
	}
	
	@Test
	public void isBlank_shouldReturnTrueForWhiteSpaceCharacter() {
		Assert.assertTrue(Utils.isBlank(" "));
	}
	
	@Test
	public void isBlank_shouldReturnTrueForABlankString() {
		Assert.assertTrue(Utils.isBlank(""));
	}
	
	@Test
	public void isBlank_shouldReturnFalseForANonBlankString() {
		assertFalse(Utils.isBlank("a"));
	}
	
	@Test
	public void getFilePath_shouldReturnTheSystemPropertyValue() {
		final String propValue = "test_val";
		System.setProperty(PROP_NAME, propValue);
		Assert.assertEquals(propValue, Utils.getFilePath(PROP_NAME));
	}
	
	@Test
	public void getFilePath_shouldReturnTheEnvironmentPropertyIfNoSystemPropertyIsSet() {
		final String propValue = "test_val";
		when(SystemUtils.getEnv(PROP_NAME)).thenReturn(propValue);
		Assert.assertEquals(propValue, Utils.getFilePath(PROP_NAME));
	}
	
	@Test
	public void getParser_shouldReturnTheParserThatCanParseTheFile() {
		ConfigFileParser mockParser1 = Mockito.mock(ConfigFileParser.class);
		ConfigFileParser mockParser2 = Mockito.mock(ConfigFileParser.class);
		ConfigFileParser mockParser3 = Mockito.mock(ConfigFileParser.class);
		File mockFile = Mockito.mock(File.class);
		Iterator parserIterator = Arrays.asList(mockParser1, mockParser2, mockParser3).iterator();
		when(ServiceLoaderUtils.getProviders(ConfigFileParser.class)).thenReturn(parserIterator);
		when(mockParser2.canParse(mockFile)).thenReturn(true);
		
		Assert.assertEquals(mockParser2, Utils.getParser(ConfigFileParser.class, mockFile));
	}
	
	@Test
	public void getParser_shouldReturnNullIfNoParserCanParseTheFile() {
		ConfigFileParser mockParser1 = Mockito.mock(ConfigFileParser.class);
		ConfigFileParser mockParser2 = Mockito.mock(ConfigFileParser.class);
		File mockFile = Mockito.mock(File.class);
		Iterator parsers = Arrays.asList(mockParser1, mockParser2).iterator();
		when(ServiceLoaderUtils.getProviders(ConfigFileParser.class)).thenReturn(parsers);
		
		Assert.assertNull(Utils.getParser(ConfigFileParser.class, mockFile));
	}
	
	@Test
	public void getVersion_shouldReturnTheBuildVersion() {
		Properties props = new Properties();
		props.setProperty(Utils.BUILD_PROP_VERSION, TEST_VERSION);
		Whitebox.setInternalState(Utils.class, "PROPERTIES", props);
		assertEquals(TEST_VERSION, Utils.getVersion());
	}
	
	@Test
	public void parseDateString_shouldParseTheSpecifiedDate() {
		LocalDateTime expected = ZonedDateTime.of(2023, 4, 27, 12, 5, 5, 0, ZoneId.of("UTC"))
		        .withZoneSameInstant(ZoneId.systemDefault()).toLocalDateTime();
		Assert.assertEquals(expected, Utils.parseDateString("2023-04-27T15:05:05+03:00"));
	}
	
	@Test
	public void isDateAfter_shouldReturnFalseForNullDate() {
		assertFalse(Utils.isDateAfter(null, of(2023, 4, 27, 12, 5, 5)));
	}
	
	@Test
	public void isDateAfter_shouldReturnFalseIfDateIsBeforeAsOfDate() {
		assertFalse(Utils.isDateAfter(of(2023, 4, 27, 12, 5, 5, 0), of(2023, 4, 27, 12, 5, 5, 1)));
	}
	
	@Test
	public void isDateAfter_shouldReturnFalseIfDateIsEqualsToAsOfDate() {
		LocalDateTime dateTime = of(2023, 4, 27, 12, 5, 5, 0);
		assertFalse(Utils.isDateAfter(dateTime, dateTime));
	}
	
	@Test
	public void isDateAfter_shouldReturnTrueIfDateIsAfterAsOfDate() {
		assertTrue(Utils.isDateAfter(of(2023, 4, 27, 12, 5, 5, 1), of(2023, 4, 27, 12, 5, 5, 0)));
	}
	
	@Test
	public void isDateAfter_shouldFailIfAsOfDateIsNull() {
		Exception thrown = Assert.assertThrows(RuntimeException.class, () -> {
			Utils.isDateAfter(of(2023, 4, 27, 12, 5, 5, 1), null);
		});
		assertEquals("other date can't be null", thrown.getMessage());
	}
	
	@Test
	public void isDateAfter_shouldReturnTrueForHashCodeMethod() throws Exception {
		assertTrue(Utils.isHashCodeMethod(new DemoClass().getClass().getMethod("hashCode")));
	}
	
	@Test
	public void isDateAfter_shouldReturnFalseForNonHashCodeMethod() throws Exception {
		assertFalse(Utils.isHashCodeMethod(new DemoClass().getClass().getMethod("wait")));
	}
	
	@Test
	public void isDateAfter_shouldReturnTrueForToStringMethod() throws Exception {
		assertTrue(Utils.isToStringMethod(new DemoClass().getClass().getMethod("toString")));
	}
	
	@Test
	public void isDateAfter_shouldReturnFalseForNonToStringMethod() throws Exception {
		assertFalse(Utils.isToStringMethod(new DemoClass().getClass().getMethod("wait")));
	}
	
	@Test
	public void isDateAfter_shouldReturnTrueForEqualsMethod() throws Exception {
		assertTrue(Utils.isEqualsMethod(new DemoClass().getClass().getMethod("equals", Object.class)));
	}
	
	@Test
	public void isDateAfter_shouldReturnFalseForNonEqualsMethod() throws Exception {
		assertFalse(Utils.isEqualsMethod(new Object().getClass().getMethod("wait")));
		assertFalse(Utils.isEqualsMethod(new DemoClass().getClass().getMethod("equals", Connection.class)));
		assertFalse(Utils.isEqualsMethod(new DemoClass().getClass().getMethod("equals", Object.class, Connection.class)));
	}
	
	@Test
	public void getDurationBetween_shouldReturnTheDurationInterval() {
		LocalDateTime start = of(2023, 4, 27, 12, 5, 5);
		LocalDateTime end = of(2023, 4, 27, 12, 6, 6);
		Assert.assertEquals(61, Utils.getDurationBetween(start, end).getSeconds());
	}
	
	private static class DemoClass {
		
		public boolean equals(Connection c) {
			return false;
		}
		
		public boolean equals(Object o, Connection c) {
			return false;
		}
	}
	
}
