/*
 * Add Copyright
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
