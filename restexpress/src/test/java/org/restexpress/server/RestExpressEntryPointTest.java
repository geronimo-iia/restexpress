/**
 *        Licensed to the Apache Software Foundation (ASF) under one
 *        or more contributor license agreements.  See the NOTICE file
 *        distributed with this work for additional information
 *        regarding copyright ownership.  The ASF licenses this file
 *        to you under the Apache License, Version 2.0 (the
 *        "License"); you may not use this file except in compliance
 *        with the License.  You may obtain a copy of the License at
 *
 *          http://www.apache.org/licenses/LICENSE-2.0
 *
 *        Unless required by applicable law or agreed to in writing,
 *        software distributed under the License is distributed on an
 *        "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *        KIND, either express or implied.  See the License for the
 *        specific language governing permissions and limitations
 *        under the License.
 *
 */
package org.restexpress.server;

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
import org.restexpress.RestExpressEntryPoint;
import org.restexpress.RestExpressLauncher;

/**
 * {@link RestExpressEntryPointTest} test {@link RestExpressEntryPoint}.
 * 
 * @author <a href="mailto:jguibert@intelligents-ia.com" >Jerome Guibert</a>
 * 
 */
public class RestExpressEntryPointTest {

	/**
	 * The REST server that handles the test calls.
	 */
	private static RestExpressLauncher launcher;
	private HttpClient httpClient;

	@BeforeClass
	public static void beforeClass() throws Exception {
		launcher = new RestExpressLauncher();
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

}
