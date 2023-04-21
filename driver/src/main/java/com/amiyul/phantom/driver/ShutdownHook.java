/*
 * Add Copyright
 */
package com.amiyul.phantom.driver;

/**
 * Shutdown hook that cleans up resources used by the driver.
 */
public class ShutdownHook implements Runnable {
	
	private ShutdownHook() {
	}
	
	protected static ShutdownHook getInstance() {
		return ShutdownHookHolder.INSTANCE;
	}
	
	@Override
	public void run() {
		DefaultClient.getInstance().shutdown();
	}
	
	private static class ShutdownHookHolder {
		
		private static final ShutdownHook INSTANCE = new ShutdownHook();
		
	}
	
}
