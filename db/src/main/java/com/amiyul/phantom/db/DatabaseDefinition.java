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
package com.amiyul.phantom.db;

import java.util.Properties;

import com.amiyul.phantom.api.BaseStateful;
import com.amiyul.phantom.api.Named;
import com.amiyul.phantom.api.Status;

import lombok.Getter;

/**
 * Encapsulates information for a single target database instance
 */
public class DatabaseDefinition extends BaseStateful implements Named {
	
	private String name;
	
	@Getter
	private String url;
	
	@Getter
	private Properties properties;
	
	public DatabaseDefinition(String name, String url, Properties properties, Status status) {
		super(status);
		this.name = name;
		this.url = url;
		this.properties = properties;
	}
	
	/**
	 * @see Named#getName()
	 */
	@Override
	public String getName() {
		return name;
	}
	
	/**
	 * @see Named#setName(String)
	 */
	@Override
	public void setName(String name) {
		this.name = name;
	}
	
}
