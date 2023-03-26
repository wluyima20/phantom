/*
 * Add Copyright
 */
package com.amiyul.phantom.api;

import java.util.Iterator;
import java.util.ServiceLoader;

public class ServiceLoaderUtils {
	
	/**
	 * Looks up the registered providers for the specified service type
	 * 
	 * @param serviceClass service class to match
	 * @return Iterator of providers
	 */
	public static <T> Iterator<T> getProviders(Class<T> serviceClass) {
		return ServiceLoader.load(serviceClass).iterator();
	}
	
}
