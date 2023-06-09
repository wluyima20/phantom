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

import com.amiyul.phantom.api.logging.LoggerUtils;

/**
 * Base class for {@link Runnable} tasks that notify a listener of the values generated by the task
 * after completion.
 */
public abstract class BaseNotifyingTask<T> extends BaseTask implements Runnable {
	
	private Listener<T> listener;
	
	public BaseNotifyingTask(String name, Listener<T> listener) {
		super(name);
		this.listener = listener;
	}
	
	@Override
	public void run() {
		String originalThreadName = Thread.currentThread().getName();
		
		try {
			Thread.currentThread().setName(originalThreadName + ":" + getName());
			try {
				T result = getResult();
				if (listener != null) {
					LoggerUtils.debug("Completed successfully, notifying listener -> " + listener);
					
					try {
						listener.onSuccess(result);
					}
					catch (Throwable t) {
						LoggerUtils.error("An error occurred while notifying listener", t);
					}
				}
			}
			catch (Throwable throwable) {
				LoggerUtils.debug("Completed with errors, notifying listener -> " + listener);
				
				if (listener != null) {
					try {
						listener.onFailure(throwable);
					}
					catch (Throwable t) {
						LoggerUtils.error("An error occurred while notifying listener after a failure", t);
					}
				}
			}
		}
		finally {
			Thread.currentThread().setName(originalThreadName);
		}
	}
	
	/**
	 * Gets the result
	 *
	 * @return T the result
	 * @throws Exception
	 */
	abstract T getResult() throws Exception;
	
}
