/*
 * Add Copyright
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
