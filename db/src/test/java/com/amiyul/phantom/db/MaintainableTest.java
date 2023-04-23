/*
 * Add Copyright
 */
package com.amiyul.phantom.db;

import java.time.LocalDateTime;
import java.time.Month;

import org.junit.Assert;
import org.junit.Test;

public class MaintainableTest {
	
	class MockMaintainable extends BaseMaintainable {
		
		MockMaintainable(LocalDateTime underMaintenanceUntil) {
			super(underMaintenanceUntil);
		}
	}
	
	@Test
	public void isUnderMaintenance_shouldReturnTrueIfTheSpecifiedDateTimeIsBeforeUnderMaintenanceUntil() {
		LocalDateTime dateTime = LocalDateTime.of(2023, Month.MARCH, 18, 17, 4, 8, 555555555);
		LocalDateTime until = LocalDateTime.of(2023, Month.MARCH, 18, 17, 4, 8, 555555556);
		Assert.assertTrue(new MockMaintainable(until).isUnderMaintenance(dateTime));
	}
	
	@Test
	public void isUnderMaintenance_shouldReturnFalseIfTheSpecifiedDateTimeIsTheSameAsUnderMaintenanceUntil() {
		LocalDateTime dateTime = LocalDateTime.of(2023, Month.MARCH, 18, 17, 4, 8, 555555555);
		LocalDateTime until = LocalDateTime.of(2023, Month.MARCH, 18, 17, 4, 8, 555555555);
		Assert.assertFalse(new MockMaintainable(until).isUnderMaintenance(dateTime));
	}
	
	@Test
	public void isUnderMaintenance_shouldReturnFalseIfTheSpecifiedDateTimeIsAfterUnderMaintenanceUntil() {
		LocalDateTime dateTime = LocalDateTime.of(2023, Month.MARCH, 18, 17, 4, 8, 555555556);
		LocalDateTime until = LocalDateTime.of(2023, Month.MARCH, 18, 17, 4, 8, 555555555);
		Assert.assertFalse(new MockMaintainable(until).isUnderMaintenance(dateTime));
	}
	
	@Test
	public void isUnderMaintenance_shouldReturnFalseIfUnderMaintenanceUntilIsNull() {
		Assert.assertFalse(new MockMaintainable(null).isUnderMaintenance(LocalDateTime.now()));
	}
	
}
