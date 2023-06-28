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

import java.sql.Connection;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest(DefaultClient.class)
public class DelayedConnectTaskTest {
	
	@Mock
	private DefaultClient mockClient;
	
	@Mock
	private Connection mockConnection;
	
	@Test
	public void shouldSetTheTaskNameFromTheConstructor() {
		final String dbName = "test";
		DelayedConnectTask task = new DelayedConnectTask(new ConnectionRequestData(dbName, false, null));
		Assert.assertEquals(DelayedConnectTask.NAME_PREFIX + dbName, task.getName());
	}
	
	@Test
	public void doCall_should() throws Exception {
		PowerMockito.mockStatic(DefaultClient.class);
		Mockito.when(DefaultClient.getInstance()).thenReturn(mockClient);
		ConnectionRequestData requestData = new ConnectionRequestData(null, false, null);
		Mockito.when(mockClient.doConnect(requestData)).thenReturn(mockConnection);
		
		Assert.assertEquals(mockConnection, new DelayedConnectTask(requestData).doCall());
	}
	
}
