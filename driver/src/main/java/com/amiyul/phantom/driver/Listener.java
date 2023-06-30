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
 * Marker interface for listeners that wish to be notified when a specific operation completes
 * successfully or fails.
 */
public interface Listener<T> {
	
	/**
	 * Called after a successful operation
	 * 
	 * @param object the result of the operation
	 */
	void onSuccess(T object);
	
	/**
	 * Called after a failed operation
	 *
	 * @param throwable the thrown exception
	 */
	void onFailure(Throwable throwable);
	
}
