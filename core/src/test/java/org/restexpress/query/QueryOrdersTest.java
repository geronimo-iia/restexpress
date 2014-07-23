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
/*
    Copyright 2012, Strategic Gains, Inc.

	Licensed under the Apache License, Version 2.0 (the "License");
	you may not use this file except in compliance with the License.
	You may obtain a copy of the License at

		http://www.apache.org/licenses/LICENSE-2.0

	Unless required by applicable law or agreed to in writing, software
	distributed under the License is distributed on an "AS IS" BASIS,
	WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
	See the License for the specific language governing permissions and
	limitations under the License.
 */
package org.restexpress.query;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.intelligentsia.commons.http.exception.BadRequestException;
import org.jboss.netty.handler.codec.http.DefaultHttpRequest;
import org.jboss.netty.handler.codec.http.HttpMethod;
import org.jboss.netty.handler.codec.http.HttpRequest;
import org.jboss.netty.handler.codec.http.HttpVersion;
import org.junit.Test;
import org.restexpress.Request;
import org.restexpress.query.OrderCallback;
import org.restexpress.query.OrderComponent;
import org.restexpress.query.QueryOrder;

/**
 * @author toddf
 * @since Jul 27, 2012
 */
public class QueryOrdersTest {
	@Test
	public void shouldParseQueryString() {
		HttpRequest httpRequest = new DefaultHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.GET, "http://www.example.com/somethings?sort=-name|description|-createdAt");
		Request request = new Request(httpRequest, null);
		QueryOrder o = QueryOrders.parseFrom(request);
		assertTrue(o.isSorted());
		OCallback callback = new OCallback();
		o.iterate(callback);
		assertEquals(3, callback.getCount());
		assertEquals("name", callback.get("name").getFieldName());
		assertTrue(callback.get("name").isDescending());
		assertEquals("description", callback.get("description").getFieldName());
		assertTrue(callback.get("description").isAscending());
		assertEquals("createdAt", callback.get("createdAt").getFieldName());
		assertTrue(callback.get("createdAt").isDescending());
	}

	@Test
	public void shouldParseSortHeader() {
		HttpRequest httpRequest = new DefaultHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.GET, "http://www.example.com/somethings");
		httpRequest.headers().add("sort", "-name|description|-createdAt");
		Request request = new Request(httpRequest, null);
		QueryOrder o = QueryOrders.parseFrom(request);
		assertTrue(o.isSorted());
		OCallback callback = new OCallback();
		o.iterate(callback);
		assertEquals(3, callback.getCount());
		assertEquals("name", callback.get("name").getFieldName());
		assertTrue(callback.get("name").isDescending());
		assertEquals("description", callback.get("description").getFieldName());
		assertTrue(callback.get("description").isAscending());
		assertEquals("createdAt", callback.get("createdAt").getFieldName());
		assertTrue(callback.get("createdAt").isDescending());
	}

	@Test
	public void shouldAllowSupportedSortProperties() {
		HttpRequest httpRequest = new DefaultHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.GET, "http://www.example.com/somethings");
		httpRequest.headers().add("sort", "-name|description|-createdAt");
		Request request = new Request(httpRequest, null);
		QueryOrder o = QueryOrders.parseFrom(request, Arrays.asList(new String[] { "name", "description", "createdAt" }));
		assertTrue(o.isSorted());
		OCallback callback = new OCallback();
		o.iterate(callback);
		assertEquals(3, callback.getCount());
		assertEquals("name", callback.get("name").getFieldName());
		assertTrue(callback.get("name").isDescending());
		assertEquals("description", callback.get("description").getFieldName());
		assertTrue(callback.get("description").isAscending());
		assertEquals("createdAt", callback.get("createdAt").getFieldName());
		assertTrue(callback.get("createdAt").isDescending());
	}

	@Test(expected = BadRequestException.class)
	public void shouldThrowOnInvalidOrderProperty() {
		HttpRequest httpRequest = new DefaultHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.GET, "http://www.example.com/somethings");
		httpRequest.headers().add("sort", "-name|description|-createdAt");
		Request request = new Request(httpRequest, null);
		QueryOrders.parseFrom(request, Arrays.asList(new String[] { "abc", "def", "ghi" }));
	}

	@Test
	public void shouldAllowSingleOrder() {
		HttpRequest httpRequest = new DefaultHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.GET, "http://www.example.com/somethings");
		httpRequest.headers().add("sort", "abc");
		Request request = new Request(httpRequest, null);
		QueryOrder o = QueryOrders.parseFrom(request, Arrays.asList(new String[] { "abc", "def", "ghi" }));
		assertNotNull(o);
		assertTrue(o.isSorted());
		o.iterate(new OrderCallback() {
			private int count = 0;

			@Override
			public void orderBy(OrderComponent component) {
				assertEquals("abc", component.getFieldName());
				assertTrue(component.isAscending());
				++count;
				assertEquals(1, count);
			}
		});
	}

	@Test
	public void shouldAllowSingleOrderAtEnd() {
		HttpRequest httpRequest = new DefaultHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.GET, "http://www.example.com/somethings");
		httpRequest.headers().add("sort", "ghi");
		Request request = new Request(httpRequest, null);
		QueryOrder o = QueryOrders.parseFrom(request, Arrays.asList(new String[] { "abc", "def", "ghi" }));
		assertTrue(o.isSorted());
		o.iterate(new OrderCallback() {
			private int count = 0;

			@Override
			public void orderBy(OrderComponent component) {
				assertEquals("ghi", component.getFieldName());
				assertTrue(component.isAscending());
				++count;
				assertEquals(1, count);
			}
		});
	}

	@Test
	public void shouldAllowSingleDescendingOrder() {
		HttpRequest httpRequest = new DefaultHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.GET, "http://www.example.com/somethings");
		httpRequest.headers().add("sort", "-abc");
		Request request = new Request(httpRequest, null);
		QueryOrder o = QueryOrders.parseFrom(request, Arrays.asList(new String[] { "abc", "def", "ghi" }));
		assertTrue(o.isSorted());
		o.iterate(new OrderCallback() {
			private int count = 0;

			@Override
			public void orderBy(OrderComponent component) {
				assertEquals("abc", component.getFieldName());
				assertTrue(component.isDescending());
				++count;
				assertEquals(1, count);
			}
		});
	}

	@Test
	public void shouldAllowSingleDescendingOrderAtEnd() {
		HttpRequest httpRequest = new DefaultHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.GET, "http://www.example.com/somethings");
		httpRequest.headers().add("sort", "-ghi");
		Request request = new Request(httpRequest, null);
		QueryOrder o = QueryOrders.parseFrom(request, Arrays.asList(new String[] { "abc", "def", "ghi" }));
		assertTrue(o.isSorted());
		o.iterate(new OrderCallback() {
			private int count = 0;

			@Override
			public void orderBy(OrderComponent component) {
				assertEquals("ghi", component.getFieldName());
				assertTrue(component.isDescending());
				++count;
				assertEquals(1, count);
			}
		});
	}

	@Test(expected = BadRequestException.class)
	public void shouldThrowOnSingleInvalidOrder() {
		HttpRequest httpRequest = new DefaultHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.GET, "http://www.example.com/somethings");
		httpRequest.headers().add("sort", "-something");
		Request request = new Request(httpRequest, null);
		QueryOrders.parseFrom(request, Arrays.asList(new String[] { "abc", "def", "ghi" }));
	}

	@Test
	public void shouldAllowSingleAllowedOrder() {
		HttpRequest httpRequest = new DefaultHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.GET, "http://www.example.com/somethings");
		httpRequest.headers().add("sort", "ghi");
		Request request = new Request(httpRequest, null);
		QueryOrder o = QueryOrders.parseFrom(request, Arrays.asList(new String[] { "ghi" }));
		assertNotNull(o);
		assertTrue(o.isSorted());
		assertTrue(o.isSorted());
		o.iterate(new OrderCallback() {
			private int count = 0;

			@Override
			public void orderBy(OrderComponent component) {
				assertEquals("ghi", component.getFieldName());
				assertTrue(component.isAscending());
				++count;
				assertEquals(1, count);
			}
		});
	}

	@Test
	public void shouldAllowSingleDescendingAllowedOrder() {
		HttpRequest httpRequest = new DefaultHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.GET, "http://www.example.com/somethings");
		httpRequest.headers().add("sort", "-ghi");
		Request request = new Request(httpRequest, null);
		QueryOrder o = QueryOrders.parseFrom(request, Arrays.asList(new String[] { "ghi" }));
		assertTrue(o.isSorted());
		o.iterate(new OrderCallback() {
			private int count = 0;

			@Override
			public void orderBy(OrderComponent component) {
				assertEquals("ghi", component.getFieldName());
				assertTrue(component.isDescending());
				++count;
				assertEquals(1, count);
			}
		});
	}

	private class OCallback implements OrderCallback {
		private Map<String, OrderComponent> ocs = new HashMap<String, OrderComponent>();

		@Override
		public void orderBy(OrderComponent component) {
			ocs.put(component.getFieldName(), component);
		}

		public OrderComponent get(String name) {
			return ocs.get(name);
		}

		public int getCount() {
			return ocs.size();
		}
	}
}
