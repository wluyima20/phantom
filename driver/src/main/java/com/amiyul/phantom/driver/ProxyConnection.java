/*
 * Add Copyright
 */
package com.amiyul.phantom.driver;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.sql.Connection;

import com.amiyul.phantom.api.Utils;

/**
 * {@link InvocationHandler} for a proxy connection returned by the driver which always, it fails
 * for any calls on methods defined on the {@link Connection} interface.
 */
public final class ProxyConnection implements InvocationHandler {
	
	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		if (Connection.class.isAssignableFrom(method.getDeclaringClass())) {
			throw new RuntimeException("No calls allowed on a connection returned asynchronously, instead use the "
			        + "connection sent to the registered listener");
		}
		
		if (Utils.isHashCodeMethod(method)) {
			return hashCode();
		} else if (Utils.isToStringMethod(method)) {
			return toString();
		} else if (Utils.isEqualsMethod(method)) {
			return proxy == args[0];
		}
		
		return method.invoke(proxy, args);
	}
	
}
