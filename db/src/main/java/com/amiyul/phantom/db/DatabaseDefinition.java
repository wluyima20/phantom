/*
 * Add Copyright
 */
package com.amiyul.phantom.db;

import java.time.LocalDateTime;
import java.util.Properties;

import com.amiyul.phantom.api.Named;

import lombok.Getter;

/**
 * Encapsulates information for a single target database instance
 */
public class DatabaseDefinition extends BaseMaintainable implements Named {
	
	private String name;
	
	@Getter
	private String url;
	
	@Getter
	private Properties properties;
	
	public DatabaseDefinition(String name, String url, Properties properties, LocalDateTime underMaintenanceUntil) {
		super(underMaintenanceUntil);
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
