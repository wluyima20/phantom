/*
 * Add Copyright
 */
package com.amiyul.phantom.driver;

/**
 * Shutdown hook that cleans up resources used by the driver.
 */
public class ShutdownHook implements Runnable {
	
	@Override
	public void run() {
		DefaultClient.getInstance().shutdown();
	}
	
}
