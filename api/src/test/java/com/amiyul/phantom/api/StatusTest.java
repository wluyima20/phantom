/*
 * Add Copyright
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
