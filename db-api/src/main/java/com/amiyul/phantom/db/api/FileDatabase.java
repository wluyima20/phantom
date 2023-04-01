/*
 * Add Copyright
 */
package com.amiyul.phantom.db.api;

import java.sql.SQLException;

import com.amiyul.phantom.api.BaseDatabase;
import com.amiyul.phantom.api.Database;
import com.amiyul.phantom.api.RequestContext;

/**
 * Base class for {@link Database} implementations backed by a file
 */
public class FileDatabase extends BaseDatabase {
	
	private FileDatabase() {
	}
	
	public static FileDatabase getInstance() {
		return FileDatabaseHolder.INSTANCE;
	}
	
	@Override
	public void process(RequestContext context) throws SQLException {

	}
	
	private static class FileDatabaseHolder {
		
		private static final FileDatabase INSTANCE = new FileDatabase();
		
	}
	
}
