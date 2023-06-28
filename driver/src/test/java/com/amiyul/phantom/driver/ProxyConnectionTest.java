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

import java.lang.reflect.Method;
import java.sql.Connection;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class ProxyConnectionTest {
	
	private ProxyConnection handler;
	
	@Before
	public void setup() {
		handler = new ProxyConnection();
	}
	
	@Test
	public void invoke_shouldFailForAnUnsupportedMethod() throws Exception {
		Method m = Connection.class.getMethod("createStatement");
		Throwable thrown = Assert.assertThrows(RuntimeException.class, () -> handler.invoke(null, m, null));
		
		Assert.assertEquals("No calls allowed on a connection returned asynchronously, instead use the "
		        + "connection sent to the registered listener",
		    thrown.getMessage());
	}
	
	@Test
	public void invoke_shouldReturnTheHashCodeOfTheHandler() throws Throwable {
		Method method = handler.getClass().getMethod("hashCode");
		
		Assert.assertEquals(handler.hashCode(), handler.invoke(null, method, null));
	}
	
	@Test
	public void invoke_shouldReturnTheStringRepresentationOfTheHandler() throws Throwable {
		Method method = handler.getClass().getMethod("toString");
		
		Assert.assertEquals(handler.toString(), handler.invoke(null, method, null));
	}
	
	@Test
	public void invoke_shouldReturnTrueForEqualsMethodIfTheObjectsAreTheSame() throws Throwable {
		Method method = handler.getClass().getMethod("equals", Object.class);
		
		Assert.assertEquals(true, handler.invoke(handler, method, new Object[] { handler }));
	}
	
	@Test
	public void invoke_shouldReturnFalseForEqualsMethodIfTheObjectsAreDifferent() throws Throwable {
		Method method = handler.getClass().getMethod("equals", Object.class);
		
		Assert.assertEquals(false, handler.invoke(handler, method, new Object[] { new ProxyConnection() }));
	}
	
}
