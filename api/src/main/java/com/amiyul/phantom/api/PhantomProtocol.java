/*
 * Add Copyright
 */
package com.amiyul.phantom.api;

/**
 * Contains protocol details used for communication between a phantom driver client and database
 * server
 */
public final class PhantomProtocol {
	
	/**
	 * Commands that a client send to the server for execution
	 */
	public enum Command {
		CONNECT, RELOAD
	}
	
}
