/*
 * Add Copyright
 */
package com.amiyul.phantom.db;

import com.amiyul.phantom.api.DatabaseProvider;

/**
 * Provider for {@link FileDatabase} instances
 */
public class FileDatabaseProvider implements DatabaseProvider<FileDatabase> {
	
	@Override
	public FileDatabase get() {
		return FileDatabase.getInstance();
	}
	
}
