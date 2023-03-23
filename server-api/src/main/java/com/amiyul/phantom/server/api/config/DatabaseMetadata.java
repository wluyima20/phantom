/*
 * Add Copyright
 */
package com.amiyul.phantom.server.api.config;

import java.time.LocalDateTime;
import java.util.Properties;

import com.amiyul.phantom.api.Disableable;
import com.amiyul.phantom.api.Named;

import lombok.Getter;
import lombok.Setter;

/**
 * Encapsulates metadata for a single database instance
 */
@Getter
@Setter
public class DatabaseMetadata implements Named, Disableable {
	
	private String name;
	
	private String url;
	
	private LocalDateTime disabledUntil;
	
	private Properties properties;
	
	private DatabaseMetadataProvider provider;
	
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
	
	/**
	 * @see Disableable#getDisabledUntil()
	 */
	@Override
	public LocalDateTime getDisabledUntil() {
		return disabledUntil;
	}
	
	/**
	 * @see Disableable#setDisabledUntil(LocalDateTime)
	 */
	@Override
	public void setDisabledUntil(LocalDateTime disabledUntil) {
		this.disabledUntil = disabledUntil;
	}
	
}
