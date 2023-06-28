/*
 * Copyright [yyyy] Amiyul LLC
 * All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
