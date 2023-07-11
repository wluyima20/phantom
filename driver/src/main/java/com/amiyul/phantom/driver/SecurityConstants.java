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

import static com.amiyul.phantom.api.Constants.PROJECT_NAME;

final class SecurityConstants {
	
	protected static final String ALG = "RSA";
	
	//TODO encrypt all values
	protected static final String MSG_CODE_MISSING_LICENSE_PATH = "Path to license file is not specified";
	
	protected static final String MSG_CODE_REGISTER_DRIVER_FAIL = "Failed to register " + PROJECT_NAME
	        + " driver, make sure your license has not expired";
	
	protected static final String PROP_LICENSE_PATH = "license.file.path";
	
	protected static final String LICENSE_PROP_EXP_DATE = "expiry.date";
	
}
