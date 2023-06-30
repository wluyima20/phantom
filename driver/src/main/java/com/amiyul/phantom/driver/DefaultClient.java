/*
 * Copyright (C) Amiyul LLC - All Rights Reserved
 *
 * This source code is protected under international copyright law. All rights
 * reserved and protected by the copyright holder.
 * 
 * This file is confidential and only available to authorized individuals with the
 * permission of the copyright holder. If you encounter this file and do not have
 * permission, please contact the copyright holder and delete this file.
 */
package com.amiyul.phantom.driver;

import static com.amiyul.phantom.api.Utils.isDateAfter;
import static com.amiyul.phantom.api.logging.LoggerUtils.debug;
import static java.time.LocalDateTime.now;

import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.amiyul.phantom.api.ConnectionRequest;
import com.amiyul.phantom.api.Constants;
import com.amiyul.phantom.api.DefaultRequest;
import com.amiyul.phantom.api.PhantomProtocol.Command;
import com.amiyul.phantom.api.RequestContext;
import com.amiyul.phantom.api.Status;
import com.amiyul.phantom.api.StatusRequest;
import com.amiyul.phantom.api.Utils;

/**
 * Default implementation of a {@link Client}
 */
public class DefaultClient implements Client {
	
	private static ScheduledExecutorService delayedExecutor;
	
	private static ExecutorService asyncExecutor;
	
	private DefaultClient() {
	}
	
	protected static DefaultClient getInstance() {
		return DefaultClientHolder.INSTANCE;
	}
	
	@Override
	public Connection connect(ConnectionRequestData requestData) throws SQLException {
		DriverConfig config = DriverConfigUtils.getConfig();
		final String targetDbName = requestData.getTargetDbName();
		LocalDateTime targetUnavailableUntil = getStatus(targetDbName).getUnavailableUntil();
		LocalDateTime asOfDate = now();
		if (!config.getStatus().isUnavailable(asOfDate) && !isDateAfter(targetUnavailableUntil, asOfDate)) {
			return doConnect(requestData);
		}
		
		//Sanity check just in case the configs have been updated to put the DB back up
		getInstance().reload();
		config = DriverConfigUtils.getConfig();
		targetUnavailableUntil = getStatus(targetDbName).getUnavailableUntil();
		if (!config.getStatus().isUnavailable(asOfDate) && !isDateAfter(targetUnavailableUntil, asOfDate)) {
			return doConnect(requestData);
		}
		
		if (config.getStatus().isUnavailable(asOfDate)) {
			debug(Constants.DATABASE_NAME + " DB is unavailable until -> " + config.getStatus().getUnavailableUntil());
		}
		
		if (isDateAfter(targetUnavailableUntil, asOfDate)) {
			debug(targetDbName + " DB is unavailable until -> " + targetUnavailableUntil);
		}
		
		LocalDateTime effectiveDate = config.getStatus().getUnavailableUntil();
		if (targetUnavailableUntil != null) {
			if (effectiveDate == null || isDateAfter(targetUnavailableUntil, effectiveDate)) {
				effectiveDate = targetUnavailableUntil;
			}
		}
		
		//TODO Add support for a user to chose async processing if the DB is unavailable
		long delay = Utils.getDurationBetween(now(), effectiveDate).getSeconds();
		
		debug("Waiting to connect for " + delay + " seconds");
		
		//TODO Make the thread pool size configurable i.e. min and max
		if (delayedExecutor == null) {
			delayedExecutor = Executors.newScheduledThreadPool(DriverUtils.getDefaultDelayedExecutorThreadCount());
		}
		
		try {
			return delayedExecutor.schedule(new DelayedConnectTask(requestData), delay, TimeUnit.SECONDS).get();
		}
		catch (Exception e) {
			throw new SQLException(e);
		}
	}
	
	/**
	 * Processes a connection request using the information on the specified
	 * {@link ConnectionRequestData}
	 *
	 * @param requestData {@link ConnectionRequestData}
	 * @return Connection object
	 * @throws SQLException
	 */
	protected Connection doConnect(ConnectionRequestData requestData) throws SQLException {
		if (!requestData.isAsync()) {
			return doConnectInternal(requestData);
		}
		
		//TODO Make the thread pool size configurable i.e. min and max
		if (asyncExecutor == null) {
			asyncExecutor = Executors.newFixedThreadPool(DriverUtils.getDefaultAsyncExecutorThreadCount());
		}
		
		asyncExecutor.execute(new AsyncConnectTask(requestData));
		
		return (Connection) Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(),
		    new Class[] { Connection.class }, new ProxyConnection());
	}
	
	/**
	 * Sends a connection request to the database
	 *
	 * @param requestData
	 * @return Connection object
	 * @throws SQLException
	 */
	protected Connection doConnectInternal(ConnectionRequestData requestData) throws SQLException {
		final String targetDbName = requestData.getTargetDbName();
		
		debug("Requesting connection to database: " + targetDbName);
		
		DefaultRequestContext requestContext = new DefaultRequestContext();
		requestContext.setRequest(new ConnectionRequest(targetDbName, requestContext));
		
		try {
			sendRequest(requestContext);
		}
		catch (SQLException e) {
			debug("Failed to obtain a connection to database: " + targetDbName + ", reloading config");
			
			DriverConfigUtils.reloadConfig();
			
			sendRequest(requestContext);
		}
		
		debug("Successfully connected to database: " + targetDbName);
		
		return requestContext.readResult();
	}
	
	@Override
	public void reload() throws SQLException {
		debug("Sending reload signal");
		
		DefaultRequestContext requestContext = new DefaultRequestContext();
		requestContext.setRequest(new DefaultRequest(Command.RELOAD, requestContext));
		sendRequest(requestContext);
		
		debug("Successfully reloaded " + Constants.DATABASE_NAME + " DB");
	}
	
	@Override
	public Status getStatus(String targetDatabaseName) throws SQLException {
		debug("Requesting the status for database: " + targetDatabaseName);
		
		DefaultRequestContext requestContext = new DefaultRequestContext();
		requestContext.setRequest(new StatusRequest(targetDatabaseName, requestContext));
		
		sendRequest(requestContext);
		
		debug("Successfully got the status for database: " + targetDatabaseName);
		
		return requestContext.readResult();
	}
	
	/**
	 * Sends a request to the database
	 *
	 * @param context {@link RequestContext} object
	 * @throws SQLException
	 */
	protected void sendRequest(RequestContext context) throws SQLException {
		DriverConfigUtils.getConfig().getDatabase().process(context);
	}
	
	/**
	 * Gracefully shuts down the client e.g. shutting down the delayed and async request executors.
	 */
	protected void shutdown() {
		if (delayedExecutor != null) {
			debug("Shutting down executor for delayed request processor threads");
			
			delayedExecutor.shutdownNow();
		}
		
		if (asyncExecutor != null) {
			debug("Shutting down executor for async request processor threads");
			
			asyncExecutor.shutdownNow();
		}
	}
	
	private static class DefaultClientHolder {
		
		private static final DefaultClient INSTANCE = new DefaultClient();
		
	}
	
}
