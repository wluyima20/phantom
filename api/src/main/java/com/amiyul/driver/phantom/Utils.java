/*
 * Add Copyright
 */
package com.amiyul.driver.phantom;

import com.amiyul.driver.phantom.logging.DriverLogger;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

public class Utils {
	
	protected static DriverLogger LOGGER;
	
	private static final String DB_SERVER_SERVICE_URL = "http://localhost:8080/";
	
	protected static final String DOUBLE_FORWARD_SLASH = "//";
	
	protected static final char FORWARD_SLASH = '/';
	
	protected static final String LOGICAL_NAME_PLACEHOLDER = "@logicalName@";
	
	protected static final String WIPE_OUT_TEMPLATE = PhantomDriver.URL_SUFFIX + DOUBLE_FORWARD_SLASH
	        + LOGICAL_NAME_PLACEHOLDER + FORWARD_SLASH;
	
	private static final Map<String, String> nameServerMap = new ConcurrentHashMap();
	
	private static final String CACHE_FILENAME = "phantom";
	
	protected static String resolveUrl(String logicalUrl, String logicalName, String hostAndPort) throws Exception {
		
		final String wipeOutPhrase = WIPE_OUT_TEMPLATE.replaceFirst(LOGICAL_NAME_PLACEHOLDER, logicalName);
		
		String resolvedUrl = logicalUrl.replaceFirst(wipeOutPhrase, "");
		
		resolvedUrl = resolvedUrl.replaceFirst(logicalName, hostAndPort);
		
		return resolvedUrl;
	}
	
	protected static String getServerInfo(String logicalName) throws Exception {
		String hostAndPort = nameServerMap.get(logicalName);
		
		if (isBlank(hostAndPort)) {
			hostAndPort = lookupFromLocalCache(logicalName);
			if (isBlank(hostAndPort)) {
				return null;
			}
			
			nameServerMap.put(logicalName, hostAndPort);
		} else {
			LOGGER.info("DB server info from in memory cache " + hostAndPort);
		}
		
		return hostAndPort;
	}
	
	protected static void updateCaches(String logicalName, String hostAndPort) throws IOException {
		
		LOGGER.info("Updating local caches with: " + logicalName + " -> " + hostAndPort);
		
		nameServerMap.put(logicalName, hostAndPort);
		File file = getCacheFile();
		
		LOGGER.info("Local cache file " + file.getAbsolutePath());
		
		if (!file.exists() || !file.isFile()) {
			file.createNewFile();
		}
		
		Properties props = new Properties();
		try (InputStream in = new FileInputStream(file)) {
			props.load(in);
			props.setProperty(logicalName, hostAndPort);
		}
		
		try (OutputStream out = new FileOutputStream(file)) {
			props.store(out, "Generated by phantom driver");
		}
		
		LOGGER.info("Successfully updated local caches with: " + logicalName + " -> " + hostAndPort);
	}
	
	private static String lookupFromLocalCache(String logicalName) throws Exception {
		LOGGER.info("Looking up DB server info from local cache for logical name: " + logicalName);
		
		File file = getCacheFile();
		if (!file.exists() || !file.isFile()) {
			LOGGER.info("No DB server info found in local cache for logical name: " + logicalName);
			return null;
		}
		
		Properties props = new Properties();
		try (InputStream in = new FileInputStream(file)) {
			props.load(in);
		}
		
		String hostAndPort = props.getProperty(logicalName);
		if (!isBlank(hostAndPort)) {
			LOGGER.info("DB server info from local cache for logical name: " + logicalName + " -> " + hostAndPort);
		}
		
		return hostAndPort;
	}
	
	private static File getCacheFile() {
		return null;//Paths.get(System.getProperty("user.home"), CACHE_FILENAME).toFile();
	}
	
	protected static String lookupFromDbService(String logicalName) throws Exception {
		LOGGER.info("Looking up DB server info from service for logical name: " + logicalName);
		
		/*String requestUrl = DB_SERVER_SERVICE_URL + logicalName;
		HttpClient httpClient = getHttpClient();
		HttpRequest serverInfoRequest = HttpRequest.newBuilder()
		        .uri(URI.create(requestUrl))
		        .header("Accept", "text/plain")
		        .build();
		
		HttpResponse<String> response = httpClient.send(serverInfoRequest, BodyHandlers.ofString());
		if (response.statusCode() != 200) {
		    throw new Exception("Failed to get server info for logical name: " + logicalName);
		}
		
		String hostAndPort = response.body();
		
		getLogger().info("DB server info from service for logical name: " + logicalName + " -> " + hostAndPort);*/
		
		return null;//hostAndPort;
	}
	
	protected static String extractNameFromUrl(String url) {
		//jdbc:mysql://localhost:3306/amiyul
		//Assuming urls of the form jdbc:phantom://local/mysql:@local for now but will need to be way
		// smarter to if we want to avoid the repetition of the logical name in the url
		int start = url.indexOf(DOUBLE_FORWARD_SLASH) + 2;
		return url.substring(start, url.indexOf(FORWARD_SLASH, start));
	}
	
	protected static boolean isBlank(String s) {
		return s == null || s.trim().isEmpty();
	}
	
}
