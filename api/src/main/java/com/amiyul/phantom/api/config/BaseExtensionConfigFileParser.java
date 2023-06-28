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

/**
 * Base class for ConfigFileParsers that check the config file extension to determine if they can
 * parse the file
 */
public abstract class BaseExtensionConfigFileParser<T> implements ConfigFileParser<T> {
	
	@Override
	public boolean canParse(File configFile) {
		return configFile.getName().endsWith("." + getExtension(configFile));
	}
	
	/**
	 * Gets the supported file extension
	 * 
	 * @param configFile config file
	 * @return file extension
	 */
	public abstract String getExtension(File configFile);
	
}
