/*
 * Add Copyright
 */
package com.amiyul.phantom.driver;

import java.sql.Connection;

/**
 * Implemented by classes that wish to be notified about {@link Connection} request events.
 */
public interface ConnectionListener extends Listener<Connection> {}
