/*
 * Add Copyright
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
