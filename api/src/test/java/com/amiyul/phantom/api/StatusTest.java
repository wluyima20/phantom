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
import java.time.Month;

import org.junit.Assert;
import org.junit.Test;

public class StatusTest {
	
	@Test
	public void isUnavailable_shouldReturnTrueIfTheSpecifiedDateTimeIsBeforeUnavailableUntil() {
		LocalDateTime dateTime = LocalDateTime.of(2023, Month.MARCH, 18, 17, 4, 8, 555555555);
		LocalDateTime until = LocalDateTime.of(2023, Month.MARCH, 18, 17, 4, 8, 555555556);
		Assert.assertTrue(new Status(until).isUnavailable(dateTime));
	}
	
	@Test
	public void isUnavailable_shouldReturnFalseIfTheSpecifiedDateTimeIsTheSameAsUnavailableUntil() {
		LocalDateTime dateTime = LocalDateTime.of(2023, Month.MARCH, 18, 17, 4, 8, 555555555);
		LocalDateTime until = LocalDateTime.of(2023, Month.MARCH, 18, 17, 4, 8, 555555555);
		Assert.assertFalse(new Status(until).isUnavailable(dateTime));
	}
	
	@Test
	public void isUnavailable_shouldReturnFalseIfTheSpecifiedDateTimeIsAfterUnavailableUntil() {
		LocalDateTime dateTime = LocalDateTime.of(2023, Month.MARCH, 18, 17, 4, 8, 555555556);
		LocalDateTime until = LocalDateTime.of(2023, Month.MARCH, 18, 17, 4, 8, 555555555);
		Assert.assertFalse(new Status(until).isUnavailable(dateTime));
	}
	
	@Test
	public void isUnavailable_shouldReturnFalseIfUnavailableUntilIsNull() {
		Assert.assertFalse(new Status(null).isUnavailable(LocalDateTime.now()));
	}
	
}
