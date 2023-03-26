/*
 * Add Copyright
 */
package com.amiyul.phantom.api.config;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.File;
import java.util.Arrays;
import java.util.Iterator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

import com.amiyul.phantom.api.ServiceLoaderUtils;

@RunWith(PowerMockRunner.class)
@PrepareForTest(ServiceLoaderUtils.class)
public class ConfigUtilsTest {
	
	@Mock
	private File mockFile;
	
	@Before
	public void setup() {
		PowerMockito.mockStatic(ServiceLoaderUtils.class);
	}
	
	@Test
	public void createParser_shouldReturnTheAppropriateParser() {
		ConfigFileParser mockParser1 = mock(ConfigFileParser.class);
		ConfigFileParser mockParser2 = mock(ConfigFileParser.class);
		ConfigFileParser mockParser3 = mock(ConfigFileParser.class);
		when(mockParser2.canParse(mockFile)).thenReturn(true);
		Iterator iterator = Arrays.asList(mockParser1, mockParser2, mockParser3).iterator();
		when(ServiceLoaderUtils.getProviders(ConfigFileParser.class)).thenReturn(iterator);
		
		assertEquals(mockParser2, ConfigUtils.createParser(mockFile));
	}
	
	@Test
	public void createParser_shouldReturnTheAppropriateParserFromTheCache() {
		ConfigFileParser mockParser1 = mock(ConfigFileParser.class);
		ConfigFileParser mockParser2 = mock(ConfigFileParser.class);
		when(mockParser2.canParse(mockFile)).thenReturn(true);
		Whitebox.setInternalState(ConfigUtils.class, "parsers", Arrays.asList(mockParser1, mockParser2));
		
		assertEquals(mockParser2, ConfigUtils.createParser(mockFile));
		
		PowerMockito.verifyZeroInteractions(ServiceLoaderUtils.class);
	}
	
}
