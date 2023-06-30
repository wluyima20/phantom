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

/**
 * Marker interface for objects that are named
 */
public interface Named {
	
	/**
	 * Gets the name
	 *
	 * @return the name
	 */
	String getName();
	
	/**
	 * Sets the name
	 *
	 * @param name the name to set
	 */
	void setName(String name);
	
}
