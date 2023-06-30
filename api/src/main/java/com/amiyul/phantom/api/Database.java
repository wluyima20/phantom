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

import java.sql.SQLException;

/**
 * Database facade that processes client requests, it's highly recommended to extend
 * {@link com.amiyul.phantom.api.BaseDatabase} instead of directly implementing this interface for
 * better compatibility in case the interface is changed.
 */
public interface Database {
	
	/**
	 * Processes a request from a client
	 *
	 * @param context {@link RequestContext} object
	 * @throws SQLException
	 */
	void process(RequestContext context) throws SQLException;
	
}
