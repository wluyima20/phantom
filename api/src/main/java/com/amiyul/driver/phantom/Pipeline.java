/*
 * Add Copyright
 */
package com.amiyul.driver.phantom;

import java.util.function.Consumer;

public interface Pipeline<T> {
	
	void addProcessor(Consumer<T> processor);
	
	Consumer<T> getNext();
	
}
