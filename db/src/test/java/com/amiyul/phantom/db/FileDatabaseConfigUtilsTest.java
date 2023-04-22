/*
 * Add Copyright
 */
package com.amiyul.phantom.db;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

import com.amiyul.phantom.api.Utils;

@RunWith(PowerMockRunner.class)
@PrepareForTest(Utils.class)
public class FileDatabaseConfigUtilsTest {
	
	@Mock
	private DatabaseConfigFileParser mockParser;
	
	@Mock
	private File mockFile;
	
	@Before
	public void setup() throws Exception {
		PowerMockito.mockStatic(Utils.class);
		Mockito.when(Utils.loadClass(anyString())).thenThrow(ClassNotFoundException.class);
	}
	
	@After
	public void tearDown() {
		Whitebox.setInternalState(FileDatabaseConfigUtils.class, "configFilePath", (Object) null);
		Whitebox.setInternalState(FileDatabaseConfigUtils.class, "parser", (Object) null);
		Whitebox.setInternalState(FileDatabaseConfigUtils.class, "configMetadata", (Object) null);
		Whitebox.setInternalState(FileDatabaseConfigUtils.class, "config", (Object) null);
	}
	
	@Test
	public void getParser_shouldReturnTheAppropriateParser() {
		when(Utils.getParser(DatabaseConfigFileParser.class, mockFile)).thenReturn(mockParser);
		
		assertEquals(mockParser, FileDatabaseConfigUtils.getParser(mockFile));
	}
	
	@Test
	public void getParser_shouldReturnTheCachedParser() {
		Whitebox.setInternalState(FileDatabaseConfigUtils.class, "parser", mockParser);
		
		assertEquals(mockParser, FileDatabaseConfigUtils.getParser(mockFile));
		
		PowerMockito.verifyZeroInteractions(Utils.class);
	}
	
	@Test
	public void getParser_shouldFailIfNoParserIsFound() {
		RuntimeException ex = Assert.assertThrows(RuntimeException.class, () -> FileDatabaseConfigUtils.getParser(mockFile));
		
		assertEquals("No appropriate parser found for specified database config file", ex.getMessage());
	}
	
	@Test
	public void discardConfig_shouldClearConfigAndMetadataCaches() {
		DatabaseConfigMetadata metadata = Mockito.mock(DatabaseConfigMetadata.class);
		DatabaseConfig config = Mockito.mock(DatabaseConfig.class);
		Whitebox.setInternalState(FileDatabaseConfigUtils.class, "configMetadata", metadata);
		Whitebox.setInternalState(FileDatabaseConfigUtils.class, "config", config);
		
		FileDatabaseConfigUtils.discardConfig();
		
		assertNull(Whitebox.getInternalState(FileDatabaseConfigUtils.class, "configMetadata"));
		assertNull(Whitebox.getInternalState(FileDatabaseConfigUtils.class, "config"));
	}
	
	@Test
	public void getConfigFile_shouldGetTheConfigFile() {
		final String path = "/some/path";
		when(Utils.getFilePath(FileDatabaseConfigUtils.PROP_CONFIG_LOCATION)).thenReturn(path);
		assertEquals(path, FileDatabaseConfigUtils.getConfigFile());
	}
	
	@Test
	public void getConfigFile_shouldReturnTheCachedConfigFile() {
		final String path = "/some/cached/path";
		Whitebox.setInternalState(FileDatabaseConfigUtils.class, "configFilePath", path);
		assertEquals(path, FileDatabaseConfigUtils.getConfigFile());
	}
	
	@Test
	public void getConfigFile_shouldFailIfNoneIsConfigured() {
		when(Utils.isBlank(ArgumentMatchers.any())).thenReturn(true);
		RuntimeException ex = Assert.assertThrows(RuntimeException.class, () -> FileDatabaseConfigUtils.getConfigFile());
		
		assertEquals("Found no defined location for the database config file", ex.getMessage());
	}
	
	@Test
	public void getConfigMetadata_shouldReturnTheConfigMetadata() throws Exception {
		final String path = "/some/cached/path";
		Whitebox.setInternalState(FileDatabaseConfigUtils.class, "configFilePath", path);
		Whitebox.setInternalState(FileDatabaseConfigUtils.class, "parser", mockParser);
		DatabaseConfigMetadata mockMetadata = Mockito.mock(DatabaseConfigMetadata.class);
		FileInputStream mockStream = Mockito.mock(FileInputStream.class);
		List<File> files = new ArrayList();
		when(Utils.getInputStream(ArgumentMatchers.any(File.class))).thenAnswer(invocation -> {
			files.add(invocation.getArgument(0));
			return mockStream;
		});
		when(mockParser.parse(mockStream)).thenReturn(mockMetadata);
		
		DatabaseConfigMetadata metadata = FileDatabaseConfigUtils.getConfigMetadata();
		
		assertEquals(mockMetadata, metadata);
		assertEquals(path, files.get(0).getAbsolutePath());
	}
	
	@Test
	public void getConfigMetadata_shouldReturnTheCachedMetadata() throws Exception {
		DatabaseConfigMetadata mockMetadata = Mockito.mock(DatabaseConfigMetadata.class);
		Whitebox.setInternalState(FileDatabaseConfigUtils.class, "configMetadata", mockMetadata);
		
		assertEquals(mockMetadata, FileDatabaseConfigUtils.getConfigMetadata());
		PowerMockito.verifyZeroInteractions(Utils.class);
	}
	
	@Test
	public void getConfig_shouldGetTheConfigInstance() {
		DatabaseConfigMetadata mockMetadata = Mockito.mock(DatabaseConfigMetadata.class);
		final String dbName1 = "db1";
		final String dbName2 = "db2";
		DatabaseDefinition dbDef1 = new DatabaseDefinition(dbName1, null, null, null);
		DatabaseDefinition dbDef2 = new DatabaseDefinition(dbName2, null, null, null);
		when(mockMetadata.getDatabaseDefinitions()).thenReturn(Arrays.asList(dbDef1, dbDef2));
		Whitebox.setInternalState(FileDatabaseConfigUtils.class, "configMetadata", mockMetadata);
		
		DatabaseConfig dbConfig = FileDatabaseConfigUtils.getConfig();
		
		assertEquals(2, dbConfig.getDatabaseDefinitions().size());
		assertEquals(dbDef1, dbConfig.getDatabaseDefinitions().get(dbName1));
		assertEquals(dbDef2, dbConfig.getDatabaseDefinitions().get(dbName2));
	}
	
	@Test
	public void getConfig_shouldReturnTheCachedConfig() {
		DatabaseConfig mockConfig = Mockito.mock(DatabaseConfig.class);
		Whitebox.setInternalState(FileDatabaseConfigUtils.class, "config", mockConfig);
		
		assertEquals(mockConfig, FileDatabaseConfigUtils.getConfig());
	}
	
}
