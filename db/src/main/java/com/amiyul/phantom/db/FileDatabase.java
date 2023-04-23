/*
 * Add Copyright
 */
package com.amiyul.phantom.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import com.amiyul.phantom.api.BaseDatabase;
import com.amiyul.phantom.api.Database;
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
		DatabaseDefinition ref = FileDatabaseConfigUtils.getConfig().getDatabaseDefinitions().get(targetDatabaseName);
		if (ref == null) {
			throw new SQLException("No target database found matching the name: " + targetDatabaseName);
		}
		
		LoggerUtils.debug("Obtaining connection to target database at -> " + ref.getUrl());
		
		return DriverManager.getConnection(ref.getUrl(), ref.getProperties());
	}
	
	@Override
	public void reload() throws SQLException {
		FileDatabaseConfigUtils.discardConfig();
	}
	
	private static class FileDatabaseHolder {
		
		private static final FileDatabase INSTANCE = new FileDatabase();
		
	}
	
}
