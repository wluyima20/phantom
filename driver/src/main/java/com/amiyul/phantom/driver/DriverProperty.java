/*
 * Add Copyright
 */
package com.amiyul.phantom.driver;

import java.sql.DriverPropertyInfo;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Encapsulates info about a driver property
 */
enum DriverProperty {
	
	TARGET_DB(DriverConstants.PROP_DRIVER_TARGET_DB, DriverConstants.PROP_DRIVER_DESCR_TARGET_DB, true),
	
	ASYNC(DriverConstants.PROP_DRIVER_ASYNC, DriverConstants.PROP_DRIVER_DESCR_ASYNC, false,
	        new String[] { Boolean.TRUE.toString(), Boolean.FALSE.toString() }),
	
	CONNECTION_LISTENER(DriverConstants.PROP_DRIVER_CONN_LISTENER, DriverConstants.PROP_DRIVER_DESCR_LISTENER);
	
	final private String name;
	
	final private String description;
	
	final private boolean required;
	
	final private String[] choices;
	
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
	 * Gets the name
	 *
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Gets the description
	 *
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}
	
	/**
	 * Gets the required
	 *
	 * @return the required
	 */
	public boolean isRequired() {
		return required;
	}
	
	/**
	 * Gets the choices
	 *
	 * @return the choices
	 */
	public String[] getChoices() {
		return choices;
	}
	
	/**
	 * Creates a {@link DriverPropertyInfo} instance from this {@link DriverProperty}
	 * 
	 * @return a {@link DriverPropertyInfo} instance
	 */
	public DriverPropertyInfo toDriverPropertyInfo() {
		DriverPropertyInfo info = new DriverPropertyInfo(name, null);
		info.description = description;
		info.required = required;
		if (getChoices() != null) {
			info.choices = Arrays.copyOf(getChoices(), getChoices().length);
		}
		
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
		if (props == null) {
			return new HashMap<>();
		}
		
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
	
	/**
	 * Looks up the {@link DriverProperty} enum value that matches the specified property name
	 * 
	 * @param propertyName the property name
	 * @return DriverProperty instance
	 */
	protected static DriverProperty findByName(String propertyName) {
		for (DriverProperty prop : values()) {
			if (prop.name.equals(propertyName)) {
				return prop;
			}
		}
		
		throw new RuntimeException("Found an invalid driver property named: " + propertyName);
	}
	
}
