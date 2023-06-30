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
package com.amiyul.phantom.api.logging;

/**
 * Implementations are used to create driver loggers
 */
public interface LoggerProvider<T extends DriverLogger> {
	
	/**
	 * Gets the logger instance
	 *
	 * @return logger
	 */
	T getLogger();
	
}
