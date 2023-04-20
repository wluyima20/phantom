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
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
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
	
	//TODO Limit queue capacity
	private static final DelayQueue<DelayedConnectionRequest> DELAYED_CONN_REQUESTS = new DelayQueue<>();
	
	private DefaultClient() {
	}
	
	protected static DefaultClient getInstance() {
		return DefaultClientHolder.INSTANCE;
	}
	
	@Override
	public Connection connect(ConnectionRequestData requestData) throws SQLException {
		if (!requestData.isAsync()) {
			return doConnect(requestData);
		}
		
		CompletableFuture.runAsync(new ConnectTask(requestData));
		
		return (Connection) Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(),
		    new Class[] { Connection.class }, new FailingConnectionInvocationHandler());
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
		//TODO Also check if the target DB is under maintenance
		Database db = DriverConfigUtils.getConfig().getDatabase();
		if (!db.isUnderMaintenance(now())) {
			return doConnectInternal(requestData);
		}
		
		//Sanity check just in case the Db config has been updated to put the DB out of maintenance
		DefaultClient.getInstance().reload();
		db = DriverConfigUtils.getConfig().getDatabase();
		if (!db.isUnderMaintenance(now())) {
			return doConnectInternal(requestData);
		}
		
		warn(Constants.DATABASE_NAME + " DB is not unavailable until -> " + db.getUnderMaintenanceUntil());
		
		//TODO Add support for a user to chose async processing in case if DB is under maintenance
		DelayedConnectionRequest delayed = new DelayedConnectionRequest(requestData, db.getUnderMaintenanceUntil());
		DELAYED_CONN_REQUESTS.add(delayed);
		
		long delay = delayed.getDelay(TimeUnit.SECONDS);
		
		debug("Waiting to connect for " + delay + " seconds");
		
		ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
		Future<Connection> cf = executor.schedule(() -> {
			debug("Processing delayed connection request");
			return doConnectInternal(requestData);
		}, delay, TimeUnit.SECONDS);
		
		Connection connection;
		try {
			connection = cf.get();
			executor.shutdownNow();
			return connection;
		}
		catch (Exception e) {
			throw new SQLException(e);
		}
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
	
	private Connection doConnectInternal(ConnectionRequestData requestData) throws SQLException {
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
