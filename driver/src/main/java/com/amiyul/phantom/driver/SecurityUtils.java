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
import java.nio.file.Files;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.time.LocalDate;
import java.util.List;
import java.util.Properties;

import javax.crypto.Cipher;

class SecurityUtils {
	
	private static PublicKey publicKey;
	
	/*static {
		if (SecurityUtils.isLicenseExpired()) {
			try {
				DriverUtils.deregisterDriver();
			}
			catch (Throwable e) {}
		}
	}*/
	
	protected static String encrypt(String in) {
		return in;
	}
	
	protected static String decrypt(String in) {
		return in;
	}
	
	/**
	 * Encrypts the specified bytes using the specified {@link PrivateKey}
	 * 
	 * @param in the bytes to encrypt
	 * @param key PrivateKey object
	 * @return encrypted bytes
	 * @throws Exception
	 */
	public static byte[] encrypt(byte[] in, PrivateKey key) throws Exception {
		Cipher c = Cipher.getInstance(ALG);
		c.init(Cipher.ENCRYPT_MODE, key);
		return c.doFinal(in);
	}
	
	/**
	 * Decrypts the specified bytes using the specified {@link PublicKey}
	 * 
	 * @param in the bytes to decrypt
	 * @param key PublicKey object
	 * @return decrypted bytes
	 * @throws Exception
	 */
	public static byte[] decrypt(byte[] in, PublicKey key) throws Exception {
		Cipher c = Cipher.getInstance(ALG);
		c.init(Cipher.DECRYPT_MODE, key);
		return c.doFinal(in);
	}
	
	protected static boolean isLicenseExpired() throws Exception {
		List<String> contents = Files.readAllLines(DriverConfigUtils.getConfig().getLicenseFile().toPath());
		byte[] publicKeyBytes = contents.get(0).getBytes(UTF_8);
		publicKey = getInstance(ALG).generatePublic(new X509EncodedKeySpec(getDecoder().decode(publicKeyBytes)));
		Cipher c = Cipher.getInstance(ALG);
		c.init(Cipher.DECRYPT_MODE, publicKey);
		Properties props = new Properties();
		props.load(new ByteArrayInputStream(c.doFinal(getDecoder().decode(contents.get(1).getBytes(UTF_8)))));
		return LocalDate.now().isAfter(LocalDate.parse(props.getProperty(LICENSE_PROP_EXP_DATE), ISO_LOCAL_DATE));
	}
	
}
