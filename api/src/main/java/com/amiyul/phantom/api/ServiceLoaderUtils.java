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
