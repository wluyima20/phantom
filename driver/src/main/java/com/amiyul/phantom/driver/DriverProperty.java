/*
 * Add Copyright
 */
package com.amiyul.phantom.driver;

import java.sql.DriverPropertyInfo;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Encapsulates info about a driver property
 */
public enum DriverProperty {
	
	TARGET_DB(DriverConstants.PROP_TARGET_DB, DriverConstants.PROP_DRIVER_DESCR_TARGET_DB, true),
	
	ASYNC(DriverConstants.URL_PARAM_ASYNC, DriverConstants.PROP_DRIVER_DESCR_ASYNC),
	
	CONNECTION_LISTENER(DriverConstants.URL_PARAM_ASYNC_LISTENER, DriverConstants.PROP_DRIVER_DESCR_LISTENER, false,
	        new String[] { Boolean.TRUE.toString(), Boolean.FALSE.toString() });
	
	private String name;
	
	private String description;
	
	private boolean required;
	
	private String[] choices;
	
	DriverProperty(String name, String description) {
		this(name, description, false);
	}
	
	DriverProperty(String name, String description, boolean required) {
		this(name, description, required, null);
	}
	
	DriverProperty(String name, String description, boolean required, String[] choices) {
		this.name = name;
		this.description = description;
		this.required = required;
		this.choices = choices;
	}
	
	/**
	 * Creates a {@link DriverPropertyInfo} instance from this {@link DriverProperty}
	 * 
	 * @return a {@link DriverPropertyInfo} instance
	 */
	public DriverPropertyInfo toDriverPropertyInfo() {
		DriverPropertyInfo info = new DriverPropertyInfo(name, null);
		info.description = description;
		info.choices = choices;
		return info;
	}
	
	/**
	 * Generates a mappings between {@link DriverProperty} and value from the specified
	 * {@link Properties} object.
	 * 
	 * @param props the {@link Properties} instance
	 * @return a map
	 */
	protected static Map<DriverProperty, String> toPropertyValueMap(Properties props) {
		Map<DriverProperty, String> propValueMap = new HashMap<>(props.size());
		props.forEach((key, value) -> {
			if (!key.equals(TARGET_DB.name)) {
				String propValue = null;
				if (value != null) {
					propValue = value.toString();
				}
				
				propValueMap.put(findByName(key.toString()), propValue);
			}
		});
		
		return propValueMap;
	}
	
	private static DriverProperty findByName(String name) {
		for (DriverProperty prop : values()) {
			if (prop.name.equals(name)) {
				return prop;
			}
		}
		
		throw new RuntimeException("Found no driver property named: " + name);
	}
	
}
