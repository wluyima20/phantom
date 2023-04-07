/*
 * Add Copyright
 */
package com.amiyul.phantom.db;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.amiyul.phantom.api.ConnectionRequest;
import com.amiyul.phantom.api.DefaultRequest;
import com.amiyul.phantom.api.PhantomProtocol.Command;
import com.amiyul.phantom.api.RequestContext;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ FileDatabaseConfigUtils.class, FileDatabaseUtils.class })
public class FileDatabaseTest {
	
	@Mock
	private RequestContext mockContext;
	
	private FileDatabase database = FileDatabase.getInstance();
	
	@Before
	public void setup() {
		PowerMockito.mockStatic(FileDatabaseConfigUtils.class);
		PowerMockito.mockStatic(FileDatabaseUtils.class);
	}
	
	@Test
	public void process_shouldProcessAConnectionRequest() throws Exception {
		ConnectionRequest request = new ConnectionRequest(null, null);
		Mockito.when(mockContext.getRequest()).thenReturn(request);
		
		database.process(mockContext);
		
		PowerMockito.mockStatic(FileDatabaseUtils.class);
		FileDatabaseUtils.getConnection(request);
	}
	
	@Test
	public void process_shouldProcessAReloadRequest() throws Exception {
		DefaultRequest request = new DefaultRequest(Command.RELOAD, null);
		Mockito.when(mockContext.getRequest()).thenReturn(request);
		
		database.process(mockContext);
		
		PowerMockito.mockStatic(FileDatabaseUtils.class);
		FileDatabaseConfigUtils.discardConfig();
	}
	
}
