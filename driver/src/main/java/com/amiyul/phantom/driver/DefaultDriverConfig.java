/*
 * Add Copyright
 */
package com.amiyul.phantom.driver;

import com.amiyul.phantom.api.BaseStateful;
import com.amiyul.phantom.api.Database;
import com.amiyul.phantom.api.Status;

import lombok.Getter;

/**
 * Default {@link DriverConfig} implementation
 */
class DefaultDriverConfig extends BaseStateful implements DriverConfig {
	
	@Getter
	private Database database;
	
	DefaultDriverConfig(Database database, Status status) {
		super(status);
		this.database = database;
	}
	
}
