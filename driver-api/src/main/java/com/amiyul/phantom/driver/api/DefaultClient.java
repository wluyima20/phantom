/*
 * Add Copyright
 */
package com.amiyul.phantom.driver.api;

import java.sql.Connection;
import java.sql.SQLException;

import com.amiyul.phantom.api.ConnectionRequest;
import com.amiyul.phantom.api.Request;
import com.amiyul.phantom.api.RequestContext;
import com.amiyul.phantom.api.Response;
import com.amiyul.phantom.api.config.ConfigUtils;

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
	public Connection connect(String targetDatabaseKey) throws SQLException {
		RequestContext requestContext = new DefaultRequestContext(new ConnectionRequest(targetDatabaseKey));
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
		ConfigUtils.getConfig().getDatabase().process(context);
	}
	
	private static class DefaultClientHolder {
		
		private static final DefaultClient INSTANCE = new DefaultClient();
		
	}
	
	private static class DefaultResponse implements Response {
		
		private final Object result;
		
		private DefaultResponse(Object result) {
			this.result = result;
		}
		
		@Override
		public <T> T getResult() {
			return (T) result;
		}
		
	}
	
	private static class DefaultRequestContext implements RequestContext {
		
		private final Request request;
		
		private Response response;
		
		private DefaultRequestContext(Request request) {
			this.request = request;
		}
		
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
			this.response = new DefaultResponse(result);
		}
		
	}
	
}
