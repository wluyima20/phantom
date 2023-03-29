/*
 * Add Copyright
 */
package com.amiyul.phantom.api;

/**
 * Default implementation of a {@link RequestContext}
 */
public class DefaultRequestContext implements RequestContext {
	
	private final Request request;
	
	private Response response;
	
	public DefaultRequestContext(Request request) {
		this.request = request;
	}
	
	@Override
	public Request getRequest() {
		return request;
	}
	
	@Override
	public Response getResponse() {
		return response;
	}
	
	@Override
	public <T> T readResult() {
		return getResponse() == null ? null : getResponse().getResult();
	}
	
	@Override
	public void writeResult(Object result) {
		this.response = new DefaultResponse(result);
	}
	
}
