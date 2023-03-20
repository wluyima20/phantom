/*
 * Add Copyright
 */
package com.amiyul.driver.phantom.config;

/**
 * Marker interface for builders for {@link Config} instances
 */
@FunctionalInterface
public interface ConfigBuilder {
	
	Config build();
	
}
