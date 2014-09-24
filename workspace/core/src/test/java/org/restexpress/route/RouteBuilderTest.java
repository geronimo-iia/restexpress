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
package org.restexpress.route;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.jboss.netty.handler.codec.http.HttpMethod;
import org.junit.Test;
import org.restexpress.Request;
import org.restexpress.Response;
import org.restexpress.RestExpress;
import org.restexpress.domain.metadata.RouteMetadata;
import org.restexpress.route.RouteBuilder;

public class RouteBuilderTest
{
	@Test
	public void shouldGenerateDefaultMethods()
	{
		RestExpress server = new RestExpress();
		RouteBuilder rb1 = server.uri("/route/builder/test1", new NoopController());
		RouteMetadata md1 = rb1.asMetadata();
		assertEquals(4, md1.getMethods().size());
		assertTrue(md1.getMethods().contains("GET"));
		assertTrue(md1.getMethods().contains("PUT"));
		assertTrue(md1.getMethods().contains("POST"));
		assertTrue(md1.getMethods().contains("DELETE"));
	}

	@Test
	public void shouldGenerateSpecifiedMethods()
	{
		RestExpress server = new RestExpress();
		RouteBuilder rb1 = server.uri("/route/builder/test2", new NoopController())
			.method(HttpMethod.GET, HttpMethod.POST);
		RouteMetadata md1 = rb1.asMetadata();
		assertEquals(2, md1.getMethods().size());
		assertTrue(md1.getMethods().contains("GET"));
		assertTrue(md1.getMethods().contains("POST"));
	}

	@Test
	public void shouldGenerateActionMethods()
	{
		RestExpress server = new RestExpress();
		RouteBuilder rb1 = server.uri("/route/builder/test/{id}.{format}", new NoopController())
			.action("readAll", HttpMethod.GET)
			.method(HttpMethod.POST);
		RouteMetadata md1 = rb1.asMetadata();
		assertEquals(2, md1.getMethods().size());
		assertTrue(md1.getMethods().contains("GET"));
		assertTrue(md1.getMethods().contains("POST"));
	}

	@SuppressWarnings("unused")
	private class NoopController
	{
        public void create(Request request, Response response)
		{
		}

		public void read(Request request, Response response)
		{
		}
		
		public void update(Request request, Response response)
		{
		}
		
		public void delete(Request request, Response response)
		{
		}
		
		public void readAll(Request request, Response response)
		{
		}
	}
}
