/*
 * Add Copyright
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
