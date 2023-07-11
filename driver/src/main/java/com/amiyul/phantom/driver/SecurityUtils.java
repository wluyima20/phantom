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

import java.security.PrivateKey;
import java.security.PublicKey;

import javax.crypto.Cipher;

class SecurityUtils {
	
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
	
}
