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

public class SystemUtils {
	
	/**
	 * Gets the value of the environment variable matching the specified name
	 * 
	 * @param name the name of environment variable
	 * @return the value
	 */
	public static String getEnv(String name) {
		return System.getenv(name);
	}
	
}
