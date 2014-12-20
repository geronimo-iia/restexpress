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
package org.intelligentsia.restexpress.sample.benchmark;

import java.io.IOException;

import junit.framework.TestCase;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

public class BenchmarkTestCase extends TestCase {

	private static final String TEST_URL = "http://localhost:8081/echo?echo=test";

	/**
	 * Build a new instance.
	 */
	public BenchmarkTestCase() {
		super();
	}

	/**
	 * Build a new instance.
	 * 
	 * @param name
	 */
	public BenchmarkTestCase(String name) {
		super(name);
	}

	public void testEcho() throws ClientProtocolException, IOException {
		HttpClient client = new DefaultHttpClient();
		HttpGet get = null;
		try {
			get = new HttpGet(TEST_URL);
			HttpResponse response = (HttpResponse) client.execute(get);
			assertEquals(200, response.getStatusLine().getStatusCode());
		} finally {
			if (get != null)
				get.releaseConnection();
		}
	}
}
