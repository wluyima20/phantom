/*
 * Add Copyright
 */
package com.amiyul.phantom.db.api.config;

import static com.amiyul.phantom.db.api.config.DatabasePropertiesFileParser.PROP_PROPS;
import static com.amiyul.phantom.db.api.config.DatabasePropertiesFileParser.PROP_URL;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.IOException;
import java.util.Properties;

import org.junit.Test;

public class DatabasePropertiesFileParserTest {
	
	private static final String PROP_USER = "user";
	
	private static final String PROP_PASSWORD = "password";
	
	private static final String DB_PROD = "mysql-prod";
	
	private static final String DB_RESEARCH = "mysql-research";
	
	private static final String DB_URL_PROD = "jdbc:mysql://localhost:3306/prod";
	
	private static final String DB_USER_PROD = "prod-user";
	
	private DatabasePropertiesFileParser parser = new DatabasePropertiesFileParser();
	
	private String generatePropertiesPropName(String dbName, String propName) {
		return generateDbPropName(dbName, PROP_PROPS + "." + propName);
	}
	
	private String generateDbPropName(String dbName, String propName) {
		return dbName + "." + propName;
	}
	
	private Properties loadTestProps() throws IOException {
		Properties props = new Properties();
		props.load(getClass().getClassLoader().getResourceAsStream("test_db.properties"));
		return props;
	}
	
	@Test
	public void createInstance_shouldCreateADbConfigMetadataInstance() throws Exception {
		Properties props = loadTestProps();
		
		DatabaseConfigMetadata metadata = parser.createInstance(props);
		
		assertEquals(2, metadata.getDatabaseMetadata().size());
		assertEquals("mysql-prod", metadata.getDatabaseMetadata().get(0).getName());
		assertEquals(DB_URL_PROD, metadata.getDatabaseMetadata().get(0).getUrl());
		assertEquals(DB_USER_PROD, metadata.getDatabaseMetadata().get(0).getProperties().getProperty(PROP_USER));
		assertEquals("prod-pass", metadata.getDatabaseMetadata().get(0).getProperties().getProperty(PROP_PASSWORD));
		assertEquals("mysql-research", metadata.getDatabaseMetadata().get(1).getName());
		assertEquals("jdbc:mysql://localhost:3307/research", metadata.getDatabaseMetadata().get(1).getUrl());
		assertEquals("research-user", metadata.getDatabaseMetadata().get(1).getProperties().getProperty(PROP_USER));
		assertEquals("research-pass", metadata.getDatabaseMetadata().get(1).getProperties().getProperty(PROP_PASSWORD));
	}
	
	@Test
	public void createInstance_shouldExcludeBlankValuesAndTrimValues() throws Exception {
		Properties props = loadTestProps();
		props.put(DatabasePropertiesFileParser.PROP_DATABASES, " " + DB_PROD + " , " + DB_RESEARCH + " ");
		props.put(generateDbPropName(DB_PROD, PROP_URL), " " + DB_URL_PROD + " ");
		props.put(generatePropertiesPropName(DB_PROD, PROP_USER), " " + DB_USER_PROD + " ");
		props.put(generateDbPropName(DB_RESEARCH, PROP_URL), " ");
		props.put(generatePropertiesPropName(DB_RESEARCH, PROP_USER), " ");
		props.remove(generatePropertiesPropName(DB_RESEARCH, PROP_PASSWORD));
		
		DatabaseConfigMetadata metadata = parser.createInstance(props);
		
		assertEquals(2, metadata.getDatabaseMetadata().size());
		assertEquals("mysql-prod", metadata.getDatabaseMetadata().get(0).getName());
		assertEquals(DB_URL_PROD, metadata.getDatabaseMetadata().get(0).getUrl());
		assertEquals(DB_USER_PROD, metadata.getDatabaseMetadata().get(0).getProperties().getProperty(PROP_USER));
		assertEquals("prod-pass", metadata.getDatabaseMetadata().get(0).getProperties().getProperty(PROP_PASSWORD));
		assertEquals("mysql-research", metadata.getDatabaseMetadata().get(1).getName());
		assertNull(metadata.getDatabaseMetadata().get(1).getUrl());
		assertNull(metadata.getDatabaseMetadata().get(1).getProperties().getProperty(PROP_USER));
		assertNull(metadata.getDatabaseMetadata().get(1).getProperties().getProperty(PROP_PASSWORD));
	}
	
}
