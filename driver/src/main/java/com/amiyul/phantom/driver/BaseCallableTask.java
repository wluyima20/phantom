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

import java.util.concurrent.Callable;

/**
 * Base class for {@link Callable} tasks
 */
public abstract class BaseCallableTask<T> extends BaseTask implements Callable<T> {
	
	public BaseCallableTask(String name) {
		super(name);
	}
	
	@Override
	public T call() throws Exception {
		String originalThreadName = Thread.currentThread().getName();
		
		try {
			Thread.currentThread().setName(originalThreadName + ":" + getName());
			return doCall();
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
	abstract T doCall() throws Exception;
}
