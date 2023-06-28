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
package com.amiyul.phantom.driver;

import static org.junit.Assert.assertEquals;

import org.junit.Assert;
import org.junit.Test;
import org.powermock.reflect.Whitebox;

public class DefaultRequestContextTest {
	
	@Test
	public void writeResult_shouldCreateTheResponseAndSetTheResultOnIt() {
		DefaultRequestContext context = new DefaultRequestContext();
		Assert.assertNull(Whitebox.getInternalState(context, "response"));
		final Object result = new Object();
		
		context.writeResult(result);
		
		Assert.assertNotNull(Whitebox.getInternalState(context, "response"));
		assertEquals(result, context.readResult());
	}
	
	@Test
	public void readResult_shouldReturnTheResultFromTheResponse() {
		DefaultRequestContext context = new DefaultRequestContext();
		final Object result = new Object();
		context.writeResult(result);
		
		assertEquals(result, context.readResult());
	}
	
	@Test
	public void readResult_shouldReturnNullIfNoResponseIsSetYet() {
		DefaultRequestContext context = new DefaultRequestContext();
		Assert.assertNull(Whitebox.getInternalState(context, "response"));
		Assert.assertNull(context.readResult());
	}
	
}
