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
package org.restexpress.route;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.jboss.netty.handler.codec.http.DefaultHttpRequest;
import org.jboss.netty.handler.codec.http.HttpMethod;
import org.jboss.netty.handler.codec.http.HttpRequest;
import org.jboss.netty.handler.codec.http.HttpVersion;
import org.junit.BeforeClass;
import org.junit.Test;
import org.restexpress.Request;
import org.restexpress.Response;
import org.restexpress.exception.MethodNotAllowedException;
import org.restexpress.route.Action;
import org.restexpress.route.RouteDeclaration;
import org.restexpress.route.RouteMapping;
import org.restexpress.route.RouteResolver;
import org.restexpress.settings.RouteDefaults;

/**
 * @author toddf
 * @since May 16, 2012
 */
public class RouteResolverTest
{
	private static RouteResolver resolver;
	private static RouteDeclaration routeDeclarations;
    private static RouteMapping routeMapping;
	
	@BeforeClass
	public static void setUpBeforeClass()
	throws Exception
	{
		routeDeclarations = new Routes();
		((Routes) routeDeclarations).defineRoutes();
		routeMapping = routeDeclarations.createRouteMapping(new RouteDefaults());
		resolver = new RouteResolver(routeMapping);
	}

	@Test
	public void shouldResolveGetRoute()
	{
		HttpRequest httpRequest = new DefaultHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.GET, "/foo/bar/bar432.json?value=ignored");
		httpRequest.headers().add("Host", "testing-host");
		Request request = new Request(httpRequest, null);
		Action action = resolver.resolve(request);
		assertNotNull(action);
		assertEquals(HttpMethod.GET, action.getRoute().getMethod());
		assertEquals("/foo/bar/{barId}", action.getRoute().getPattern());
	}

	@Test
	public void shouldResolveAliasBarGetRoute()
	{
		HttpRequest httpRequest = new DefaultHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.GET, "/bar/bar432.json?value=ignored");
		httpRequest.headers().add("Host", "testing-host");
		Request request = new Request(httpRequest, null);
		Action action = resolver.resolve(request);
		assertNotNull(action);
		assertEquals(HttpMethod.GET, action.getRoute().getMethod());
		assertEquals("/foo/bar/{barId}", action.getRoute().getPattern());
	}

	@Test
	public void shouldResolvePostRoute()
	{
		HttpRequest httpRequest = new DefaultHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.POST, "/foo.json?value=ignored");
		httpRequest.headers().add("Host", "testing-host");
		Request request = new Request(httpRequest, null);
		Action action = resolver.resolve(request);
		assertNotNull(action);
		assertEquals(HttpMethod.POST, action.getRoute().getMethod());
		assertEquals("/foo", action.getRoute().getPattern());
	}

	@Test
	public void shouldResolveAliasFooPostRoute()
	{
		HttpRequest httpRequest = new DefaultHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.POST, "/yada/yada.json?value=ignored");
		httpRequest.headers().add("Host", "testing-host");
		Request request = new Request(httpRequest, null);
		Action action = resolver.resolve(request);
		assertNotNull(action);
		assertEquals(HttpMethod.POST, action.getRoute().getMethod());
		assertEquals("/foo", action.getRoute().getPattern());
	}

	@Test
	public void shouldResolveCrudRouteForGet()
	{
		HttpRequest httpRequest = new DefaultHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.GET, "/foo/foo23.json?value=ignored");
		httpRequest.headers().add("Host", "testing-host");
		Request request = new Request(httpRequest, null);
		Action action = resolver.resolve(request);
		assertNotNull(action);
		assertEquals(HttpMethod.GET, action.getRoute().getMethod());
		assertEquals("/foo/{fooId}", action.getRoute().getPattern());
	}

	@Test
	public void shouldResolveAliasCrudRouteForGet()
	{
		HttpRequest httpRequest = new DefaultHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.GET, "/blah/foo/foo23.json?value=ignored");
		httpRequest.headers().add("Host", "testing-host");
		Request request = new Request(httpRequest, null);
		Action action = resolver.resolve(request);
		assertNotNull(action);
		assertEquals(HttpMethod.GET, action.getRoute().getMethod());
		assertEquals("/foo/{fooId}", action.getRoute().getPattern());
	}

	@Test
	public void shouldResolveCrudRouteForPut()
	{
		HttpRequest httpRequest = new DefaultHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.PUT, "/foo/foo23.json?value=ignored");
		httpRequest.headers().add("Host", "testing-host");
		Request request = new Request(httpRequest, null);
		Action action = resolver.resolve(request);
		assertNotNull(action);
		assertEquals(HttpMethod.PUT, action.getRoute().getMethod());
		assertEquals("/foo/{fooId}", action.getRoute().getPattern());
	}

	@Test
	public void shouldResolveAliasCrudRouteForPut()
	{
		HttpRequest httpRequest = new DefaultHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.PUT, "/blah/foo/foo23.json?value=ignored");
		httpRequest.headers().add("Host", "testing-host");
		Request request = new Request(httpRequest, null);
		Action action = resolver.resolve(request);
		assertNotNull(action);
		assertEquals(HttpMethod.PUT, action.getRoute().getMethod());
		assertEquals("/foo/{fooId}", action.getRoute().getPattern());
	}

	@Test
	public void shouldResolveCrudRouteForPost()
	{
		HttpRequest httpRequest = new DefaultHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.POST, "/foo/foo23.json?value=ignored");
		httpRequest.headers().add("Host", "testing-host");
		Request request = new Request(httpRequest, null);
		Action action = resolver.resolve(request);
		assertNotNull(action);
		assertEquals(HttpMethod.POST, action.getRoute().getMethod());
		assertEquals("/foo/{fooId}", action.getRoute().getPattern());
	}

	@Test
	public void shouldResolveAliasCrudRouteForPost()
	{
		HttpRequest httpRequest = new DefaultHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.POST, "/blah/foo/foo23.json?value=ignored");
		httpRequest.headers().add("Host", "testing-host");
		Request request = new Request(httpRequest, null);
		Action action = resolver.resolve(request);
		assertNotNull(action);
		assertEquals(HttpMethod.POST, action.getRoute().getMethod());
		assertEquals("/foo/{fooId}", action.getRoute().getPattern());
	}

	@Test
	public void shouldResolveCrudRouteForDelete()
	{
		HttpRequest httpRequest = new DefaultHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.DELETE, "/foo/foo23.json?value=ignored");
		httpRequest.headers().add("Host", "testing-host");
		Request request = new Request(httpRequest, null);
		Action action = resolver.resolve(request);
		assertNotNull(action);
		assertEquals(HttpMethod.DELETE, action.getRoute().getMethod());
		assertEquals("/foo/{fooId}", action.getRoute().getPattern());
	}

	@Test
	public void shouldResolveAliasCrudRouteForDelete()
	{
		HttpRequest httpRequest = new DefaultHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.DELETE, "/blah/foo/foo23.json?value=ignored");
		httpRequest.headers().add("Host", "testing-host");
		Request request = new Request(httpRequest, null);
		Action action = resolver.resolve(request);
		assertNotNull(action);
		assertEquals(HttpMethod.DELETE, action.getRoute().getMethod());
		assertEquals("/foo/{fooId}", action.getRoute().getPattern());
	}

	@Test(expected=MethodNotAllowedException.class)
	public void shouldThrowMethodNotAllowed()
	{
		HttpRequest httpRequest = new DefaultHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.OPTIONS, "/foo/foo23.json?value=ignored");
		httpRequest.headers().add("Host", "testing-host");
		Request request = new Request(httpRequest, null);
		resolver.resolve(request);
	}

	@Test
	public void shouldSendAllowedMethodsForCrudRoute()
	{
		HttpRequest httpRequest = new DefaultHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.OPTIONS, "/foo/foo23.json?value=ignored");
		httpRequest.headers().add("Host", "testing-host");
		Request request = new Request(httpRequest, null);
		try
		{
			resolver.resolve(request);
		}
		catch(MethodNotAllowedException e)
		{
			List<HttpMethod> allowed = e.getAllowedMethods();
			assertEquals(4, allowed.size());
			assertTrue(allowed.contains(HttpMethod.GET));
			assertTrue(allowed.contains(HttpMethod.PUT));
			assertTrue(allowed.contains(HttpMethod.POST));
			assertTrue(allowed.contains(HttpMethod.DELETE));
		}
	}

	@Test
	public void shouldSendAllowedMethodsForPostRoute()
	{
		HttpRequest httpRequest = new DefaultHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.OPTIONS, "/foo.json?value=ignored");
		httpRequest.headers().add("Host", "testing-host");
		Request request = new Request(httpRequest, null);
		try
		{
			resolver.resolve(request);
		}
		catch(MethodNotAllowedException e)
		{
			List<HttpMethod> allowed = e.getAllowedMethods();
			assertEquals(1, allowed.size());
			assertTrue(allowed.contains(HttpMethod.POST));
		}
	}

	@Test
	public void shouldSendAllowedMethodsForGetRoute()
	{
		HttpRequest httpRequest = new DefaultHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.OPTIONS, "/foo/bar/bar23.json?value=ignored");
		httpRequest.headers().add("Host", "testing-host");
		Request request = new Request(httpRequest, null);
		try
		{
			resolver.resolve(request);
		}
		catch(MethodNotAllowedException e)
		{
			List<HttpMethod> allowed = e.getAllowedMethods();
			assertEquals(1, allowed.size());
			assertTrue(allowed.contains(HttpMethod.GET));
		}
	}

	private static class Routes
	extends RouteDeclaration
	{
		private InnerService service;
		private RouteDefaults defaults = new RouteDefaults();
		
		public Routes()
		{
			super();
			service = new InnerService();
		}

        public void defineRoutes()
        {
    		uri("/foo/bar/{barId}.{format}", service, defaults)
    			.alias("/bar/{barId}.{format}")
    			.name("BAR_CRUD_ROUTE")
    			.action("readBar", HttpMethod.GET);

    		uri("/foo.{format}", service, defaults)
    			.alias("/yada/yada.{format}")
    			.method(HttpMethod.POST);

    		uri("/foo/{fooId}.{format}", service, defaults)
    			.alias("/blah/foo/{fooId}.{format}")
    			.name("CRUD_ROUTE");
        }
	}
	
	private static class InnerService
	{
		@SuppressWarnings("unused")
        public Object create(Request request, Response response)
		{
			return null;
		}

		@SuppressWarnings("unused")
        public Object read(Request request, Response response)
		{
			return null;
		}

		@SuppressWarnings("unused")
        public void update(Request request, Response response)
		{
		}

		@SuppressWarnings("unused")
		public void delete(Request request, Response response)
		{
		}

		@SuppressWarnings("unused")
		public Object readBar(Request request, Response response)
		{
			return null;
		}
	}
}
