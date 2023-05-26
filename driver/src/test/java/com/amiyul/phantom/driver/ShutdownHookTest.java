/*
 * Add Copyright
 */
package com.amiyul.phantom.driver;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ DefaultClient.class })
public class ShutdownHookTest {
	
	@Mock
	private DefaultClient mockClient;
	
	@Test
	public void run_shouldShutdownTheClient() {
		PowerMockito.mockStatic(DefaultClient.class);
		Mockito.when(DefaultClient.getInstance()).thenReturn(mockClient);
		ShutdownHook.getInstance().run();
		
		Mockito.verify(mockClient).shutdown();
	}
	
}
