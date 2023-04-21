/*
 * Add Copyright
 */
package com.amiyul.phantom.driver;

import java.util.Arrays;
import java.util.List;

import com.amiyul.phantom.api.Constants;

public final class DriverConstants {
	
	private static final String PREFIX_DB = "database.";
	
	protected static final String URL_PREFIX = "jdbc:" + Constants.DATABASE_NAME + "://";
	
	protected static final char URL_SEPARATOR_DB_PARAM = '?';
	
	protected static final String URL_SEPARATOR_PARAMS = ";";
	
	protected static final String URL_SEPARATOR_PARAM_KEY_VALUE = "=";
	
	protected static final String URL_PARAM_ASYNC = "async";
	
	protected static final String URL_PARAM_ASYNC_LISTENER = "asyncConnectionListener";
	
	public static final String PROP_DB_UNDER_MAINTENANCE_UNTIL = PREFIX_DB + "under.maintenance.until";
	
	public static final String PROP_DB_PROVIDER_CLASS = PREFIX_DB + "provider.class";
	
	protected static final int DEFAULT_THREAD_SIZE = 5;
	
	protected static final List<String> PROP_NAMES = Arrays.asList(URL_PARAM_ASYNC, URL_PARAM_ASYNC_LISTENER);
	
}
