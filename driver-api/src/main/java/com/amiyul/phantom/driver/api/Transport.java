/*
 * Add Copyright
 */
package com.amiyul.phantom.driver.api;

import com.amiyul.phantom.api.Request;
import com.amiyul.phantom.api.RequestContext;
import com.amiyul.phantom.api.Response;

public interface Transport {
	
	<TR, T extends Request<TR>, SR, S extends Response<SR>> void send(RequestContext<TR, T, SR, S> context);
	
}
