/*
 * Add Copyright
 */
package com.amiyul.phantom.driver;

import static java.time.LocalDateTime.of;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.verifyStatic;

import java.sql.Connection;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.stubbing.Answer;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

import com.amiyul.phantom.api.Database;
import com.amiyul.phantom.api.PhantomProtocol.Command;
import com.amiyul.phantom.api.RequestContext;
import com.amiyul.phantom.api.Status;
import com.amiyul.phantom.api.Utils;
import com.amiyul.phantom.api.logging.LoggerUtils;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ DriverConfigUtils.class, DefaultClient.class, Utils.class, LoggerUtils.class, DelayedConnectTask.class,
        Executors.class, DriverUtils.class })
public class DefaultClientTest {
	
	@Mock
	private Connection mockConnection;
	
	@Mock
	private Status mockDbStatus;
	
	@Mock
	private Status mockTargetDbStatus;
	
	@Mock
	private DriverConfig mockConfig;
	
	@Mock
	private ScheduledExecutorService mockExecutor;
	
	@Mock
	private DelayedConnectTask mockTask;
	
	@Mock
	private ScheduledFuture mockFuture;
	
	private DefaultClient client = DefaultClient.getInstance();
	
	@Before
	public void setup() {
		PowerMockito.mockStatic(DriverConfigUtils.class);
		PowerMockito.mockStatic(Utils.class);
		PowerMockito.mockStatic(LoggerUtils.class);
		PowerMockito.mockStatic(DelayedConnectTask.class);
		PowerMockito.mockStatic(Executors.class);
		PowerMockito.mockStatic(DriverUtils.class);
		Whitebox.setInternalState(DefaultClient.class, "delayedExecutor", mockExecutor);
		PowerMockito.spy(DefaultClient.class);
		when(DriverConfigUtils.getConfig()).thenReturn(mockConfig);
	}
	
	@After
	public void tearDown() {
		Whitebox.setInternalState(DefaultClient.class, "delayedExecutor", (Object) null);
	}
	
	@Test
	public void connect_shouldSendAConnectionRequest() throws Exception {
		final String dbName = "db";
		ConnectionRequestData requestData = new ConnectionRequestData(dbName, false, null);
		client = Mockito.spy(client);
		when(mockConfig.getStatus()).thenReturn(mockDbStatus);
		doReturn(mockConnection).when(client).doConnect(requestData);
		doReturn(mockTargetDbStatus).when(client).getStatus(dbName);
		
		assertEquals(mockConnection, client.connect(requestData));
	}
	
	@Test
	public void connect_shouldReloadConfigsIfPhantomDbIsMarkedAsUnavailableAndThenConnect() throws Exception {
		final String dbName = "db";
		ConnectionRequestData requestData = new ConnectionRequestData(dbName, false, null);
		client = Mockito.spy(client);
		when(DefaultClient.getInstance()).thenReturn(client);
		when(mockConfig.getStatus()).thenReturn(mockDbStatus);
		when(mockDbStatus.isUnavailable(any(LocalDateTime.class))).thenReturn(true).thenReturn(false);
		doReturn(mockConnection).when(client).doConnect(requestData);
		doReturn(mockTargetDbStatus).when(client).getStatus(dbName);
		doNothing().when(client).reload();
		
		assertEquals(mockConnection, client.connect(requestData));
		
		verify(client).reload();
		verifyStatic(DriverConfigUtils.class, times(2));
		DriverConfigUtils.getConfig();
	}
	
