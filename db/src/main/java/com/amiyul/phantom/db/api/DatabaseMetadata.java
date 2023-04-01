/*
 * Add Copyright
 */
package com.amiyul.phantom.db.api;

import java.util.Properties;

import com.amiyul.phantom.api.Disableable;
import com.amiyul.phantom.api.Named;

import lombok.Getter;
import lombok.Setter;

/**
 * Encapsulates metadata for a single target database instance
 */
public class DatabaseMetadata implements Named, Disableable {
	
	private String name;
	
	@Getter
	@Setter
	private String url;
	
	@Getter
	@Setter
	private Properties properties;
	
	public DatabaseMetadata(String name, String url, Properties properties) {
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
