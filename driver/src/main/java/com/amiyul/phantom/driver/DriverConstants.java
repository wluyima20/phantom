/*
 * Add Copyright
 */
package com.amiyul.phantom.driver;

import java.util.Arrays;
import java.util.List;

import com.amiyul.phantom.api.Constants;

public final class DriverConstants {
	
	protected static final String URL_PREFIX = "jdbc:" + Constants.DATABASE_NAME + "://";
	
	protected static final char URL_DB_PARAM_SEPARATOR = '?';
	
	protected static final String URL_PARAMS_SEPARATOR = ";";
	
	protected static final String URL_PARAM_KEY_VALUE_SEPARATOR = "=";
	
	protected static final String PROP_ASYNC = "async";
	
	protected static final String PROP_ASYNC_LISTENER = "asyncConnectionListener";
	
	protected static final List<String> PROP_NAMES = Arrays.asList(PROP_ASYNC, PROP_ASYNC_LISTENER);
	
}
