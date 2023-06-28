/*
 * Copyright [yyyy] Amiyul LLC
 * All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
