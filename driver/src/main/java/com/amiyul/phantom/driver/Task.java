/*
 * Add Copyright
 */
package com.amiyul.phantom.driver;

/**
 * This interface should be implemented by a named task, the name of the thread will set to the name
 * of the task.
 */
public interface Task extends Runnable {
	
	/**
	 * Gets the task name
	 * 
	 * @return the task name
	 */
	String getName();
	
}
