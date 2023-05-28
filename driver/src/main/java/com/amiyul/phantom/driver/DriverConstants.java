/*
 * Add Copyright
 */
package com.amiyul.phantom.driver;

import com.amiyul.phantom.api.Constants;

public final class DriverConstants {
	
	private static final String PREFIX_DB = "database.";
	
	protected static final String URL_PREFIX = "jdbc:" + Constants.DATABASE_NAME + "://";
	
	protected static final char URL_SEPARATOR_DB_PARAM = '?';
	
	protected static final String URL_SEPARATOR_PARAMS = ";";
	
	protected static final String URL_SEPARATOR_PARAM_KEY_VALUE = "=";
	
	protected static final String PROP_DRIVER_TARGET_DB = "targetDbName";
	
	protected static final String PROP_DRIVER_ASYNC = "async";
	
	protected static final String PROP_DRIVER_CONN_LISTENER = "connectionListener";
	
	protected static final String PROP_DRIVER_DESCR_TARGET_DB = "A unique name of a target database to which to connect";
	
	protected static final String PROP_DRIVER_DESCR_ASYNC = "Specifies whether the connection request should processed "
	        + "asynchronously or not, if set to true the connection listener MUST be specified via the property named "
	        + PROP_DRIVER_CONN_LISTENER;
	
	protected static final String PROP_DRIVER_DESCR_LISTENER = "Specifies the fully qualified java classname of the listener "
	        + "class to be notified upon connection success or failure, this is ONLY required if " + PROP_DRIVER_ASYNC
	        + " property is to true";
	
	protected static final String PROP_DB_UNAVAILABLE_UNTIL = PREFIX_DB + Constants.PROP_UNAVAILABLE_UNTIL;
	
	protected static final String PROP_DB_PROVIDER_CLASS = PREFIX_DB + "provider.class";
	
	protected static final int DEFAULT_THREAD_SIZE = 5;
	
}
