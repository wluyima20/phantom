/*
 * Add Copyright
 */
package com.amiyul.phantom.driver;

/**
 * Base class for tasks
 */
public abstract class BaseTask implements Task {
	
	private String name;
	
	public BaseTask(String name) {
		this.name = name;
	}
	
	@Override
	public String getName() {
		return name;
	}
	
}
