/*
 * Add Copyright
 */
package com.amiyul.phantom.api;

public interface RequestContext<R, REQ extends Request<R>, RESP extends Response<R>> {
	
	REQ getRequest();
	
	RESP getResponse();
	
	R readResult();
	
	void writeResult(R result);
	
}
