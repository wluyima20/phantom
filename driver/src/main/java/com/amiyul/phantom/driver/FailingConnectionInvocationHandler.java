/*
 * Add Copyright
 */
package com.amiyul.phantom.driver;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.sql.Connection;

/**
 * InvocationHandler for proxy connections returned by the driver which always fails for any calls
 * on methods defined in the {@link Connection} interface.
 */
public class FailingConnectionInvocationHandler implements InvocationHandler {
	
	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		if (Connection.class.isAssignableFrom(method.getDeclaringClass())) {
			throw new RuntimeException("No calls allowed on a connection returned asynchronously, instead use the "
			        + "connection sent to the registered listener");
		}
		
		return method.invoke(proxy, args);
	}
	
}
