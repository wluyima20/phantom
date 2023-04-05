/*
 * Add Copyright
 */
package com.amiyul.phantom.driver;

import java.sql.Connection;
import java.sql.SQLException;

import com.amiyul.phantom.api.ConnectionRequest;
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
	public Connection connect(String targetDatabaseName) throws SQLException {
		LoggerUtils.debug("Obtaining connection to the database named: " + targetDatabaseName);
		
		DefaultRequestContext requestContext = new DefaultRequestContext();
		requestContext.request = new ConnectionRequest(targetDatabaseName, requestContext);
		sendRequest(requestContext);
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
	
	private static class DefaultClientHolder {
		
		private static final DefaultClient INSTANCE = new DefaultClient();
		
	}
	
	private static class DefaultRequestContext implements RequestContext {
		
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
