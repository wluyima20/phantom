/*
 * Add Copyright
 */
package com.amiyul.phantom.driver;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

import com.amiyul.phantom.api.BaseDatabase;
import com.amiyul.phantom.api.Database;
import com.amiyul.phantom.api.DatabaseProvider;
import com.amiyul.phantom.api.Status;
import com.amiyul.phantom.api.Utils;
import com.amiyul.phantom.api.logging.LoggerUtils;
import com.amiyul.phantom.db.FileDatabase;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ Utils.class, LoggerUtils.class, DefaultClient.class })
public class DriverConfigUtilsTest {
	
	private static class MockDatabase extends BaseDatabase {
		
		@Override
		public Connection getConnection(String targetDatabaseName) throws SQLException {
			return null;
		}
		
		@Override
		public void reload() {
		}
		
		@Override
		public Status getStatus(String targetDatabaseName) throws SQLException {
			return null;
		}
		
	}
	
	public static class MockDatabaseProvider implements DatabaseProvider {
		
		@Override
		public Object get() {
			return new MockDatabase();
		}
		
	}
	
	@Mock
	private DriverConfigFileParser mockParser;
	
	@Mock
	private File mockFile;
	
	@Mock
	private DefaultClient mockClient;
	
	@Before
	public void setup() {
		PowerMockito.mockStatic(Utils.class);
		PowerMockito.mockStatic(LoggerUtils.class);
		PowerMockito.mockStatic(DefaultClient.class);
	}
	
	@After
	public void tearDown() {
		Whitebox.setInternalState(DriverConfigUtils.class, "configFilePath", (Object) null);
		Whitebox.setInternalState(DriverConfigUtils.class, "parser", (Object) null);
		Whitebox.setInternalState(DriverConfigUtils.class, "configMetadata", (Object) null);
		Whitebox.setInternalState(DriverConfigUtils.class, "config", (Object) null);
	}
	
	@Test
	public void getParser_shouldReturnTheAppropriateParser() {
		when(Utils.getParser(DriverConfigFileParser.class, mockFile)).thenReturn(mockParser);
		
		assertEquals(mockParser, DriverConfigUtils.getParser(mockFile));
	}
	
	@Test
	public void getParser_shouldReturnTheCachedParser() {
		Whitebox.setInternalState(DriverConfigUtils.class, "parser", mockParser);
		
		assertEquals(mockParser, DriverConfigUtils.getParser(mockFile));
		
		PowerMockito.verifyZeroInteractions(Utils.class);
	}
	
	@Test
	public void getParser_shouldFailIfNoParserIsFound() {
		RuntimeException ex = Assert.assertThrows(RuntimeException.class, () -> DriverConfigUtils.getParser(mockFile));
		
		assertEquals("No appropriate parser found for specified driver config file", ex.getMessage());
	}
	
	@Test
	public void createMetadata_shouldCreateTheConfigMetadataWithTheLoadedClass() {
		final String className = MockDatabaseProvider.class.getName();
		final String unavailableUntil = "2023-05-23T22:25:10+03:00";
		DriverConfigMetadata metadata = DriverConfigUtils.createMetadata(className, unavailableUntil);
		assertEquals(className, metadata.getDatabaseProviderClassName());
		assertEquals(unavailableUntil, metadata.getUnavailableUntil());
	}
	
	@Test
	public void reloadConfig_shouldClearConfigAndMetadataCachesAndSendReloadSignalToTheDatabase() throws Exception {
		DriverConfigMetadata metadata = Mockito.mock(DriverConfigMetadata.class);
		DriverConfig config = Mockito.mock(DriverConfig.class);
		Whitebox.setInternalState(DriverConfigUtils.class, "configMetadata", metadata);
		Whitebox.setInternalState(DriverConfigUtils.class, "config", config);
		when(DefaultClient.getInstance()).thenReturn(mockClient);
		
		DriverConfigUtils.reloadConfig();
		
		assertNull(Whitebox.getInternalState(DriverConfigUtils.class, "configMetadata"));
		assertNull(Whitebox.getInternalState(DriverConfigUtils.class, "config"));
		verify(mockClient).reload();
	}
	
	@Test
	public void getConfigFile_shouldGetTheConfigFile() {
		final String path = "/some/path";
		when(Utils.getFilePath(DriverConfigUtils.PROP_CONFIG_LOCATION)).thenReturn(path);
		assertEquals(path, DriverConfigUtils.getConfigFile());
	}
	
