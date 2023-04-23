/*
 * Add Copyright
 */
package com.amiyul.phantom.driver;

import java.time.LocalDateTime;

import com.amiyul.phantom.db.BaseMaintainable;
import com.amiyul.phantom.api.Database;

import lombok.Getter;

/**
 * Default {@link DriverConfig} implementation
 */
class DefaultDriverConfig extends BaseMaintainable implements DriverConfig {
	
	@Getter
	private Database database;
	
	DefaultDriverConfig(Database database, LocalDateTime underMaintenanceUntil) {
		super(underMaintenanceUntil);
		this.database = database;
	}
	
}
