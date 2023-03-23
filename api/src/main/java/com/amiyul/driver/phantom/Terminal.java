/*
 * Add Copyright
 */
package com.amiyul.driver.phantom;

import java.util.function.Consumer;
import java.util.function.Supplier;

public interface Terminal<T> {
	
	Consumer<T> getReader();
	
	Supplier<T> getWriter();
	
	Pipeline getPipeline();
	
}
