/*
 * Copyright (C) Amiyul LLC - All Rights Reserved
 *
 * This source code is protected under international copyright law. All rights
 * reserved and protected by the copyright holder.
 * 
 * This file is confidential and only available to authorized individuals with the
 * permission of the copyright holder. If you encounter this file and do not have
 * permission, please contact the copyright holder and delete this file.
 */
package com.amiyul.phantom.db;

import static org.mockito.Mockito.when;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Collections;
import java.util.Map;
import java.util.Properties;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.amiyul.phantom.api.Status;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ FileDatabaseConfigUtils.class, DriverManager.class, FileDatabase.class })
public class FileDatabaseTest {
	
	@Mock
	private DatabaseConfig mockDbConfig;
	
	@Mock
	private DatabaseDefinition mockDbDefinition;
	
	@Mock
	private Properties mockProps;
	
	@Mock
	private Connection mockConnection;
	
	@Mock
	private Status mockStatus;
	
	private FileDatabase database = FileDatabase.getInstance();
	
	@Before
	public void setup() {
		PowerMockito.mockStatic(FileDatabaseConfigUtils.class);
		PowerMockito.mockStatic(DriverManager.class);
	}
	
	@Test
	public void getConnection_shouldGetAConnectionToTheTargetDb() throws Exception {
		final String dbName = "test";
		final String url = "testUrl";
		when(FileDatabaseConfigUtils.getConfig()).thenReturn(mockDbConfig);
		Map map = Collections.singletonMap(dbName, mockDbDefinition);
		when(mockDbConfig.getDatabaseDefinitions()).thenReturn(map);
		when(mockDbDefinition.getUrl()).thenReturn(url);
		when(mockDbDefinition.getProperties()).thenReturn(mockProps);
		when(DriverManager.getConnection(url, mockProps)).thenReturn(mockConnection);
		
		Assert.assertEquals(mockConnection, database.getConnection(dbName));
	}
	
	@Test
	public void reload_shouldReloadTheDatabase() throws Exception {
		when(FileDatabaseConfigUtils.getConfig()).thenReturn(mockDbConfig);
		
		database.reload();
		
		PowerMockito.verifyStatic(FileDatabaseConfigUtils.class);
		FileDatabaseConfigUtils.discardConfig();
	}
	
	@Test
	public void getStatus_shouldGetAConnectionToTheTargetDb() throws Exception {
		final String dbName = "test";
		when(FileDatabaseConfigUtils.getConfig()).thenReturn(mockDbConfig);
		Map map = Collections.singletonMap(dbName, mockDbDefinition);
		when(mockDbConfig.getDatabaseDefinitions()).thenReturn(map);
		when(mockDbDefinition.getStatus()).thenReturn(mockStatus);
		
		Assert.assertEquals(mockStatus, database.getStatus(dbName));
	}
	
}
