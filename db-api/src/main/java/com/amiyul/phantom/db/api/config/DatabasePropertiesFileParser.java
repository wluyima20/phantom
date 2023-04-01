/*
 * Add Copyright
 */
package com.amiyul.phantom.db.api.config;

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
import com.amiyul.phantom.db.api.DatabaseMetadata;

/**
 * {@link ConfigFileParser} for a database properties file
 */
public class DatabasePropertiesFileParser extends BasePropertiesFileParser<DatabaseConfigMetadata> implements DatabaseConfigFileParser {
	
	protected static final String PROP_DATABASES = "databases";
	
	protected static final String PROP_URL = "url";
	
	protected static final String PROP_PROPS = "properties";
	
	@Override
	public DatabaseConfigMetadata createInstance(Properties properties) throws Exception {
		Map<String, String> props = cleanEntries(new HashMap(properties));
		List<DatabaseMetadata> dbMetadataList = new ArrayList(props.size());
		String[] dbNames = props.get(PROP_DATABASES).split(",");
		Arrays.stream(dbNames).forEach(dbName -> {
			dbName = dbName.trim();
			Map<String, String> dbMetadata = getPropsWithPrefix(props, dbName);
			Map<String, String> dbConnProps = getPropsWithPrefix(dbMetadata, PROP_PROPS);
			Properties dbProps = new Properties();
			dbProps.putAll(dbConnProps);
			dbMetadataList.add(new DatabaseMetadata(dbName, dbMetadata.get(PROP_URL), dbProps));
		});
		
		return () -> dbMetadataList;
	}
	
	private Map<String, String> getPropsWithPrefix(Map<String, String> map, String prefix) {
		return map.entrySet().stream().filter(e -> e.getKey().startsWith(prefix + ".")).collect(Collectors.toMap(e -> {
			int propStartIndex = e.getKey().indexOf(prefix) + prefix.length() + 1;
			return e.getKey().substring(propStartIndex);
		}, Map.Entry::getValue));
	}
	
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
