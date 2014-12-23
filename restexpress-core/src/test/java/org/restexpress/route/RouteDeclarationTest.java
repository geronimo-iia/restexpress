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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.jboss.netty.handler.codec.http.HttpMethod;
import org.junit.BeforeClass;
import org.junit.Test;
import org.restexpress.Request;
import org.restexpress.Response;
import org.restexpress.route.invoker.StandardInvoker;

/**
 * @author toddf
 * @since Sep 27, 2010
 */
public class RouteDeclarationTest {
	private static final String RAH_ROUTE_NAME = "POST_ONLY";
	private static RouteDeclaration routeDeclarations;
	private static RouteMapping routeMapping;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		routeDeclarations = new RouteDeclaration();
		defineRoutes(routeDeclarations);
		routeMapping = routeDeclarations.createRouteMapping("http://localhost:8081");
	}

	private String findInvokeMethodName(Route r) {
		return ((StandardInvoker) r.invoker()).action().getName();
	}

	@Test
	public void testGetRoutesForNullMethod() {
		routeMapping.getRoutesFor(null);
	}

	@Test
	public void testGetRoutesForGetMethod() {
		List<Route> r = routeMapping.getRoutesFor(HttpMethod.GET);
		assertNotNull(r);
		assertFalse(r.isEmpty());
		assertEquals(4, r.size());
	}

	@Test
	public void testGetRoutesForPutMethod() {
		List<Route> r = routeMapping.getRoutesFor(HttpMethod.PUT);
		assertNotNull(r);
		assertFalse(r.isEmpty());
		assertEquals(1, r.size());
	}

	@Test
	public void testGetRoutesForPostMethod() {
		List<Route> r = routeMapping.getRoutesFor(HttpMethod.POST);
		assertNotNull(r);
		assertFalse(r.isEmpty());
		assertEquals(3, r.size());
	}

	@Test
	public void testGetRoutesForDeleteMethod() {
		List<Route> r = routeMapping.getRoutesFor(HttpMethod.DELETE);
		assertNotNull(r);
		assertFalse(r.isEmpty());
		assertEquals(1, r.size());
	}

	@Test
	public void testGetRoutesForHeadMethod() {
		List<Route> r = routeMapping.getRoutesFor(HttpMethod.HEAD);
		assertNotNull(r);
		assertTrue(r.isEmpty());
	}

	@Test
	public void testGetNullNamedRoute() {
		Route r = routeMapping.getNamedRoute(null, HttpMethod.GET);
		assertNull(r);
	}

	@Test
	public void shouldReturnNullForNamedRouteWithWrongMethod() {
		Route r = routeMapping.getNamedRoute(RAH_ROUTE_NAME, HttpMethod.GET);
		assertNull(r);
	}

	@Test
	public void shouldGetReplyToNamedRoute() {
		Route r = routeMapping.getNamedRoute(RAH_ROUTE_NAME, HttpMethod.POST);
		assertNotNull(r);
		assertEquals(RAH_ROUTE_NAME, r.getName());
		assertEquals("createRah", findInvokeMethodName(r));
	}

	@Test
	public void shouldGetCRUDReadNamedRoute() {
		Route r = routeMapping.getNamedRoute("CRUD_ROUTE", HttpMethod.GET);
		assertNotNull(r);
		assertEquals("CRUD_ROUTE", r.getName());
		assertEquals("read",findInvokeMethodName(r));
	}

	@Test
	public void shouldGetCRUDCreateNamedRoute() {
		Route r = routeMapping.getNamedRoute("CRUD_ROUTE", HttpMethod.POST);
		assertNotNull(r);
		assertEquals("CRUD_ROUTE", r.getName());
		assertEquals("create",findInvokeMethodName(r));
	}

	@Test
	public void shouldGetCRUDUpdateNamedRoute() {
		Route r = routeMapping.getNamedRoute("CRUD_ROUTE", HttpMethod.PUT);
		assertNotNull(r);
		assertEquals("CRUD_ROUTE", r.getName());
		assertEquals("update",findInvokeMethodName(r));
	}

	@Test
	public void shouldGetCRUDDeleteNamedRoute() {
		Route r = routeMapping.getNamedRoute("CRUD_ROUTE", HttpMethod.DELETE);
		assertNotNull(r);
		assertEquals("CRUD_ROUTE", r.getName());
		assertEquals("delete",findInvokeMethodName(r));
	}

	@Test
	public void shouldFindGetMethod() {
		List<HttpMethod> methods = routeMapping.getAllowedMethods("/foo/bar/42.json");
		assertNotNull(methods);
		assertEquals(1, methods.size());
		assertEquals(HttpMethod.GET, methods.get(0));
	}

	@Test
	public void shouldFindPostMethod() {
		List<HttpMethod> methods = routeMapping.getAllowedMethods("/foo.json");
		assertNotNull(methods);
		assertEquals(1, methods.size());
		assertEquals(HttpMethod.POST, methods.get(0));
	}

	@Test
	public void shouldFindMultipleMethods() {
		List<HttpMethod> methods = routeMapping.getAllowedMethods("/foo/foo42.json");
		assertNotNull(methods);
		assertEquals(4, methods.size());
		assertTrue(methods.contains(HttpMethod.GET));
		assertTrue(methods.contains(HttpMethod.PUT));
		assertTrue(methods.contains(HttpMethod.POST));
		assertTrue(methods.contains(HttpMethod.DELETE));
	}

	public static RouteDeclaration defineRoutes(RouteDeclaration routeDeclaration) {
		InnerService service = new InnerService();
		routeDeclaration.uri("/foo/bar/{barId}.{format}", service).action("readBar", HttpMethod.GET);
		routeDeclaration.uri("/foo/bat/{batId}.{format}", service).action("readBat", HttpMethod.GET);
		routeDeclaration.uri("/foo.{format}", service).method(HttpMethod.POST);
		routeDeclaration.uri("/foo/{fooId}.{format}", service).name("CRUD_ROUTE");
		routeDeclaration.uri("/foo/rah/{rahId}.{format}", service).action("createRah", HttpMethod.POST).name(RAH_ROUTE_NAME);
		routeDeclaration.uri("/foo/yada/{yadaId}.{format}", service).action("readYada", HttpMethod.GET);
		return routeDeclaration;
	}

	public static class InnerService {

		public Object create(Request request, Response response) {
			return null;
		}

		public Object read(Request request, Response response) {
			return null;
		}

		public void update(Request request, Response response) {
		}

		public void delete(Request request, Response response) {
		}

		public Object createRah(Request request, Response response) {
			return null;
		}

		public Object readBar(Request request, Response response) {
			return null;
		}

		public Object readBat(Request request, Response response) {
			return null;
		}

		public Object readYada(Request request, Response response) {
			return null;
		}
	}
}
