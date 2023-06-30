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
