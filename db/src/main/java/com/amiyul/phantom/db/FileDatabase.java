/*
 * Add Copyright
 */
package com.amiyul.phantom.db;

import java.sql.SQLException;

import com.amiyul.phantom.api.BaseDatabase;
import com.amiyul.phantom.api.ConnectionRequest;
import com.amiyul.phantom.api.Database;
import com.amiyul.phantom.api.PhantomProtocol.Command;
import com.amiyul.phantom.api.RequestContext;

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
	public void process(RequestContext context) throws SQLException {
		Command command = context.getRequest().getCommand();
		switch (command) {
			case CONNECT:
				FileDatabaseUtils.getConnection((ConnectionRequest) context.getRequest());
				break;
			case RELOAD:
				FileDatabaseConfigUtils.discardConfig();
				break;
			default:
				throw new SQLException("Don't know how to process protocol command: " + command);
		}
	}
	
	private static class FileDatabaseHolder {
		
		private static final FileDatabase INSTANCE = new FileDatabase();
		
	}
	
}
