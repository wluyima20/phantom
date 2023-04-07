/*
 * Add Copyright
 */
package com.amiyul.phantom.driver;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.stubbing.Answer;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.amiyul.phantom.api.ConnectionRequest;
import com.amiyul.phantom.api.Database;
import com.amiyul.phantom.api.PhantomProtocol.Command;
import com.amiyul.phantom.api.RequestContext;
import com.amiyul.phantom.driver.DefaultClient.DefaultRequestContext;

@RunWith(PowerMockRunner.class)
@PrepareForTest(DriverConfigUtils.class)
public class DefaultClientTest {
	
	private DefaultClient client;
	
	@Before
	public void setup() {
		PowerMockito.mockStatic(DriverConfigUtils.class);
		client = DefaultClient.getInstance();
	}
	
	@Test
	public void connect_shouldSendAConnectionRequest() throws Exception {
		final String dbName = "db";
		client = Mockito.spy(client);
		List<RequestContext> contexts = new ArrayList();
		Connection mockConnection = Mockito.mock(Connection.class);
		Mockito.doAnswer((Answer<Object>) invocation -> {
			RequestContext context = invocation.getArgument(0);
			context.writeResult(mockConnection);
			contexts.add(context);
			return null;
		}).when(client).sendRequest(ArgumentMatchers.any(DefaultRequestContext.class));
		
		Assert.assertEquals(mockConnection, client.connect(dbName));
		
		Assert.assertEquals(1, contexts.size());
		Assert.assertEquals(dbName, ((ConnectionRequest) contexts.get(0).getRequest()).getTargetDatabaseName());
	}
	
	@Test
	public void connect_shouldReturnNullIfNoResponseIsPresentOnTheContext() throws Exception {
		final String dbName = "db";
		client = Mockito.spy(client);
		List<RequestContext> contexts = new ArrayList();
		Mockito.doAnswer((Answer<Object>) invocation -> {
			contexts.add(invocation.getArgument(0));
			return null;
		}).when(client).sendRequest(ArgumentMatchers.any(DefaultRequestContext.class));
		
		Assert.assertNull(client.connect(dbName));
		
		Assert.assertEquals(1, contexts.size());
		Assert.assertEquals(dbName, ((ConnectionRequest) contexts.get(0).getRequest()).getTargetDatabaseName());
	}
	
	@Test
	public void reload_shouldSendAReloadRequest() throws Exception {
		client = Mockito.spy(client);
		List<RequestContext> contexts = new ArrayList();
		Mockito.doAnswer((Answer<Object>) invocation -> {
			contexts.add(invocation.getArgument(0));
			return null;
		}).when(client).sendRequest(ArgumentMatchers.any(DefaultRequestContext.class));
		
		client.reload();
		
		Assert.assertEquals(1, contexts.size());
		Assert.assertEquals(Command.RELOAD, contexts.get(0).getRequest().getCommand());
	}
	
	@Test
	public void sendRequest_shouldSendATheRequestToTheDatabase() throws Exception {
		DriverConfig mockConfig = Mockito.mock(DriverConfig.class);
		RequestContext mockContext = Mockito.mock(RequestContext.class);
		Database mockDb = Mockito.mock(Database.class);
		Mockito.when(DriverConfigUtils.getConfig()).thenReturn(mockConfig);
		Mockito.when(mockConfig.getDatabase()).thenReturn(mockDb);
		
		client.sendRequest(mockContext);
		
		Mockito.verify(mockDb).process(mockContext);
	}
	
}
