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

import com.amiyul.phantom.api.PhantomProtocol.Command;

/**
 * Simulation of a request sent by the database client to the {@link Database} server
 */
public interface Request {
	
	/**
	 * Gets the command to be executed by the database
	 * 
	 * @return the command
	 */
	Command getCommand();
	
	/**
	 * Gets the request context object associated to this request
	 * 
	 * @return request context
	 */
	RequestContext getContext();
	
}
