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
package com.amiyul.phantom.api;

import java.time.LocalDateTime;

import lombok.Getter;

/**
 * Encapsulates state information for a {@link Stateful} object
 */
public class Status {
	
	@Getter
	private LocalDateTime unavailableUntil;
	
	public Status(LocalDateTime unavailableUntil) {
		this.unavailableUntil = unavailableUntil;
	}
	
	/**
	 * Checks if this instance is unavailable as of the specified date and time.
	 *
	 * @param asOfDateTime the reference date
	 * @return true if is unavailable otherwise false
	 */
	public boolean isUnavailable(LocalDateTime asOfDateTime) {
		return Utils.isDateAfter(getUnavailableUntil(), asOfDateTime);
	}
	
}
