/*
 * Add Copyright
 */
package com.amiyul.driver.phantom;

public interface Transport {
	
	<TR, T extends Request<TR>, SR, S extends Response<SR>> void send(RequestContext<TR, T, SR, S> context);
	
}