	@Test
	public void connect_shouldReloadConfigsIfTheTargetDbIsMarkedAsUnavailableAndThenConnect() throws Exception {
		final String dbName = "db";
		ConnectionRequestData requestData = new ConnectionRequestData(dbName, false, null);
		client = Mockito.spy(client);
		when(DefaultClient.getInstance()).thenReturn(client);
		when(mockConfig.getStatus()).thenReturn(mockDbStatus);
		LocalDateTime unavailableUntil = LocalDateTime.now();
		when(mockTargetDbStatus.getUnavailableUntil()).thenReturn(unavailableUntil);
		when(mockConfig.getStatus()).thenReturn(mockDbStatus);
		when(Utils.isDateAfter(eq(unavailableUntil), any(LocalDateTime.class))).thenReturn(true).thenReturn(false);
		doReturn(mockConnection).when(client).doConnect(requestData);
		doReturn(mockTargetDbStatus).when(client).getStatus(dbName);
		doNothing().when(client).reload();
		
		assertEquals(mockConnection, client.connect(requestData));
		
		verify(client).reload();
		verifyStatic(DriverConfigUtils.class, times(2));
		DriverConfigUtils.getConfig();
	}
	
	@Test
	public void connect_shouldScheduleToConnectWhenPhantomDbBecomesAvailable() throws Exception {
		final String dbName = "db";
		ConnectionRequestData requestData = new ConnectionRequestData(dbName, false, null);
		client = Mockito.spy(client);
		when(DefaultClient.getInstance()).thenReturn(client);
		when(mockConfig.getStatus()).thenReturn(mockDbStatus);
		LocalDateTime unavailableUntil = LocalDateTime.now();
		when(mockDbStatus.isUnavailable(any(LocalDateTime.class))).thenReturn(true).thenReturn(true);
		when(mockDbStatus.getUnavailableUntil()).thenReturn(unavailableUntil);
		doReturn(mockConnection).when(client).doConnect(requestData);
		doReturn(mockTargetDbStatus).when(client).getStatus(dbName);
		doNothing().when(client).reload();
		final long delay = 1;
		Duration duration = Duration.ofSeconds(delay);
		when(Utils.getDurationBetween(any(LocalDateTime.class), eq(unavailableUntil))).thenReturn(duration);
		PowerMockito.whenNew(DelayedConnectTask.class).withArguments(requestData).thenReturn(mockTask);
		when(mockExecutor.schedule(mockTask, delay, TimeUnit.SECONDS)).thenReturn(mockFuture);
		when(mockFuture.get()).thenReturn(mockConnection);
		
		assertEquals(mockConnection, client.connect(requestData));
		
		verify(client).reload();
		verifyStatic(DriverConfigUtils.class, times(2));
		DriverConfigUtils.getConfig();
	}
	
	@Test
	public void connect_shouldScheduleToConnectWhenTheTargetDbBecomesAvailable() throws Exception {
		final String dbName = "db";
		ConnectionRequestData requestData = new ConnectionRequestData(dbName, false, null);
		client = Mockito.spy(client);
		when(DefaultClient.getInstance()).thenReturn(client);
		when(mockConfig.getStatus()).thenReturn(mockDbStatus);
		LocalDateTime unavailableUntil = LocalDateTime.now();
		when(mockTargetDbStatus.getUnavailableUntil()).thenReturn(unavailableUntil);
		when(Utils.isDateAfter(eq(unavailableUntil), any(LocalDateTime.class))).thenReturn(true).thenReturn(true);
		doReturn(mockConnection).when(client).doConnect(requestData);
		doReturn(mockTargetDbStatus).when(client).getStatus(dbName);
		doNothing().when(client).reload();
		final long delay = 1;
		Duration duration = Duration.ofSeconds(delay);
		when(Utils.getDurationBetween(any(LocalDateTime.class), eq(unavailableUntil))).thenReturn(duration);
		PowerMockito.whenNew(DelayedConnectTask.class).withArguments(requestData).thenReturn(mockTask);
		when(mockExecutor.schedule(mockTask, delay, TimeUnit.SECONDS)).thenReturn(mockFuture);
		when(mockFuture.get()).thenReturn(mockConnection);
		
		assertEquals(mockConnection, client.connect(requestData));
		
		verify(client).reload();
		verifyStatic(DriverConfigUtils.class, times(2));
		DriverConfigUtils.getConfig();
	}
	
