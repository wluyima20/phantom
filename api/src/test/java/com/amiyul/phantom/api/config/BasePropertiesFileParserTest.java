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
package com.amiyul.phantom.api.config;

import java.io.FileInputStream;
import java.util.Properties;

import org.junit.Assert;
import org.junit.Test;

public class BasePropertiesFileParserTest {
	
	private static class MockParser extends BasePropertiesFileParser {
		
		private Properties properties;
		
		@Override
		public Object createInstance(Properties properties) throws Exception {
			this.properties = properties;
			return null;
		}
		
	}
	
	private MockParser parser = new MockParser();
	
	@Test
	public void getParser_shouldReturnTheAppropriateParser() throws Exception {
		FileInputStream i = new FileInputStream((getClass().getClassLoader().getResource("test.properties").getFile()));
		
		parser.parse(i);
		
		Properties expected = new Properties();
		expected.put("key1", "value1");
		expected.put("key2", "value2");
		Assert.assertEquals(expected, parser.properties);
	}
	
}
