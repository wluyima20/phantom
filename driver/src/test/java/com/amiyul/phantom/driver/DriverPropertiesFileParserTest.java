/*
 * Add Copyright
 */
package com.amiyul.phantom.driver;

import static com.amiyul.phantom.driver.DriverConstants.PROP_DB_PROVIDER_CLASS;
import static com.amiyul.phantom.driver.DriverConstants.PROP_DB_UNAVAILABLE_UNTIL;

import java.util.Properties;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest(DriverConfigUtils.class)
public class DriverPropertiesFileParserTest {
	
	private DriverPropertiesFileParser parser = new DriverPropertiesFileParser();
	
	@Test
	public void createInstance_shouldCreateTheDriverConfigMetadata() throws Exception {
		PowerMockito.mockStatic(DriverConfigUtils.class);
		final String classname = "someClass";
		final String date = "2023-05-23T22:25:10+03:00";
		Properties props = new Properties();
		props.put(PROP_DB_PROVIDER_CLASS, classname);
		props.put(PROP_DB_UNAVAILABLE_UNTIL, date);
		DriverConfigMetadata mockMetadata = Mockito.mock(DriverConfigMetadata.class);
		Mockito.when(DriverConfigUtils.createMetadata(classname, date)).thenReturn(mockMetadata);
		
		Assert.assertEquals(mockMetadata, parser.createInstance(props));
	}
	
}
