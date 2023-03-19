/*
 * Add Copyright
 */
package com.amiyul.phantom.driver.config;

/**
 * Marker interface for builders for {@link Config} instances
 */
@FunctionalInterface
public interface ConfigBuilder {
	
	Config build();
	
}
