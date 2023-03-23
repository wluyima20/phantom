/*
 * Add Copyright
 */
package com.amiyul.phantom;

import java.time.LocalDateTime;
import java.time.Month;

import com.amiyul.phantom.api.Disableable;
import org.junit.Assert;
import org.junit.Test;

public class DisableableTest {
	
	class MockDisableable implements Disableable {
		
		LocalDateTime disabledUntil;
		
		MockDisableable(LocalDateTime disabledUntil) {
			this.disabledUntil = disabledUntil;
		}
		
		@Override
		public LocalDateTime getDisabledUntil() {
			return disabledUntil;
		}
		
		@Override
		public void setDisabledUntil(LocalDateTime disabledUntil) {
			this.disabledUntil = disabledUntil;
		}
	}
	
	@Test
	public void isDisabledAsOf_shouldReturnTrueIfTheSpecifiedDateTimeIsBeforeDisabledUntil() {
		LocalDateTime dateTime = LocalDateTime.of(2023, Month.MARCH, 18, 17, 4, 8, 555555555);
		LocalDateTime until = LocalDateTime.of(2023, Month.MARCH, 18, 17, 4, 8, 555555556);
		Assert.assertTrue(new MockDisableable(until).isDisabledAsOf(dateTime));
	}
	
	@Test
	public void isDisabledAsOf_shouldReturnFalseIfTheSpecifiedDateTimeIsTheSameAsDisabledUntil() {
		LocalDateTime dateTime = LocalDateTime.of(2023, Month.MARCH, 18, 17, 4, 8, 555555555);
		LocalDateTime until = LocalDateTime.of(2023, Month.MARCH, 18, 17, 4, 8, 555555555);
		Assert.assertFalse(new MockDisableable(until).isDisabledAsOf(dateTime));
	}
	
	@Test
	public void isDisabledAsOf_shouldReturnFalseIfTheSpecifiedDateTimeIsAfterDisabledUntil() {
		LocalDateTime dateTime = LocalDateTime.of(2023, Month.MARCH, 18, 17, 4, 8, 555555556);
		LocalDateTime until = LocalDateTime.of(2023, Month.MARCH, 18, 17, 4, 8, 555555555);
		Assert.assertFalse(new MockDisableable(until).isDisabledAsOf(dateTime));
	}
	
	@Test
	public void isDisabledAsOf_shouldReturnFalseIfDisabledUntilIsNull() {
		Assert.assertFalse(new MockDisableable(null).isDisabledAsOf(null));
	}
	
}
