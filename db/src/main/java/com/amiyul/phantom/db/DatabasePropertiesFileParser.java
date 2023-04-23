/*
 * Add Copyright
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
	
	protected static final String PROP_UNDER_MAINTENANCE = "down.until";
	
	@Override
	public DatabaseConfigMetadata createInstance(Properties properties) throws Exception {
		final Map<String, String> map = cleanEntries(new HashMap(properties));
		List<DatabaseDefinition> dbDefs = new ArrayList(map.size());
		Arrays.stream(map.get(PROP_DATABASES).split(",")).map(dbName -> dbName.trim()).forEach(dbName -> {
			Map<String, String> dbDefProps = getPropsWithPrefix(map, dbName);
			Properties dbProps = new Properties();
			dbProps.putAll(getPropsWithPrefix(dbDefProps, PROP_PROPS));
			LocalDateTime downUntil = null;
			final String downUntilStr = dbDefProps.get(PROP_UNDER_MAINTENANCE);
			if (!Utils.isBlank(downUntilStr)) {
				downUntil = Utils.parseDateString(downUntilStr);
			}
			
			dbDefs.add(new DatabaseDefinition(dbName, dbDefProps.get(PROP_URL), dbProps, downUntil));
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
