/*
 * Copyright [yyyy] Amiyul LLC
 * All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
