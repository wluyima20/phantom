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
