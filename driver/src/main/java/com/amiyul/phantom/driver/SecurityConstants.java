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

final class SecurityConstants {
	
	//TODO encrypt all values
	protected static final String MSG_CODE_AES = "AES";
	
	protected static final String MSG_CODE_ENC_ALG = MSG_CODE_AES + "/GCM/NoPadding";
	
	protected static final String MSG_CODE_TAG_LENGTH = "128";
	
	protected static final String MSG_CODE_KEY_ITERATION_COUNT = "65536";
	
	protected static final String MSG_CODE_KEY_BIT_COUNT = "256";
	
	protected static final String MSG_CODE_KEY_ALG = "PBKDF2WithHmacSHA" + MSG_CODE_KEY_BIT_COUNT;
	
	protected static final String MSG_CODE_MISSING_LICENSE_PATH = "Path to license file is not specified";
	
	protected static final String MSG_CODE_LICENSE_EXPIRED = "License expired";
	
	protected static final String PROP_LICENSE_PATH = "license.file.path";
	
}
