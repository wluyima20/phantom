/*
 * Add Copyright
 */
package com.amiyul.phantom.driver;

import java.time.LocalDateTime;

import com.amiyul.phantom.db.BaseStateful;
import com.amiyul.phantom.api.Database;

import lombok.Getter;

/**
 * Default {@link DriverConfig} implementation
 */
class DefaultDriverConfig extends BaseStateful implements DriverConfig {
	
	@Getter
	private Database database;
	
	DefaultDriverConfig(Database database, LocalDateTime downUntil) {
		super(downUntil);
		this.database = database;
	}
	
}
