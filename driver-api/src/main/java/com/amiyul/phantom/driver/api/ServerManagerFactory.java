/*
 * Add Copyright
 */
package com.amiyul.phantom.driver.api;

import com.amiyul.phantom.api.Database;

public class ServerManagerFactory {
	
	private ServerManagerFactory() {
	}
	
	private static class ServerManagerFactoryHolder {
		
		private static final ServerManagerFactory INSTANCE = new ServerManagerFactory();
		
	}
	
	public static ServerManagerFactory getInstance() {
		return ServerManagerFactoryHolder.INSTANCE;
	}
	
	public ServerManager createManager(Database database) {
		return null;
	}
	
}
