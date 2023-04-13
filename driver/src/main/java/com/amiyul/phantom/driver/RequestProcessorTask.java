/*
 * Add Copyright
 */
package com.amiyul.phantom.driver;

import com.amiyul.phantom.api.RequestContext;
import com.amiyul.phantom.api.logging.LoggerUtils;

/**
 * Processors a driver client request and notifies the {@link Listener} on success otherwise on
 * failure
 */
public class RequestProcessorTask implements Runnable {
	
	private final RequestContext requestContext;
	
	private final Listener listener;
	
	public RequestProcessorTask(RequestContext requestContext, Listener listener) {
		this.requestContext = requestContext;
		this.listener = listener;
	}
	
	@Override
	public void run() {
		//TODO Set thread name
		try {
			DriverConfigUtils.getConfig().getDatabase().process(requestContext);
			
			if (listener != null) {
				LoggerUtils.debug("Done processing async request, notifying the listener -> " + listener);
				
				try {
					listener.onSuccess(requestContext.readResult());
				}
				catch (Throwable t) {
					LoggerUtils.error("An error occurred while notifying listener", t);
				}
			}
		}
		catch (Throwable throwable) {
			LoggerUtils.debug("Failed to process async request, notifying the listener -> " + listener);
			
			if (listener != null) {
				try {
					listener.onFailure(throwable);
				}
				catch (Throwable t) {
					LoggerUtils.error("An error occurred while notifying listener after a failure", t);
				}
			}
		}
	}
	
}
