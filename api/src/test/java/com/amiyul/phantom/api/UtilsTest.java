/*
 * Add Copyright
 */
package com.amiyul.phantom.api;

import static org.mockito.Mockito.when;

import java.io.File;
import java.util.Arrays;
import java.util.Iterator;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.amiyul.phantom.api.config.ConfigFileParser;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ SystemUtils.class, ServiceLoaderUtils.class })
public class UtilsTest {
	
	private static final String PROP_NAME = "test_prop";
	
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
		Assert.assertFalse(Utils.isBlank("a"));
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
	
}
