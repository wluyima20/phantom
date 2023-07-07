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

import static com.amiyul.phantom.driver.SecurityConstants.MSG_CODE_TAG_LENGTH;

import java.nio.charset.StandardCharsets;
import java.security.spec.KeySpec;
import java.time.LocalDate;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

class SecurityUtils {
	
	/*static {
		if (SecurityUtils.isLicenseExpired()) {
			try {
				DriverUtils.deregisterDriver();
			}
			catch (Throwable e) {}
		}
	}*/
	
	protected byte[] loadLicense() {
		return null;
	}
	
	protected static boolean isLicenseExpired() {
		return true;
	}
	
	protected static String encrypt(String in) {
		return in;
	}
	
	protected static String decrypt(String in) {
		return in;
	}
	
	public static SecretKey getKeyFromPassword(String password, String salt) throws Exception {
		SecretKeyFactory f = SecretKeyFactory.getInstance(SecurityConstants.MSG_CODE_KEY_ALG);
		KeySpec spec = new PBEKeySpec(password.toCharArray(), salt.getBytes(StandardCharsets.UTF_8),
		        Integer.valueOf(decrypt(SecurityConstants.MSG_CODE_KEY_ITERATION_COUNT)),
		        Integer.valueOf(decrypt(SecurityConstants.MSG_CODE_KEY_BIT_COUNT)));
		
		return new SecretKeySpec(f.generateSecret(spec).getEncoded(), decrypt(SecurityConstants.MSG_CODE_AES));
	}
	
	public static String encrypt(String in, SecretKey key, byte[] iv) throws Exception {
		Cipher c = Cipher.getInstance(getAlgorithm());
		c.init(Cipher.ENCRYPT_MODE, key, new GCMParameterSpec(Integer.valueOf(decrypt(MSG_CODE_TAG_LENGTH)), iv));
		byte[] out = c.doFinal(in.getBytes(StandardCharsets.UTF_8));
		return Base64.getEncoder().encodeToString(out);
	}
	
	public static String decrypt(String in, SecretKey key, byte[] iv) throws Exception {
		Cipher c = Cipher.getInstance(getAlgorithm());
		c.init(Cipher.DECRYPT_MODE, key, new GCMParameterSpec(Integer.valueOf(decrypt(MSG_CODE_TAG_LENGTH)), iv));
		byte[] out = c.doFinal(Base64.getDecoder().decode(in));
		return new String(out);
	}
	
	protected static LocalDate getLicenseExpiryDate() {
		return null;
	}
	
	protected static byte[] encode(String in) {
		return Base64.getEncoder().encode(in.getBytes(StandardCharsets.UTF_8));
	}
	
	protected static byte[] decode(String in) {
		return Base64.getEncoder().encode(in.getBytes(StandardCharsets.UTF_8));
	}
	
	private static String getAlgorithm() {
		return decrypt(SecurityConstants.MSG_CODE_ENC_ALG);
	}
	
}