	@Test
	public void getConfigFile_shouldReturnTheCachedConfigFile() {
		final String path = "/some/cached/path";
		Whitebox.setInternalState(DriverConfigUtils.class, "configFilePath", path);
		assertEquals(path, DriverConfigUtils.getConfigFile());
	}
	
	@Test
	public void getConfigFile_shouldReturnNullIfNoneIsConfigured() {
		assertNull(DriverConfigUtils.getConfigFile());
	}
	
	@Test
	public void getConfigMetadata_shouldReturnTheConfigMetadata() throws Exception {
		final String path = "/some/cached/path";
		Whitebox.setInternalState(DriverConfigUtils.class, "configFilePath", path);
		Whitebox.setInternalState(DriverConfigUtils.class, "parser", mockParser);
		DriverConfigMetadata mockMetadata = Mockito.mock(DriverConfigMetadata.class);
		FileInputStream mockStream = Mockito.mock(FileInputStream.class);
		List<File> files = new ArrayList();
		when(Utils.getInputStream(any(File.class))).thenAnswer(invocation -> {
			files.add(invocation.getArgument(0));
			return mockStream;
		});
		when(mockParser.parse(mockStream)).thenReturn(mockMetadata);
		
		DriverConfigMetadata metadata = DriverConfigUtils.getConfigMetadata();
		
		assertEquals(mockMetadata, metadata);
		assertEquals(path, files.get(0).getAbsolutePath());
	}
	
	@Test
	public void getConfigMetadata_shouldReturnTheCachedMetadata() throws Exception {
		DriverConfigMetadata mockMetadata = Mockito.mock(DriverConfigMetadata.class);
		Whitebox.setInternalState(DriverConfigUtils.class, "configMetadata", mockMetadata);
		
		assertEquals(mockMetadata, DriverConfigUtils.getConfigMetadata());
		PowerMockito.verifyZeroInteractions(Utils.class);
	}
	
	@Test
	public void getConfigMetadata_shouldReturnNullIfNoConfigFileIsSpecified() throws Exception {
		when(Utils.isBlank(any())).thenReturn(true);
		
		assertNull(DriverConfigUtils.getConfigMetadata());
	}
	
	@Test
	public void getConfig_shouldGetTheConfigObject() throws Exception {
		final String className = MockDatabaseProvider.class.getName();
		final String unavailableUntil = "2023-05-23T22:25:10+03:00";
		DriverConfigMetadata mockMetadata = Mockito.mock(DriverConfigMetadata.class);
		when(mockMetadata.getDatabaseProviderClassName()).thenReturn(className);
		when(mockMetadata.getUnavailableUntil()).thenReturn(unavailableUntil);
		Whitebox.setInternalState(DriverConfigUtils.class, "configMetadata", mockMetadata);
		when(Utils.loadClass(className)).thenCallRealMethod();
		LocalDateTime mockDate = LocalDateTime.now();
		when(Utils.parseDateString(unavailableUntil)).thenReturn(mockDate);
		
		DriverConfig config = DriverConfigUtils.getConfig();
		
		assertEquals(MockDatabase.class, config.getDatabase().getClass());
		assertEquals(mockDate, config.getStatus().getUnavailableUntil());
	}
	
	@Test
	public void getConfig_shouldDefaultToTheFileDatabaseProvider() {
		DriverConfigMetadata mockMetadata = Mockito.mock(DriverConfigMetadata.class);
		Whitebox.setInternalState(DriverConfigUtils.class, "configMetadata", mockMetadata);
		
		assertEquals(FileDatabase.class, DriverConfigUtils.getConfig().getDatabase().getClass());
	}
	
	@Test
	public void getConfig_shouldDefaultToTheFileDatabaseProviderIfNoConfigMetadataExists() {
		when(Utils.isBlank(any())).thenReturn(true);
		
		assertEquals(FileDatabase.class, DriverConfigUtils.getConfig().getDatabase().getClass());
	}
	
	@Test
	public void getConfig_shouldReturnTheCachedConfig() {
		Database mockDatabase = Mockito.mock(Database.class);
		DriverConfig mockConfig = Mockito.mock(DriverConfig.class);
		when(mockConfig.getDatabase()).thenReturn(mockDatabase);
		Whitebox.setInternalState(DriverConfigUtils.class, "config", mockConfig);
		
		assertEquals(mockDatabase, DriverConfigUtils.getConfig().getDatabase());
	}
	
}
