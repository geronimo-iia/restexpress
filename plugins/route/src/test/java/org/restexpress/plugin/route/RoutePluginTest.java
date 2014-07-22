package org.restexpress.plugin.route;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.restexpress.RestExpressLauncher;
import org.restexpress.pipeline.observer.SimpleConsoleLogMessageObserver;

/**
 * RoutePluginTest.
 * 
 * @author <a href="mailto:jguibert@intelligents-ia.com" >Jerome Guibert</a>
 * 
 */
public class RoutePluginTest {
	/**
	 * The REST server that handles the test calls.
	 */
	private static RestExpressLauncher launcher;
	private HttpClient httpClient;

	@BeforeClass
	public static void beforeClass() throws Exception {
		launcher = new RestExpressLauncher();
		launcher.server().registerPlugin(new RouteMetadataPlugin());
		launcher.server().addMessageObserver(new SimpleConsoleLogMessageObserver());
		launcher.bind();
	}

	@AfterClass
	public static void afterClass() {
		launcher.shutdown();
	}

	@Before
	public void beforeEach() {
		httpClient = new DefaultHttpClient();
	}

	@After
	public void afterEach() {
		httpClient = null;
	}

	@Test
	public void testGetAllRoute() throws IOException {
		HttpGet getRequest = new HttpGet(launcher.server().settings().serverSettings().getBaseUrl() + "/routes/metadata");
		final HttpResponse response = httpClient.execute(getRequest);
		assertEquals(200, response.getStatusLine().getStatusCode());
	}

	@Test
	public void testGetSingle() throws IOException {
		HttpGet getRequest = new HttpGet(launcher.server().settings().serverSettings().getBaseUrl() + "/routes/single.route.metadata/metadata.json");
		final HttpResponse response = httpClient.execute(getRequest);
		assertEquals(200, response.getStatusLine().getStatusCode());
		;
	}

	@Test
	public void testGetSingleDidNotExist() throws IOException {
		HttpGet getRequest = new HttpGet(launcher.server().settings().serverSettings().getBaseUrl() + "/routes/NOROUTE/metadata.json");
		final HttpResponse response = httpClient.execute(getRequest);
		assertEquals(404, response.getStatusLine().getStatusCode());
	}

	@Test
	public void testGetConsole() throws IOException {
		HttpGet getRequest = new HttpGet(launcher.server().settings().serverSettings().getBaseUrl() + "/routes");
		final HttpResponse response = httpClient.execute(getRequest);
		assertEquals(200, response.getStatusLine().getStatusCode());
		// System.err.println(new Scanner(response.getEntity().getContent(),
		// "UTF-8").useDelimiter("\\A").next());
	}

}
