/*
 * Add Copyright
 */
package com.amiyul.phantom.driver.config;

import static com.amiyul.phantom.driver.config.DriverConfigMetadata.PROP_DB_PROVIDER_CLASS;

import java.util.Properties;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.amiyul.phantom.driver.DriverConfigUtils;

@RunWith(PowerMockRunner.class)
@PrepareForTest(DriverConfigUtils.class)
public class DriverPropertiesFileParserTest {
	
	private DriverPropertiesFileParser parser = new DriverPropertiesFileParser();
	
	@Test
	public void createInstance_shouldCreateTheDriverConfigMetadata() throws Exception {
		PowerMockito.mockStatic(DriverConfigUtils.class);
		final String classname = "someClass";
		Properties props = new Properties();
		props.put(PROP_DB_PROVIDER_CLASS, classname);
		DriverConfigMetadata mockMetadata = Mockito.mock(DriverConfigMetadata.class);
		Mockito.when(DriverConfigUtils.createMetadata(classname)).thenReturn(mockMetadata);
		
		Assert.assertEquals(mockMetadata, parser.createInstance(props));
	}
	
}
