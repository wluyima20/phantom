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
package com.amiyul.phantom.driver;

import java.io.File;

import com.amiyul.phantom.api.BaseStateful;
import com.amiyul.phantom.api.Database;
import com.amiyul.phantom.api.Status;

import lombok.Getter;

/**
 * Default {@link DriverConfig} implementation
 */
class DefaultDriverConfig extends BaseStateful implements DriverConfig {
	
	@Getter
	private File licenseFile;
	
	@Getter
	private Database database;
	
	DefaultDriverConfig(File licenseFile, Database database, Status status) {
		super(status);
		this.licenseFile = licenseFile;
		this.database = database;
	}
	
}
