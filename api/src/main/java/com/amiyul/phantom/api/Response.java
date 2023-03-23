/*
 * Add Copyright
 */
package com.amiyul.phantom.api;

import java.util.function.Supplier;

/**
 * Simulation of a response sent by the database {@link Server} back to the {@link Client}
 */
public interface Response<T> extends Supplier<T> {
	
}
