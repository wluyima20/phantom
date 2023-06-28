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

import java.io.File;
import java.io.FileInputStream;

import org.junit.Assert;
import org.junit.Test;

public class BaseExtensionConfigFileParserTest {
	
	private static final String EXT = "properties";
	
	private static class MockParser extends BaseExtensionConfigFileParser {
		
		@Override
		public String getExtension(File configFile) {
			return EXT;
		}
		
		@Override
		public Object parse(FileInputStream configInputStream) throws Exception {
			return null;
		}
		
	}
	
	private MockParser parser = new MockParser();
	
	@Test
	public void canParse_shouldReturnFalseIfTheFileExtensionDoesNotMatch() {
		Assert.assertFalse(parser.canParse(new File("test.txt")));
	}
	
	@Test
	public void canParse_shouldReturnTrueIfTheFileExtensionMatches() {
		Assert.assertTrue(parser.canParse(new File("test." + EXT)));
	}
	
}
