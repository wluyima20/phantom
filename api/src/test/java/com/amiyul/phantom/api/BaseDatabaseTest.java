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
package com.amiyul.phantom.api;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.modules.junit4.PowerMockRunner;

import com.amiyul.phantom.api.PhantomProtocol.Command;

@RunWith(PowerMockRunner.class)
public class BaseDatabaseTest {
	
	private static class MockDatabase extends BaseDatabase {
		
		protected String connectRequestTargetDb;
		
		protected String statusRequestTargetDb;
		
		private boolean reloaded = false;
		
		private Connection connection;
		
		private Status status;
		
		public MockDatabase(Connection connection, Status status) {
			this.connection = connection;
			this.status = status;
		}
		
		@Override
		public Connection getConnection(String targetDatabaseName) throws SQLException {
			this.connectRequestTargetDb = targetDatabaseName;
			return connection;
		}
		
		@Override
		public void reload() {
			reloaded = true;
		}
		
		@Override
		public Status getStatus(String targetDatabaseName) throws SQLException {
			this.statusRequestTargetDb = targetDatabaseName;
			return status;
		}
		
	}
	
	@Mock
	private RequestContext mockContext;
	
	@Mock
	private Connection mockConnection;
	
	private Request request;
	
	private MockDatabase database;
	
	@Test
	public void process_shouldProcessConnectRequest() throws Exception {
		final String dbName = "test";
		request = new ConnectionRequest(dbName, mockContext);
		database = new MockDatabase(mockConnection, null);
		Mockito.when(mockContext.getRequest()).thenReturn(request);
		
		database.process(mockContext);
		
		Mockito.verify(mockContext).writeResult(mockConnection);
		Assert.assertEquals(dbName, database.connectRequestTargetDb);
	}
	
	@Test
	public void process_shouldProcessReloadRequest() throws Exception {
		request = new DefaultRequest(Command.RELOAD, mockContext);
		database = new MockDatabase(null, null);
		Mockito.when(mockContext.getRequest()).thenReturn(request);
		
		database.process(mockContext);
		
		Assert.assertTrue(database.reloaded);
	}
	
	@Test
	public void process_shouldProcessStatusRequest() throws Exception {
		final String dbName = "test";
		request = new StatusRequest(dbName, mockContext);
		Status status = new Status(null);
		database = new MockDatabase(null, status);
		Mockito.when(mockContext.getRequest()).thenReturn(request);
		
		database.process(mockContext);
		
		Mockito.verify(mockContext).writeResult(status);
		Assert.assertEquals(dbName, database.statusRequestTargetDb);
	}
	
}
