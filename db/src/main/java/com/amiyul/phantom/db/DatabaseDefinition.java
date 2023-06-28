/*
 * Copyright [yyyy] Amiyul LLC
 * All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.amiyul.phantom.db;

import java.util.Properties;

import com.amiyul.phantom.api.BaseStateful;
import com.amiyul.phantom.api.Named;
import com.amiyul.phantom.api.Status;

import lombok.Getter;

/**
 * Encapsulates information for a single target database instance
 */
public class DatabaseDefinition extends BaseStateful implements Named {
	
	private String name;
	
	@Getter
	private String url;
	
	@Getter
	private Properties properties;
	
	public DatabaseDefinition(String name, String url, Properties properties, Status status) {
		super(status);
		this.name = name;
		this.url = url;
		this.properties = properties;
	}
	
	/**
	 * @see Named#getName()
	 */
	@Override
	public String getName() {
		return name;
	}
	
	/**
	 * @see Named#setName(String)
	 */
	@Override
	public void setName(String name) {
		this.name = name;
	}
	
}
