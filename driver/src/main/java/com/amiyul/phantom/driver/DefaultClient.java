/*
 * Add Copyright
 */
package com.amiyul.phantom.driver;

import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.concurrent.CompletableFuture;

import com.amiyul.phantom.api.ConnectionRequest;
import com.amiyul.phantom.api.DefaultRequest;
import com.amiyul.phantom.api.PhantomProtocol.Command;
import com.amiyul.phantom.api.Request;
import com.amiyul.phantom.api.RequestContext;
import com.amiyul.phantom.api.Response;
import com.amiyul.phantom.api.logging.LoggerUtils;

/**
 * Default implementation of a {@link Client}
 */
public class DefaultClient implements Client {
	
	private DefaultClient() {
	}
	
	protected static DefaultClient getInstance() {
		return DefaultClientHolder.INSTANCE;
	}
	
	@Override
	public Connection connect(ConnectionRequestData requestData) throws SQLException {
		final String targetDatabaseName = requestData.getTargetDatabaseName();
		
		LoggerUtils.debug("Obtaining connection to the database named: " + targetDatabaseName);
		
		DefaultRequestContext requestContext = new DefaultRequestContext();
		requestContext.request = new ConnectionRequest(targetDatabaseName, requestContext);
		if (requestData.getAsync()) {
			CompletableFuture.runAsync(new RequestProcessorTask(requestContext, requestData.getListener()));
			//TODO Keep references to all futures for clean up during shutdown
			//TODO include the future on the proxy
			return (Connection) Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(),
			    new Class[] { Connection.class }, new FailingConnectionInvocationHandler());
		} else {
			sendRequest(requestContext);
		}
		
		return requestContext.readResult();
	}
	
	@Override
	public void reload() throws SQLException {
		LoggerUtils.debug("Sending reload signal");
		
		DefaultRequestContext requestContext = new DefaultRequestContext();
		requestContext.request = new DefaultRequest(Command.RELOAD, requestContext);
		sendRequest(requestContext);
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
