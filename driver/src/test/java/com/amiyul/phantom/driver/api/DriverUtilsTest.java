/*
 * Add Copyright
 */
package com.amiyul.phantom.driver.api;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.File;
import java.util.Arrays;
import java.util.Iterator;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

import com.amiyul.phantom.api.Database;
import com.amiyul.phantom.api.DatabaseProvider;
import com.amiyul.phantom.api.ServiceLoaderUtils;
import com.amiyul.phantom.api.config.ConfigFileParser;
import com.amiyul.phantom.driver.api.config.DriverConfig;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ DriverConfigUtils.class, ServiceLoaderUtils.class })
public class DriverUtilsTest {
	
	@Mock
	private ConfigFileParser mockParser;
	
	@Mock
	private Database mockDatabase;
	
	@Mock
	private DatabaseProvider mockDatabaseProvider;
	
	@Mock
	private File mockFile;
	
	@Before
	public void setup() {
		PowerMockito.mockStatic(ServiceLoaderUtils.class);
		Whitebox.setInternalState(DriverConfigUtils.class, "parser", (Object) null);
	}
	
	@Test
	public void getParser_shouldReturnTheAppropriateParser() {
		ConfigFileParser mockParser1 = mock(ConfigFileParser.class);
		ConfigFileParser mockParser2 = mock(ConfigFileParser.class);
		ConfigFileParser mockParser3 = mock(ConfigFileParser.class);
		when(mockParser2.canParse(mockFile)).thenReturn(true);
		Iterator iterator = Arrays.asList(mockParser1, mockParser2, mockParser3).iterator();
		when(ServiceLoaderUtils.getProviders(ConfigFileParser.class)).thenReturn(iterator);
		
		assertEquals(mockParser2, DriverConfigUtils.getParser(mockFile));
	}
	
	@Test
	public void getParser_shouldReturnTheCachedParser() {
		ConfigFileParser mockParser = mock(ConfigFileParser.class);
		Whitebox.setInternalState(DriverConfigUtils.class, "parser", mockParser);
		
		assertEquals(mockParser, DriverConfigUtils.getParser(mockFile));
		
		PowerMockito.verifyZeroInteractions(ServiceLoaderUtils.class);
	}
	
	@Test
	public void getCnfig() throws Exception {
		//System.setProperty(PROP_CONFIG_LOCATION, "/some/file");
		
		DriverConfig config = DriverConfigUtils.getConfig();
		
		Assert.assertEquals(mockDatabase, config.getDatabase());
	}
	
}
