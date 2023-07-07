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

import static com.amiyul.phantom.driver.SecurityUtils.encode;
import static org.junit.Assert.assertEquals;

import javax.crypto.SecretKey;

import org.junit.Before;
import org.junit.Test;

public class SecurityUtilsTest {
	
	private static final String ORIGINAL_TEXT = "cool";
	
	private static final String CYPHER_TEXT = "mzx95a1NOSJsOfIl0fbbxD0iE9g=";
	
	private static final String PASSWORD = "dev";
	
	private static final String SALT = "test";
	
	private static final String BASE64_IV = "GfyVOSa882o26cBO";
	
	private static SecretKey key;
	
	@Before
	public void setup() throws Exception {
		key = SecurityUtils.getKeyFromPassword(PASSWORD, SALT);
	}
	
	@Test
	public void encrypt_shouldEncryptTheString() throws Exception {
		assertEquals(CYPHER_TEXT, SecurityUtils.encrypt(ORIGINAL_TEXT, key, encode(BASE64_IV)));
		
	}
	
	@Test
	public void decrypt_shouldDecryptTheString() throws Exception {
		assertEquals(ORIGINAL_TEXT, SecurityUtils.decrypt(CYPHER_TEXT, key, encode(BASE64_IV)));
		
	}
	
}
