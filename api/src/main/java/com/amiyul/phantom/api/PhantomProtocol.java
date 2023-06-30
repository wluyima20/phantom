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
 * Contains protocol details used for communication between a phantom driver client and the database
 * server
 */
public final class PhantomProtocol {
	
	/**
	 * Commands that a client send to the database for execution
	 */
	public enum Command {
		CONNECT, RELOAD, STATUS
	}
	
}
