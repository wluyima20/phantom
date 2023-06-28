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

import static com.amiyul.phantom.driver.DriverConstants.PROP_DRIVER_ASYNC;
import static com.amiyul.phantom.driver.DriverConstants.PROP_DRIVER_CONN_LISTENER_CLASS;
import static com.amiyul.phantom.driver.DriverConstants.PROP_DRIVER_TARGET_DB;
import static com.amiyul.phantom.driver.DriverProperty.ASYNC;
import static com.amiyul.phantom.driver.DriverProperty.CONNECTION_LISTENER;
import static com.amiyul.phantom.driver.DriverProperty.TARGET_DB;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.sql.DriverPropertyInfo;
import java.util.Map;
import java.util.Properties;

import org.junit.Assert;
import org.junit.Test;

public class DriverPropertyTest {
	
	@Test
	public void toDriverPropertyInfo_shouldReturnTheDriverPropertyInfoObject() {
		DriverPropertyInfo async = ASYNC.toDriverPropertyInfo();
		
		assertEquals(ASYNC.getName(), async.name);
		assertEquals(ASYNC.getDescription(), async.description);
		assertFalse(async.required);
		Assert.assertArrayEquals(ASYNC.getChoices(), async.choices);
		assertNull(async.value);
		
		DriverPropertyInfo targetDb = TARGET_DB.toDriverPropertyInfo();
		assertTrue(targetDb.required);
		assertNull(targetDb.choices);
	}
	
	@Test
	public void toPropertyValueMap_shouldReturnAMappingOfDriverPropertiesAndValues() {
		final String async = "true";
		final String clazz = "someClass";
		Properties props = new Properties();
		props.setProperty(PROP_DRIVER_TARGET_DB, "test");
		props.setProperty(PROP_DRIVER_ASYNC, async);
		props.setProperty(PROP_DRIVER_CONN_LISTENER_CLASS, clazz);
		
		Map<DriverProperty, String> map = DriverProperty.toPropertyValueMap(props);
		
		assertEquals(2, map.size());
		assertEquals(async, map.get(ASYNC));
		assertEquals(clazz, map.get(CONNECTION_LISTENER));
	}
	
	@Test
	public void toPropertyValueMap_shouldReturnAnEmptyArrayIfPropsIsNull() {
		assertTrue(DriverProperty.toPropertyValueMap(null).isEmpty());
	}
	
	@Test
	public void findByName_shouldReturnTheMatchingDriverPropertyEnum() {
		assertEquals(ASYNC, DriverProperty.findByName(PROP_DRIVER_ASYNC));
	}
	
	@Test
	public void findByName_shouldFailForAnInvalidProperty() {
		final String propName = "testProp";
		Throwable thrown = Assert.assertThrows(RuntimeException.class, () -> DriverProperty.findByName(propName));
		
		assertEquals("Found an invalid driver property named: " + propName, thrown.getMessage());
	}
	
}
