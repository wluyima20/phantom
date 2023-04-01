/*
 * Add Copyright
 */
package com.amiyul.phantom.api;

import java.util.function.Supplier;

/**
 * Implementations provide a phantom {@link Database} instance
 */
public interface DatabaseProvider<T extends Database> extends Supplier<T> {}
