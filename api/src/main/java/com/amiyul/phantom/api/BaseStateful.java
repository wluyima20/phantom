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
 * Base class for {@link Stateful} implementations
 */
public abstract class BaseStateful implements Stateful {
	
	private Status status;
	
	public BaseStateful(Status status) {
		this.status = status;
	}
	
	@Override
	public Status getStatus() {
		return status;
	}
	
}
