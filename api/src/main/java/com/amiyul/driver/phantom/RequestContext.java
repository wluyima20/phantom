/*
 * Add Copyright
 */
package com.amiyul.driver.phantom;

public interface RequestContext<TR, T extends Request<TR>, SR, S extends Response<SR>> {
	
	T getRequest();
	
	S getResponse();
	
	Pipeline getPipeline();
	
	void next();
	
	TR read();
	
	void write(SR result);
	
}
