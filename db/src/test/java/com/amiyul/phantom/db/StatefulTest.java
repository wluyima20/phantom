/*
 * Add Copyright
 */
package com.amiyul.phantom.db;

import java.time.LocalDateTime;
import java.time.Month;

import org.junit.Assert;
import org.junit.Test;

public class StatefulTest {
	
	class MockStateful extends BaseStateful {
		
		MockStateful(LocalDateTime downUntil) {
			super(downUntil);
		}
	}
	
	@Test
	public void isDown_shouldReturnTrueIfTheSpecifiedDateTimeIsBeforeDownUntil() {
		LocalDateTime dateTime = LocalDateTime.of(2023, Month.MARCH, 18, 17, 4, 8, 555555555);
		LocalDateTime until = LocalDateTime.of(2023, Month.MARCH, 18, 17, 4, 8, 555555556);
		Assert.assertTrue(new MockStateful(until).isDown(dateTime));
	}
	
	@Test
	public void isDown_shouldReturnFalseIfTheSpecifiedDateTimeIsTheSameAsDownUntil() {
		LocalDateTime dateTime = LocalDateTime.of(2023, Month.MARCH, 18, 17, 4, 8, 555555555);
		LocalDateTime until = LocalDateTime.of(2023, Month.MARCH, 18, 17, 4, 8, 555555555);
		Assert.assertFalse(new MockStateful(until).isDown(dateTime));
	}
	
	@Test
	public void isDown_shouldReturnFalseIfTheSpecifiedDateTimeIsAfterDownUntil() {
		LocalDateTime dateTime = LocalDateTime.of(2023, Month.MARCH, 18, 17, 4, 8, 555555556);
		LocalDateTime until = LocalDateTime.of(2023, Month.MARCH, 18, 17, 4, 8, 555555555);
		Assert.assertFalse(new MockStateful(until).isDown(dateTime));
	}
	
	@Test
	public void isDown_shouldReturnFalseIfDownUntilIsNull() {
		Assert.assertFalse(new MockStateful(null).isDown(LocalDateTime.now()));
	}
	
}
