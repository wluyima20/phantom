/*
 * Add Copyright
 */
package com.amiyul.phantom.db;

import static org.mockito.Mockito.when;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.amiyul.phantom.api.ConnectionRequest;
import com.amiyul.phantom.api.RequestContext;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ FileDatabaseConfigUtils.class, DriverManager.class, FileDatabaseUtils.class })
public class FileDatabaseUtilsTest {
	
	@Before
	public void setup() {
		PowerMockito.mockStatic(FileDatabaseConfigUtils.class);
		PowerMockito.mockStatic(DriverManager.class);
	}
	
	@Test
	public void getConnection_shouldObtainTheConnectionFromTheAppropriateTargetDbAndSetItOnTheContext() throws Exception {
		final String dbName1 = "db1";
		final String dbUrl1 = "db-url-1";
		Properties mockDbProps = Mockito.mock(Properties.class);
		DatabaseDefinition dbDef1 = new DatabaseDefinition(dbName1, dbUrl1, mockDbProps);
		DatabaseDefinition dbDef2 = new DatabaseDefinition("db2", null, null);
		Map<String, DatabaseDefinition> nameDbDefMap = new HashMap();
		nameDbDefMap.put(dbName1, dbDef1);
		nameDbDefMap.put("db2", dbDef2);
		DatabaseConfig mockConfig = Mockito.mock(DatabaseConfig.class);
		when(FileDatabaseConfigUtils.getConfig()).thenReturn(mockConfig);
		when(mockConfig.getDatabaseDefinitions()).thenReturn(nameDbDefMap);
		Connection mockConnection = Mockito.mock(Connection.class);
		RequestContext mockContext = Mockito.mock(RequestContext.class);
		ConnectionRequest request = new ConnectionRequest(dbName1, mockContext);
		when(DriverManager.getConnection(dbUrl1, mockDbProps)).thenReturn(mockConnection);
		
		FileDatabaseUtils.getConnection(request);
		
		Mockito.verify(mockContext).writeResult(mockConnection);
	}
	
	@Test
	public void getConnection_shouldFailIfNoTargetDbIsFoundMatchingTheDbName() {
		DatabaseDefinition dbDef = new DatabaseDefinition("db2", null, null);
		Map<String, DatabaseDefinition> nameDbDefMap = Collections.singletonMap("db", dbDef);
		DatabaseConfig mockConfig = Mockito.mock(DatabaseConfig.class);
		when(FileDatabaseConfigUtils.getConfig()).thenReturn(mockConfig);
		when(mockConfig.getDatabaseDefinitions()).thenReturn(nameDbDefMap);
		final String dbName = "some-db";
		
		Exception thrown = Assert.assertThrows(SQLException.class,
		    () -> FileDatabaseUtils.getConnection(new ConnectionRequest(dbName, null)));
		
		Assert.assertEquals("No target database found matching the name: " + dbName, thrown.getMessage());
	}
	
}
