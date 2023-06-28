/*
 * Copyright [yyyy] Amiyul LLC
 * All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.amiyul.phantom.driver;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.DriverPropertyInfo;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Logger;

import com.amiyul.phantom.api.Utils;
import com.amiyul.phantom.api.logging.JavaLogger;
import com.amiyul.phantom.api.logging.LoggerUtils;

public final class PhantomDriver implements Driver {
	
	private static Integer majorVersion;
	
	private static Integer minorVersion;
	
	static {
		try {
			DriverManager.registerDriver(new PhantomDriver());
			Runtime.getRuntime().addShutdownHook(new Thread(ShutdownHook.getInstance()));
		}
		catch (SQLException e) {
			throw new RuntimeException("Failed to register driver");
		}
	}
	
	@Override
	public Connection connect(String url, Properties info) throws SQLException {
		if (!acceptsURL(url)) {
			//This is required as per the jdbc spec if a driver realizes it isn't appropriate for the url
			return null;
		}
		
		try {
			return DriverUtils.connect(url, info);
		}
		catch (Exception e) {
			throw new SQLException(e);
		}
	}
	
	@Override
	public boolean acceptsURL(String url) {
		if (Utils.isBlank(url)) {
			return false;
		}
		
		return url.startsWith(DriverConstants.URL_PREFIX);
	}
	
	@Override
	public DriverPropertyInfo[] getPropertyInfo(String url, Properties info) {
		Map<DriverProperty, String> propValueMap = DriverUtils.createDriverPropertyAndValueMap(url, info);
		
		return propValueMap.entrySet().stream().map(e -> {
			DriverPropertyInfo propInfo = e.getKey().toDriverPropertyInfo();
			propInfo.value = e.getValue();
			return propInfo;
		}).toArray(DriverPropertyInfo[]::new);
	}
	
	@Override
	public int getMajorVersion() {
		if (majorVersion == null) {
			majorVersion = Integer.valueOf(DriverUtils.getVersion().split("\\.")[0]);
		}
		
		return majorVersion;
	}
	
	@Override
	public int getMinorVersion() {
		if (minorVersion == null) {
			minorVersion = Integer.valueOf(DriverUtils.getVersion().split("\\.")[1]);
		}
		
		return minorVersion;
	}
	
	@Override
	public boolean jdbcCompliant() {
		return false;
	}
	
	@Override
	public Logger getParentLogger() throws SQLFeatureNotSupportedException {
		if (LoggerUtils.isUsingJavaLogger()) {
			return JavaLogger.getInstance().getNativeLogger();
		}
		
		throw new SQLFeatureNotSupportedException();
	}
	
}
