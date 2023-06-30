/*
 * Copyright (C) Amiyul LLC - All Rights Reserved
 *
 * This source code is protected under international copyright law. All rights
 * reserved and protected by the copyright holder.
 * 
 * This file is confidential and only available to authorized individuals with the
 * permission of the copyright holder. If you encounter this file and do not have
 * permission, please contact the copyright holder and delete this file.
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
