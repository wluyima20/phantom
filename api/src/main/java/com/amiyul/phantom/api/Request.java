/*
 * Add Copyright
 */
package com.amiyul.phantom.api;

import java.util.function.Supplier;

/**
 * Simulation of a request sent by the database {@link Client} to the {@link Server}
 */
public interface Request<T> extends Supplier<T> {
	
}
