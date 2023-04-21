/*
 * Add Copyright
 */
package com.amiyul.phantom.driver;

import java.util.concurrent.Callable;

/**
 * Base class for tasks
 */
public abstract class BaseTask<T> implements Task<T>, Runnable, Callable<T> {
	
	private String name;
	
	public BaseTask(String name) {
		this.name = name;
	}
	
	@Override
	public String getName() {
		return name;
	}
	
	@Override
	public void run() {
		execute();
	}
	
	@Override
	public T call() throws Exception {
		return execute();
	}
	
	@Override
	public T execute() {
		String originalThreadName = Thread.currentThread().getName();
		
		try {
			Thread.currentThread().setName(originalThreadName + ":" + getName());
			return doExecute();
		}
		finally {
			Thread.currentThread().setName(originalThreadName);
		}
	}
	
	/**
	 * @see Task#execute()
	 */
	abstract T doExecute();
	
}
