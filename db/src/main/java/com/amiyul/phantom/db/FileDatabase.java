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

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import com.amiyul.phantom.api.BaseDatabase;
import com.amiyul.phantom.api.Database;
import com.amiyul.phantom.api.Status;
import com.amiyul.phantom.api.logging.LoggerUtils;

/**
 * Base class for {@link Database} implementations where the target database definitions are
 * configured in a file
 */
public class FileDatabase extends BaseDatabase {
	
	private FileDatabase() {
	}
	
	public static FileDatabase getInstance() {
		return FileDatabaseHolder.INSTANCE;
	}
	
	@Override
	public Connection getConnection(String targetDatabaseName) throws SQLException {
		DatabaseDefinition dbDef = getTargetDb(targetDatabaseName);
		
		LoggerUtils.debug("Connecting to target database at -> " + dbDef.getUrl());
		
		return DriverManager.getConnection(dbDef.getUrl(), dbDef.getProperties());
	}
	
	@Override
	public void reload() throws SQLException {
		FileDatabaseConfigUtils.discardConfig();
	}
	
	@Override
	public Status getStatus(String targetDatabaseName) throws SQLException {
		return getTargetDb(targetDatabaseName).getStatus();
	}
	
	private DatabaseDefinition getTargetDb(String targetDatabaseName) throws SQLException {
		DatabaseDefinition def = FileDatabaseConfigUtils.getConfig().getDatabaseDefinitions().get(targetDatabaseName);
		if (def == null) {
			throw new SQLException("No target database found matching the name: " + targetDatabaseName);
		}
		
		return def;
	}
	
	private static class FileDatabaseHolder {
		
		private static final FileDatabase INSTANCE = new FileDatabase();
		
	}
	
}
