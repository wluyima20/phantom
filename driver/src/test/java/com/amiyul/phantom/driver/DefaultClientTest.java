/*
 * Copyright (C) Amiyul LLC - All Rights Reserved
 *
 * This source code is protected under international copyright law. All rights
 * reserved and protected by the copyright holder.
 * 
 * This file is confidential and only available to authorized individuals with the
 * permission of the copyright holder. If you encounter this file and do not have
 * permission, please contact the copyright holder and delete this file.
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
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.verifyStatic;

import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.Assert;
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

import com.amiyul.phantom.api.ConnectionRequest;
import com.amiyul.phantom.api.Database;
import com.amiyul.phantom.api.PhantomProtocol.Command;
import com.amiyul.phantom.api.RequestContext;
import com.amiyul.phantom.api.Status;
import com.amiyul.phantom.api.StatusRequest;
import com.amiyul.phantom.api.Utils;
import com.amiyul.phantom.api.logging.LoggerUtils;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ DriverConfigUtils.class, DefaultClient.class, Utils.class, LoggerUtils.class, DelayedConnectTask.class,
        Executors.class, DriverUtils.class, AsyncConnectTask.class })
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
	private ScheduledExecutorService mockDelayedExecutor;
	
	@Mock
	private ExecutorService mockAsyncExecutor;
	
	@Mock
	private DelayedConnectTask mockDelayedTask;
	
	@Mock
	private AsyncConnectTask mockAsyncTask;
	
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
		Whitebox.setInternalState(DefaultClient.class, "delayedExecutor", mockDelayedExecutor);
		Whitebox.setInternalState(DefaultClient.class, "asyncExecutor", mockAsyncExecutor);
		PowerMockito.spy(DefaultClient.class);
		when(DriverConfigUtils.getConfig()).thenReturn(mockConfig);
	}
	
	@After
	public void tearDown() {
		Whitebox.setInternalState(DefaultClient.class, "delayedExecutor", (Object) null);
		Whitebox.setInternalState(DefaultClient.class, "asyncExecutor", (Object) null);
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
		PowerMockito.whenNew(DelayedConnectTask.class).withArguments(requestData).thenReturn(mockDelayedTask);
		when(mockDelayedExecutor.schedule(mockDelayedTask, delay, TimeUnit.SECONDS)).thenReturn(mockFuture);
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
		PowerMockito.whenNew(DelayedConnectTask.class).withArguments(requestData).thenReturn(mockDelayedTask);
		when(mockDelayedExecutor.schedule(mockDelayedTask, delay, TimeUnit.SECONDS)).thenReturn(mockFuture);
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
		PowerMockito.whenNew(DelayedConnectTask.class).withArguments(requestData).thenReturn(mockDelayedTask);
		when(mockDelayedExecutor.schedule(mockDelayedTask, dbDelay, TimeUnit.SECONDS)).thenReturn(mockFuture);
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
		PowerMockito.whenNew(DelayedConnectTask.class).withArguments(requestData).thenReturn(mockDelayedTask);
		when(mockDelayedExecutor.schedule(mockDelayedTask, targetDelay, TimeUnit.SECONDS)).thenReturn(mockFuture);
		when(mockFuture.get()).thenReturn(mockConnection);
		
		assertEquals(mockConnection, client.connect(requestData));
		
		verify(client).reload();
		verifyStatic(DriverConfigUtils.class, times(2));
		DriverConfigUtils.getConfig();
	}
	
	@Test
	public void connect_shouldCreateAndSetDelayedExecutorIfNecessary() throws Exception {
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
		PowerMockito.whenNew(DelayedConnectTask.class).withArguments(requestData).thenReturn(mockDelayedTask);
		when(mockDelayedExecutor.schedule(mockDelayedTask, delay, TimeUnit.SECONDS)).thenReturn(mockFuture);
		when(mockFuture.get()).thenReturn(mockConnection);
		Whitebox.setInternalState(DefaultClient.class, "delayedExecutor", (Object) null);
		final int threadSize = 1;
		when(DriverUtils.getDefaultDelayedExecutorThreadCount()).thenReturn(threadSize);
		when(Executors.newScheduledThreadPool(threadSize)).thenReturn(mockDelayedExecutor);
		
		assertEquals(mockConnection, client.connect(requestData));
		
		verify(client).reload();
		verifyStatic(DriverConfigUtils.class, times(2));
		DriverConfigUtils.getConfig();
		assertEquals(mockDelayedExecutor, Whitebox.getInternalState(DefaultClient.class, "delayedExecutor"));
	}
	
	@Test
	public void doConnect_shouldConnectForANonAsyncRequest() throws Exception {
		client = Mockito.spy(client);
		ConnectionRequestData requestData = new ConnectionRequestData(null, false, null);
		doReturn(mockConnection).when(client).doConnectInternal(requestData);
		
		assertEquals(mockConnection, client.doConnect(requestData));
		verifyNoInteractions(mockAsyncExecutor);
	}
	
	@Test
	public void doConnect_shouldConnectAsynchronouslyForAnAsyncRequest() throws Exception {
		client = Mockito.spy(client);
		ConnectionRequestData requestData = new ConnectionRequestData(null, true, null);
		PowerMockito.whenNew(AsyncConnectTask.class).withArguments(requestData).thenReturn(mockAsyncTask);
		
		Connection connection = client.doConnect(requestData);
		Assert.assertTrue(connection instanceof Proxy);
		Mockito.verify(mockAsyncExecutor).execute(mockAsyncTask);
	}
	
	@Test
	public void doConnect_shouldCreateAndSetAsyncExecutorIfNecessary() throws Exception {
		client = Mockito.spy(client);
		ConnectionRequestData requestData = new ConnectionRequestData(null, true, null);
		PowerMockito.whenNew(AsyncConnectTask.class).withArguments(requestData).thenReturn(mockAsyncTask);
		Whitebox.setInternalState(DefaultClient.class, "asyncExecutor", (Object) null);
		final int threadSize = 1;
		when(DriverUtils.getDefaultAsyncExecutorThreadCount()).thenReturn(threadSize);
		when(Executors.newFixedThreadPool(threadSize)).thenReturn(mockAsyncExecutor);
		
		Connection connection = client.doConnect(requestData);
		Assert.assertTrue(connection instanceof Proxy);
		Mockito.verify(mockAsyncExecutor).execute(mockAsyncTask);
		assertEquals(mockAsyncExecutor, Whitebox.getInternalState(DefaultClient.class, "asyncExecutor"));
	}
	
	@Test
	public void doConnectInternal_shouldSendAConnectRequestToTheTargetDb() throws Exception {
		final String dbName = "db";
		client = Mockito.spy(client);
		ConnectionRequestData requestData = new ConnectionRequestData(dbName, true, null);
		List<RequestContext> contexts = new ArrayList();
		Mockito.doAnswer((Answer<Object>) invocation -> {
			RequestContext context = invocation.getArgument(0);
			context.writeResult(mockConnection);
			contexts.add(context);
			return null;
		}).when(client).sendRequest(ArgumentMatchers.any(DefaultRequestContext.class));
		
		assertEquals(mockConnection, client.doConnectInternal(requestData));
		
		assertEquals(1, contexts.size());
		assertEquals(dbName, ((ConnectionRequest) contexts.get(0).getRequest()).getTargetDatabaseName());
	}
	
	@Test
	public void doConnectInternal_shouldReloadAndReSendAConnectRequestIfASqlExceptionIsEncountered() throws Exception {
		final String dbName = "db";
		client = Mockito.spy(client);
		ConnectionRequestData requestData = new ConnectionRequestData(dbName, true, null);
		List<RequestContext> contexts = new ArrayList();
		Exception ex = Mockito.mock(SQLException.class);
		List<Exception> exceptions = new ArrayList();
		Mockito.doAnswer((Answer<Object>) invocation -> {
			if (exceptions.isEmpty()) {
				exceptions.add(ex);
				throw ex;
			}
			RequestContext context = invocation.getArgument(0);
			context.writeResult(mockConnection);
			contexts.add(context);
			return null;
		}).when(client).sendRequest(ArgumentMatchers.any(DefaultRequestContext.class));
		
		assertEquals(mockConnection, client.doConnectInternal(requestData));
		
		PowerMockito.verifyStatic(DriverConfigUtils.class);
		DriverConfigUtils.reloadConfig();
		assertEquals(1, contexts.size());
		assertEquals(dbName, ((ConnectionRequest) contexts.get(0).getRequest()).getTargetDatabaseName());
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
		
		assertEquals(1, contexts.size());
		assertEquals(Command.RELOAD, contexts.get(0).getRequest().getCommand());
	}
	
	@Test
	public void getStatus_shouldSendAStatusRequestToTheTargetDb() throws Exception {
		final String dbName = "db";
		client = Mockito.spy(client);
		List<RequestContext> contexts = new ArrayList();
		Mockito.doAnswer((Answer<Object>) invocation -> {
			RequestContext context = invocation.getArgument(0);
			context.writeResult(mockTargetDbStatus);
			contexts.add(context);
			return null;
		}).when(client).sendRequest(ArgumentMatchers.any(DefaultRequestContext.class));
		
		assertEquals(mockTargetDbStatus, client.getStatus(dbName));
		
		assertEquals(1, contexts.size());
		assertEquals(dbName, ((StatusRequest) contexts.get(0).getRequest()).getTargetDatabaseName());
	}
	
	@Test
	public void sendRequest_shouldSendATheRequestToTheDatabase() throws Exception {
		RequestContext mockContext = Mockito.mock(RequestContext.class);
		Database mockDb = Mockito.mock(Database.class);
		when(DriverConfigUtils.getConfig()).thenReturn(mockConfig);
		when(mockConfig.getDatabase()).thenReturn(mockDb);
		
		client.sendRequest(mockContext);
		
		verify(mockDb).process(mockContext);
	}
	
	@Test
	public void shutdown_shouldShutdownTheExecutors() {
		client.shutdown();
		
		verify(mockDelayedExecutor).shutdownNow();
		verify(mockAsyncExecutor).shutdownNow();
	}
	
	@Test
	public void shutdown_shouldSkipShuttingDownTheExecutorsIfTheyAreNotYetSet() {
		Whitebox.setInternalState(DefaultClient.class, "delayedExecutor", (Object) null);
		Whitebox.setInternalState(DefaultClient.class, "asyncExecutor", (Object) null);
		
		client.shutdown();
		
		verifyNoInteractions(mockDelayedExecutor);
		verifyNoInteractions(mockAsyncExecutor);
	}
	
}
