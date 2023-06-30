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
 * Holds contextual data about a single request sent by the client to the database
 */
public interface RequestContext {
	
	/**
	 * Gets request object
	 * 
	 * @return request
	 */
	Request getRequest();
	
	/**
	 * Reads the result from the response
	 * 
	 * @return result
	 */
	<T> T readResult();
	
	/**
	 * Writes the result to the response
	 * 
	 * @param result to write
	 */
	void writeResult(Object result);
	
}