	@Test
	public void connect_shouldWaitForPhantomDbToBecomeAvailableIfDelayIsAfterThatOfTheTargetDb() throws Exception {
		final String dbName = "db";
		ConnectionRequestData requestData = new ConnectionRequestData(dbName, false, null);
		client = Mockito.spy(client);
		when(DefaultClient.getInstance()).thenReturn(client);
		when(mockConfig.getStatus()).thenReturn(mockDbStatus);
		LocalDateTime dbUnavailableUntil = of(2023, 5, 21, 12, 5, 6);
		when(mockDbStatus.isUnavailable(any(LocalDateTime.class))).thenReturn(true).thenReturn(true);
		when(mockDbStatus.getUnavailableUntil()).thenReturn(dbUnavailableUntil);
		LocalDateTime targetUnavailableUntil = of(2023, 5, 21, 12, 5, 5);
		;
		when(mockTargetDbStatus.getUnavailableUntil()).thenReturn(targetUnavailableUntil);
		when(Utils.isDateAfter(eq(targetUnavailableUntil), any(LocalDateTime.class))).thenReturn(true).thenReturn(true);
		when(Utils.isDateAfter(targetUnavailableUntil, dbUnavailableUntil)).thenReturn(false);
		doReturn(mockConnection).when(client).doConnect(requestData);
		doReturn(mockTargetDbStatus).when(client).getStatus(dbName);
		doNothing().when(client).reload();
		final long dbDelay = 2;
		Duration dbDuration = Duration.ofSeconds(dbDelay);
		when(Utils.getDurationBetween(any(LocalDateTime.class), eq(dbUnavailableUntil))).thenReturn(dbDuration);
		Duration targetDuration = Duration.ofSeconds(1);
		when(Utils.getDurationBetween(any(LocalDateTime.class), eq(targetUnavailableUntil))).thenReturn(targetDuration);
		PowerMockito.whenNew(DelayedConnectTask.class).withArguments(requestData).thenReturn(mockTask);
		when(mockExecutor.schedule(mockTask, dbDelay, TimeUnit.SECONDS)).thenReturn(mockFuture);
		when(mockFuture.get()).thenReturn(mockConnection);
		
		assertEquals(mockConnection, client.connect(requestData));
		
		verify(client).reload();
		verifyStatic(DriverConfigUtils.class, times(2));
		DriverConfigUtils.getConfig();
	}
	
	@Test
	public void connect_shouldWaitForTargetDbToBecomeAvailableIfDelayIsAfterThatOfPhantomDb() throws Exception {
		final String dbName = "db";
		ConnectionRequestData requestData = new ConnectionRequestData(dbName, false, null);
		client = Mockito.spy(client);
		when(DefaultClient.getInstance()).thenReturn(client);
		when(mockConfig.getStatus()).thenReturn(mockDbStatus);
		LocalDateTime dbUnavailableUntil = of(2023, 5, 21, 12, 5, 5);
		when(mockDbStatus.isUnavailable(any(LocalDateTime.class))).thenReturn(true).thenReturn(true);
		when(mockDbStatus.getUnavailableUntil()).thenReturn(dbUnavailableUntil);
		LocalDateTime targetUnavailableUntil = of(2023, 5, 21, 12, 5, 6);
		when(mockTargetDbStatus.getUnavailableUntil()).thenReturn(targetUnavailableUntil);
		when(Utils.isDateAfter(eq(targetUnavailableUntil), any(LocalDateTime.class))).thenReturn(true).thenReturn(true);
		when(Utils.isDateAfter(targetUnavailableUntil, dbUnavailableUntil)).thenReturn(true);
		doReturn(mockConnection).when(client).doConnect(requestData);
		doReturn(mockTargetDbStatus).when(client).getStatus(dbName);
		doNothing().when(client).reload();
		Duration dbDuration = Duration.ofSeconds(1);
		when(Utils.getDurationBetween(any(LocalDateTime.class), eq(dbUnavailableUntil))).thenReturn(dbDuration);
		final long targetDelay = 2;
		Duration targetDuration = Duration.ofSeconds(targetDelay);
		when(Utils.getDurationBetween(any(LocalDateTime.class), eq(targetUnavailableUntil))).thenReturn(targetDuration);
		PowerMockito.whenNew(DelayedConnectTask.class).withArguments(requestData).thenReturn(mockTask);
		when(mockExecutor.schedule(mockTask, targetDelay, TimeUnit.SECONDS)).thenReturn(mockFuture);
		when(mockFuture.get()).thenReturn(mockConnection);
		
		assertEquals(mockConnection, client.connect(requestData));
		
		verify(client).reload();
		verifyStatic(DriverConfigUtils.class, times(2));
		DriverConfigUtils.getConfig();
	}
	
