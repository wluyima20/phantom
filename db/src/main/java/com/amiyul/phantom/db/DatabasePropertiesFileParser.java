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

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;

import com.amiyul.phantom.api.Constants;
import com.amiyul.phantom.api.Status;
import com.amiyul.phantom.api.Utils;
import com.amiyul.phantom.api.config.BasePropertiesFileParser;
import com.amiyul.phantom.api.config.ConfigFileParser;

/**
 * {@link ConfigFileParser} for a database properties file
 */
public class DatabasePropertiesFileParser extends BasePropertiesFileParser<DatabaseConfigMetadata> implements DatabaseConfigFileParser {
	
	protected static final String PROP_DATABASES = "databases";
	
	protected static final String PROP_URL = "url";
	
	protected static final String PROP_PROPS = "properties";
	
	@Override
	public DatabaseConfigMetadata createInstance(Properties properties) throws Exception {
		final Map<String, String> map = cleanEntries(new HashMap(properties));
		List<DatabaseDefinition> dbDefs = new ArrayList(map.size());
		Arrays.stream(map.get(PROP_DATABASES).split(",")).map(dbName -> dbName.trim()).forEach(dbName -> {
			Map<String, String> dbDefProps = getPropsWithPrefix(map, dbName);
			Properties dbProps = new Properties();
			dbProps.putAll(getPropsWithPrefix(dbDefProps, PROP_PROPS));
			LocalDateTime unavailableUntil = null;
			final String unavailableUntilStr = dbDefProps.get(Constants.PROP_UNAVAILABLE_UNTIL);
			if (!Utils.isBlank(unavailableUntilStr)) {
				unavailableUntil = Utils.parseDateString(unavailableUntilStr);
			}
			
			dbDefs.add(new DatabaseDefinition(dbName, dbDefProps.get(PROP_URL), dbProps, new Status(unavailableUntil)));
		});
		
		return () -> dbDefs;
	}
	
	/**
	 * Gets the map entries with keys that start with the specified prefix
	 * 
	 * @param map the map to search
	 * @param prefix the prefix to match
	 * @return Map
	 */
	private Map<String, String> getPropsWithPrefix(Map<String, String> map, String prefix) {
		return map.entrySet().stream().filter(e -> e.getKey().startsWith(prefix + ".")).collect(Collectors.toMap(e -> {
			int propStartIndex = e.getKey().indexOf(prefix) + prefix.length() + 1;
			return e.getKey().substring(propStartIndex);
		}, Map.Entry::getValue));
	}
	
	/**
	 * Removes blank values and trims all values in the specified map
	 * 
	 * @param map the map to clean
	 * @return Map
	 */
	private Map<String, String> cleanEntries(Map<String, String> map) {
		Map<String, String> cleanedMap = new HashMap(map.size());
		map.forEach((k, v) -> {
			if (v != null && !Utils.isBlank(v)) {
				cleanedMap.put(k, v.trim());
			}
		});
		
		return cleanedMap;
	}
	
}
