/*
 * Add Copyright
 */
package com.amiyul.phantom.api;

public interface Transport {
	
	<TR, T extends Request<TR>, SR, S extends Response<SR>> void send(RequestContext<TR, T, SR, S> context);
	
}
