/*
 * Add Copyright
 */
package com.amiyul.phantom.api;

public interface RequestContext<TR, T extends Request<TR>, SR, S extends Response<SR>> {
	
	T getRequest();
	
	S getResponse();
	
	TR read();
	
	void write(SR result);
	
}