	@Test
	public void connect_shouldCreateAndSetExecutorIfNecessary() throws Exception {
		final String dbName = "db";
		ConnectionRequestData requestData = new ConnectionRequestData(dbName, false, null);
		client = Mockito.spy(client);
		when(DefaultClient.getInstance()).thenReturn(client);
		when(mockConfig.getStatus()).thenReturn(mockDbStatus);
		LocalDateTime unavailableUntil = LocalDateTime.now();
		when(mockDbStatus.isUnavailable(any(LocalDateTime.class))).thenReturn(true).thenReturn(true);
		when(mockDbStatus.getUnavailableUntil()).thenReturn(unavailableUntil);
		doReturn(mockConnection).when(client).doConnect(requestData);
		doReturn(mockTargetDbStatus).when(client).getStatus(dbName);
		doNothing().when(client).reload();
		final long delay = 1;
		Duration duration = Duration.ofSeconds(delay);
		when(Utils.getDurationBetween(any(LocalDateTime.class), eq(unavailableUntil))).thenReturn(duration);
		PowerMockito.whenNew(DelayedConnectTask.class).withArguments(requestData).thenReturn(mockTask);
		when(mockExecutor.schedule(mockTask, delay, TimeUnit.SECONDS)).thenReturn(mockFuture);
		when(mockFuture.get()).thenReturn(mockConnection);
		Whitebox.setInternalState(DefaultClient.class, "delayedExecutor", (Object) null);
		final int threadSize = 1;
		when(DriverUtils.getDefaultDelayedExecutorThreadCount()).thenReturn(threadSize);
		when(Executors.newScheduledThreadPool(threadSize)).thenReturn(mockExecutor);
		
		assertEquals(mockConnection, client.connect(requestData));
		
		verify(client).reload();
		verifyStatic(DriverConfigUtils.class, times(2));
		DriverConfigUtils.getConfig();
		assertEquals(mockExecutor, Whitebox.getInternalState(DefaultClient.class, "delayedExecutor"));
	}
	
	//@Test
	public void reload_shouldSendAReloadRequest() throws Exception {
		client = Mockito.spy(client);
		List<RequestContext> contexts = new ArrayList();
		Mockito.doAnswer((Answer<Object>) invocation -> {
			contexts.add(invocation.getArgument(0));
			return null;
		}).when(client).sendRequest(ArgumentMatchers.any(DefaultRequestContext.class));
		
		client.reload();
		
		assertEquals(1, contexts.size());
		assertEquals(Command.RELOAD, contexts.get(0).getRequest().getCommand());
	}
	
	//@Test
	public void sendRequest_shouldSendATheRequestToTheDatabase() throws Exception {
		DriverConfig mockConfig = Mockito.mock(DriverConfig.class);
		RequestContext mockContext = Mockito.mock(RequestContext.class);
		Database mockDb = Mockito.mock(Database.class);
		//when(DriverConfigUtils.getConfig()).thenReturn(mockConfig);
		when(mockConfig.getDatabase()).thenReturn(mockDb);
		
		client.sendRequest(mockContext);
		
		Mockito.verify(mockDb).process(mockContext);
	}
	
}
