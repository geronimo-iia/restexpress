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
package org.restexpress.jaxrs;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.jboss.netty.handler.codec.http.HttpResponseStatus;
import org.junit.Test;
import org.restexpress.JaxRsReader;
import org.restexpress.RestExpressService;
import org.restexpress.exception.ConfigurationException;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;

public class JaxRsReaderTest {

	@Test(expected = ConfigurationException.class)
	public void shouldFailWhenNoMapping() {
		JaxRsReader.register(RestExpressService.newBuilder(), new FailEchoService());
	}

	@Test
	public void readEchoService() {
		RestExpressService restExpress = RestExpressService.newBuilder();
		int result = JaxRsReader.register(restExpress, new EchoService());

		assertEquals(4, result);

		Map<String, String> routes = restExpress.getRouteUrlsByName();

		assertEquals(4, routes.size());
		assertNotNull(Iterables.filter(routes.keySet(), new Predicate<String>(){
			 @Override
			public boolean apply(String input) {
				return input.startsWith("echoservice.root");
			}
		}));
		assertNotNull(Iterables.filter(routes.keySet(), new Predicate<String>(){
			 @Override
			public boolean apply(String input) {
				return input.startsWith("echoservice.hello");
			}
		}));
		assertNotNull(Iterables.filter(routes.keySet(), new Predicate<String>(){
			 @Override
			public boolean apply(String input) {
				return input.startsWith("echoservice.greeting");
			}
		}));
		assertNotNull(Iterables.filter(routes.keySet(), new Predicate<String>(){
			 @Override
			public boolean apply(String input) {
				return input.startsWith("echoservice.restexpress.who");
			}
		}));
	
	}

	@Test
	public void sayHello() throws ClientProtocolException, IOException {
		RestExpressService restExpress = RestExpressService.newBuilder();
		EchoService echoService = new EchoService();
		JaxRsReader.register(restExpress, echoService);
		HttpGet request = null;
		try {
			restExpress.bind();

			HttpClient http = new DefaultHttpClient();
			request = new HttpGet("http://localhost:8081/hello");
			HttpResponse response = (HttpResponse) http.execute(request);
			assertEquals(HttpResponseStatus.OK.getCode(), response.getStatusLine().getStatusCode());
			HttpEntity entity = response.getEntity();
			assertEquals("\"hello\"", EntityUtils.toString(entity));
			request.releaseConnection();

			request = new HttpGet("http://localhost:8081/?echo=ping");
			response = (HttpResponse) http.execute(request);
			assertEquals(HttpResponseStatus.OK.getCode(), response.getStatusLine().getStatusCode());
			entity = response.getEntity();
			assertEquals("\"ping\"", EntityUtils.toString(entity));

		} finally {
			if (request != null)
				request.releaseConnection();
			restExpress.shutdown();
		}

	}

	@Test
	public void sendEcho() throws ClientProtocolException, IOException {
		RestExpressService restExpress = RestExpressService.newBuilder();
		EchoService echoService = new EchoService();
		JaxRsReader.register(restExpress, echoService);
		HttpGet request = null;
		try {
			restExpress.bind();

			HttpClient http = new DefaultHttpClient();
			request = new HttpGet("http://localhost:8081/?echo=ping");
			HttpResponse response = (HttpResponse) http.execute(request);
			assertEquals(HttpResponseStatus.OK.getCode(), response.getStatusLine().getStatusCode());
			HttpEntity entity = response.getEntity();
			assertEquals("\"ping\"", EntityUtils.toString(entity));

		} finally {
			if (request != null)
				request.releaseConnection();
			restExpress.shutdown();
		}
	}

	@Test
	public void sendEchoWithDefaultValue() throws ClientProtocolException, IOException {
		RestExpressService restExpress = RestExpressService.newBuilder();
		EchoService echoService = new EchoService();
		JaxRsReader.register(restExpress, echoService);
		HttpGet request = null;
		try {
			restExpress.bind();

			HttpClient http = new DefaultHttpClient();
			request = new HttpGet("http://localhost:8081/");
			HttpResponse response = (HttpResponse) http.execute(request);
			assertEquals(HttpResponseStatus.OK.getCode(), response.getStatusLine().getStatusCode());
			HttpEntity entity = response.getEntity();
			assertEquals("\"arf\"", EntityUtils.toString(entity));

		} finally {
			if (request != null)
				request.releaseConnection();
			restExpress.shutdown();
		}
	}

	@Test
	public void sendGretting() throws ClientProtocolException, IOException {
		RestExpressService restExpress = RestExpressService.newBuilder();
		EchoService echoService = new EchoService();
		JaxRsReader.register(restExpress, echoService);
		HttpGet request = null;
		try {
			restExpress.bind();

			HttpClient http = new DefaultHttpClient();
			request = new HttpGet("http://localhost:8081/greeting?who=Hal");
			HttpResponse response = (HttpResponse) http.execute(request);
			assertEquals(HttpResponseStatus.OK.getCode(), response.getStatusLine().getStatusCode());
			HttpEntity entity = response.getEntity();
			assertEquals("\"hello Hal!\"", EntityUtils.toString(entity));

		} finally {
			if (request != null)
				request.releaseConnection();
			restExpress.shutdown();
		}
	}

	@Test
	public void sendGrettingByPath() throws ClientProtocolException, IOException {
		RestExpressService restExpress = RestExpressService.newBuilder();
		EchoService echoService = new EchoService();
		JaxRsReader.register(restExpress, echoService);
		HttpGet request = null;
		try {
			restExpress.bind();

			HttpClient http = new DefaultHttpClient();
			request = new HttpGet("http://localhost:8081/restexpress/Hal");
			HttpResponse response = (HttpResponse) http.execute(request);
			assertEquals(HttpResponseStatus.OK.getCode(), response.getStatusLine().getStatusCode());
			HttpEntity entity = response.getEntity();
			assertEquals("\"hello Hal!\"", EntityUtils.toString(entity));

		} finally {
			if (request != null)
				request.releaseConnection();
			restExpress.shutdown();
		}
	}

	@Test
	public void sendEchoWithParameterCasting() throws ClientProtocolException, IOException {
		RestExpressService restExpress = RestExpressService.newBuilder();
		MapperService mapperService = new MapperService();
		assertEquals(1, JaxRsReader.register(restExpress, mapperService));
		HttpGet request = null;
		try {
			restExpress.bind();

			HttpClient http = new DefaultHttpClient();
			request = new HttpGet("http://localhost:8081/?echo=one");
			HttpResponse response = (HttpResponse) http.execute(request);
			assertEquals(HttpResponseStatus.OK.getCode(), response.getStatusLine().getStatusCode());
			HttpEntity entity = response.getEntity();
			assertEquals("\"one index 0\"", EntityUtils.toString(entity));
			request.releaseConnection();

			request = new HttpGet("http://localhost:8081/?echo=one&i=3");
			response = (HttpResponse) http.execute(request);
			assertEquals(HttpResponseStatus.OK.getCode(), response.getStatusLine().getStatusCode());
			entity = response.getEntity();
			assertEquals("\"one index 3\"", EntityUtils.toString(entity));

		} finally {
			if (request != null)
				request.releaseConnection();
			restExpress.shutdown();
		}
	}
}
