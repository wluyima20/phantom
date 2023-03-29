/*
 * Add Copyright
 */
package com.amiyul.phantom.api;

import java.util.function.Supplier;

/**
 * Simulation of a response sent by the {@link Database} server back to the client
 * 
 * @param <T> the result type
 */
public interface Response<T> extends Supplier<T> {
	
}
