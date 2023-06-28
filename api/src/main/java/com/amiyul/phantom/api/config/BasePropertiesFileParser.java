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
package com.amiyul.phantom.api.config;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

/**
 * Base class for ConfigFileParsers for properties files
 */
public abstract class BasePropertiesFileParser<T> extends BaseExtensionConfigFileParser<T> {
	
	@Override
	public String getExtension(File configFile) {
		return "properties";
	}
	
	@Override
	public T parse(FileInputStream configInputStream) throws Exception {
		Properties props = new Properties();
		props.load(configInputStream);
		return createInstance(props);
	}
	
	/**
	 * Creates an instance using the specified properties
	 * 
	 * @param properties the properties to use
	 * @return created instance
	 */
	public abstract T createInstance(Properties properties) throws Exception;
	
}
