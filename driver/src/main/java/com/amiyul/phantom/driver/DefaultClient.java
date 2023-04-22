/*
 * Add Copyright
 */
package com.amiyul.phantom.driver;

import static com.amiyul.phantom.api.logging.LoggerUtils.debug;
import static com.amiyul.phantom.api.logging.LoggerUtils.warn;
import static java.time.LocalDateTime.now;

import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.amiyul.phantom.api.ConnectionRequest;
import com.amiyul.phantom.api.Constants;
import com.amiyul.phantom.api.Database;
import com.amiyul.phantom.api.DefaultRequest;
import com.amiyul.phantom.api.PhantomProtocol.Command;
import com.amiyul.phantom.api.Request;
import com.amiyul.phantom.api.RequestContext;
import com.amiyul.phantom.api.Response;

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
		//TODO Also check if the target DB is under maintenance
		Database db = DriverConfigUtils.getConfig().getDatabase();
		if (!db.isUnderMaintenance(now())) {
			return doConnect(requestData);
		}
		
		//Sanity check just in case the Db config has been updated to put the DB out of maintenance
		DefaultClient.getInstance().reload();
		db = DriverConfigUtils.getConfig().getDatabase();
		if (!db.isUnderMaintenance(now())) {
			return doConnect(requestData);
		}
		
		warn(Constants.DATABASE_NAME + " DB is not unavailable until -> " + db.getUnderMaintenanceUntil());
		
		//TODO Add support for a user to chose async processing in case if DB is under maintenance
		long delay = Duration.between(LocalDateTime.now(), db.getUnderMaintenanceUntil()).getSeconds();
		
		debug("Waiting to connect for " + delay + " seconds");
		
		//TODO use daemon threads make the thread pool size configurable i.e. min and max
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
	
	@Override
	public void reload() throws SQLException {
		debug("Sending reload signal");
		
		DefaultRequestContext requestContext = new DefaultRequestContext();
		requestContext.request = new DefaultRequest(Command.RELOAD, requestContext);
		sendRequest(requestContext);
	}
	
	/**
	 * Processes a connection request using the information on the specified
	 * {@link ConnectionRequestData}
	 *
	 * @param requestData {@link ConnectionRequestData}
	 * @return Connection
	 * @throws SQLException
	 */
	protected Connection doConnect(ConnectionRequestData requestData) throws SQLException {
		if (!requestData.isAsync()) {
			return doConnectInternal(requestData);
		}
		
		//TODO use daemon threads make the thread pool size configurable i.e. min and max
		if (asyncExecutor == null) {
			asyncExecutor = Executors.newScheduledThreadPool(DriverUtils.getDefaultAsyncExecutorThreadCount());
		}
		
		asyncExecutor.execute(new AsyncConnectTask(requestData));
		
		return (Connection) Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(),
		    new Class[] { Connection.class }, new FailingConnectionInvocationHandler());
	}
	
	/**
	 * Sends a connection request to the database
	 *
	 * @param requestData
	 * @return Connection object
	 * @throws SQLException
	 */
	protected Connection doConnectInternal(ConnectionRequestData requestData) throws SQLException {
		final String targetDbName = requestData.getTargetDatabaseName();
		
		debug("Obtaining connection to database: " + targetDbName);
		
		DefaultRequestContext requestContext = new DefaultRequestContext();
		requestContext.request = new ConnectionRequest(targetDbName, requestContext);
		
		try {
			sendRequest(requestContext);
		}
		catch (SQLException e) {
			debug("Failed to obtain a connection to database: " + targetDbName + ", reloading config");
			
			DriverConfigUtils.reloadConfig();
			
			sendRequest(requestContext);
		}
		
		debug("Connection obtained");
		
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
	
	protected static class DefaultRequestContext implements RequestContext {
		
		private Request request;
		
		private Response response;
		
		@Override
		public Request getRequest() {
			return request;
		}
		
		@Override
		public <T> T readResult() {
			return response == null ? null : response.getResult();
		}
		
		@Override
		public void writeResult(Object result) {
			this.response = new Response() {
				
				@Override
				public <T> T getResult() {
					return (T) result;
				}
				
			};
		}
		
	}
	
}
