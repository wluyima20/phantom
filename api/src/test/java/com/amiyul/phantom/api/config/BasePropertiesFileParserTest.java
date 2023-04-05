/*
 * Add Copyright
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
