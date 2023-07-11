/*
 * Copyright (C) Amiyul LLC - All Rights Reserved
 *
 * This source code is protected under international copyright law. All rights
 * reserved and protected by the copyright holder.
 * 
 * This file is confidential and only available to authorized individuals with the
 * permission of the copyright holder. If you encounter this file and do not have
 * permission, please contact the copyright holder and delete this file.
 */
package com.amiyul.phantom.driver;

import static com.amiyul.phantom.driver.SecurityConstants.ALG;
import static com.amiyul.phantom.driver.SecurityConstants.LICENSE_PROP_EXP_DATE;
import static java.nio.charset.StandardCharsets.UTF_8;
import static java.security.KeyFactory.getInstance;
import static java.time.format.DateTimeFormatter.ISO_LOCAL_DATE;
import static java.util.Base64.getDecoder;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.nio.file.Files;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;
import java.util.Properties;

import javax.crypto.Cipher;

import com.amiyul.phantom.api.Constants;
import com.amiyul.phantom.api.Database;
import com.amiyul.phantom.api.DatabaseProvider;
import com.amiyul.phantom.api.Status;
import com.amiyul.phantom.api.Utils;
import com.amiyul.phantom.api.logging.LoggerUtils;
import com.amiyul.phantom.db.FileDatabaseProvider;

/**
 * Contains driver config utilities
 */
public class DriverConfigUtils {
	
	public static final String PROP_CONFIG_LOCATION = Constants.PROJECT_NAME.toUpperCase(Locale.ENGLISH)
	        + "_DRIVER_CONFIG_LOCATION";
	
	private static String configFilePath;
	
	private static DriverConfigFileParser parser;
	
	private static DriverConfigMetadata configMetadata;
	
	private static DriverConfig config;
	
	static {
		try {
			List<String> contents = Files.readAllLines(DriverConfigUtils.getConfig().getLicenseFile().toPath());
			byte[] keyBytes = contents.get(0).getBytes(UTF_8);
			PublicKey key = getInstance(ALG).generatePublic(new X509EncodedKeySpec(getDecoder().decode(keyBytes)));
			Cipher c = Cipher.getInstance(ALG);
			c.init(Cipher.DECRYPT_MODE, key);
			Properties props = new Properties();
			props.load(new ByteArrayInputStream(c.doFinal(getDecoder().decode(contents.get(1).getBytes(UTF_8)))));
			//TODO load messages
			if (!LocalDate.now().isAfter(LocalDate.parse(props.getProperty(LICENSE_PROP_EXP_DATE), ISO_LOCAL_DATE))) {
				LoggerUtils.error(SecurityConstants.MSG_CODE_LICENSE_EXPIRED, null);
				throw new RuntimeException();
			}
		}
		catch (Throwable e) {
			try {
				DriverManager.deregisterDriver(PhantomDriver.DRIVER);
			}
			catch (SQLException ex) {
				//Ignore
			}
			
			LoggerUtils.error(SecurityConstants.MSG_CODE_REGISTER_DRIVER_FAIL, null);
		}
	}
	
	/**
	 * Gets {@link DriverConfigFileParser} via the service loader mechanism that can parse the specified
	 * driver config file.
	 *
	 * @param configFile the driver config file
	 * @return DriverConfigFileParser instance
	 */
	protected synchronized static DriverConfigFileParser getParser(File configFile) {
		if (parser == null) {
			parser = Utils.getParser(DriverConfigFileParser.class, configFile);
			if (parser == null) {
				throw new RuntimeException("No appropriate parser found for specified driver config file");
			}
			
			LoggerUtils.debug("Found driver config file parser");
		}
		
		return parser;
	}
	
	/**
	 * Creates a {@link DriverConfigMetadata} instance
	 *
	 * @param dbProviderClassName the database provider class name
	 * @param unavailableUntil time when the database will be available
	 * @param licenseFilePath the path to the license file
	 * @return ConfigMetadata object
	 * @throws Exception
	 */
	protected static DriverConfigMetadata createMetadata(String dbProviderClassName, String unavailableUntil,
	                                                     String licenseFilePath) {
		return new DriverConfigMetadata() {
			
			@Override
			public String getDatabaseProviderClassName() {
				return dbProviderClassName;
			}
			
			@Override
			public String getUnavailableUntil() {
				return unavailableUntil;
			}
			
			@Override
			public String getLicenseFilePath() {
				return licenseFilePath;
			}
			
		};
	}
	
	/**
	 * Gets the {@link DriverConfig} instance
	 *
	 * @return DriverConfig
	 */
	protected synchronized static DriverConfig getConfig() {
		if (config == null) {
			File licenseFile;
			DatabaseProvider<Database> provider;
			LocalDateTime unavailableUntil = null;
			
			try {
				DriverConfigMetadata metadata = getConfigMetadata();
				licenseFile = new File(metadata.getLicenseFilePath());
				Class<? extends DatabaseProvider> clazz = null;
				if (!Utils.isBlank(metadata.getDatabaseProviderClassName())) {
					clazz = Utils.loadClass(metadata.getDatabaseProviderClassName());
				}
				
				if (!Utils.isBlank(metadata.getUnavailableUntil())) {
					unavailableUntil = Utils.parseDateString(metadata.getUnavailableUntil());
				}
				
				if (clazz == null) {
					clazz = FileDatabaseProvider.class;
					LoggerUtils.debug("No configured database provider, defaulting to file database provider");
				}
				
				provider = clazz.newInstance();
			}
			catch (Exception e) {
				throw new RuntimeException(e);
			}
			
			config = new DefaultDriverConfig(licenseFile, provider.get(), new Status(unavailableUntil));
		}
		
		return config;
	}
	
	/**
	 * Reloads the driver and database configurations
	 * 
	 * @throws SQLException
	 */
	protected static void reloadConfig() throws SQLException {
		LoggerUtils.debug("Discarding driver config");
		
		configMetadata = null;
		config = null;
		DefaultClient.getInstance().reload();
	}
	
	/**
	 * Gets the path to the driver config file
	 *
	 * @return config file path
	 */
	protected synchronized static String getConfigFile() {
		if (configFilePath == null) {
			configFilePath = Utils.getFilePath(PROP_CONFIG_LOCATION);
			
			if (!Utils.isBlank(configFilePath)) {
				LoggerUtils.debug("Using driver config file located at -> " + configFilePath);
			} else {
				LoggerUtils.debug("Found no defined location for the driver config file");
			}
		}
		
		return configFilePath;
	}
	
	/**
	 * Gets the {@link DriverConfigMetadata} instance
	 *
	 * @return DriverConfigMetadata
	 * @throws Exception
	 */
	protected synchronized static DriverConfigMetadata getConfigMetadata() throws Exception {
		if (configMetadata == null) {
			LoggerUtils.debug("Loading " + Constants.PROJECT_NAME + " driver configuration");
			
			String filePath = getConfigFile();
			if (Utils.isBlank(filePath)) {
				throw new RuntimeException("No driver config file specified");
			}
			
			File configFile = new File(filePath);
			configMetadata = getParser(configFile).parse(Utils.getInputStream(configFile));
		}
		
		return configMetadata;
	}
	
}
