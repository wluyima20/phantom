/*
 * Add Copyright
 */
package com.amiyul.phantom.driver;

import com.amiyul.phantom.api.logging.LoggerUtils;

/**
 * Base class for {@link NotifyingTask}
 */
public abstract class BaseNotifyingTask<T> implements NotifyingTask {
	
	private String name;
	
	private Listener<T> listener;
	
	public BaseNotifyingTask(String name, Listener<T> listener) {
		this.name = name;
		this.listener = listener;
	}
	
	@Override
	public String getName() {
		return name;
	}
	
	@Override
	public Listener<T> getListener() {
		return listener;
	}
	
	@Override
	public void run() {
		String originalThreadName = Thread.currentThread().getName();
		
		try {
			Thread.currentThread().setName(originalThreadName + ":" + getName());
			
			T result = doRun();
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
		finally {
			Thread.currentThread().setName(originalThreadName);
		}
	}
	
	/**
	 * Subclasses should add their implementation logic in this method
	 *
	 * @return T the result generated by the task
	 * @throws Exception
	 */
	public abstract T doRun() throws Exception;
	
}