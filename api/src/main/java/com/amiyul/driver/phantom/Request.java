/*
 * Add Copyright
 */
package com.amiyul.driver.phantom;

import java.util.function.Supplier;

/**
 * Simulation of a request sent by the database {@link Client} to the
 * {@link com.amiyul.driver.phantom.server.Server}
 */
public interface Request<T> extends Supplier<T> {
	
}
