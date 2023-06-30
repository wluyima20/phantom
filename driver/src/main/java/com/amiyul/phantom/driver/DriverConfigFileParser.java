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

import com.amiyul.phantom.api.config.ConfigFileParser;

/**
 * Classes that implement this interface parse the contents of a driver config file and generate a
 * {@link DriverConfigMetadata} instance
 */
public interface DriverConfigFileParser extends ConfigFileParser<DriverConfigMetadata> {}
