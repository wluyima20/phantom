/*
 * Add Copyright
 */
package com.amiyul.phantom.driver;

import com.amiyul.phantom.api.Request;
import com.amiyul.phantom.api.RequestContext;
import com.amiyul.phantom.api.Response;

import lombok.Setter;

class DefaultRequestContext implements RequestContext {
	
	@Setter
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
