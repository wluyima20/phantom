/*
 * Add Copyright
 */
package com.amiyul.driver.phantom;

import java.util.function.Supplier;

/**
 * Simulation of a response sent by the database {@link com.amiyul.driver.phantom.server.Server}
 * back to the {@link Client}
 */
public interface Response<T> extends Supplier<T> {
	
}
