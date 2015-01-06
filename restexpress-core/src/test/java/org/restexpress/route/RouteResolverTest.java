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
import org.restexpress.RestExpress;
import org.restexpress.RestExpressService;
import org.restexpress.TestToolKit;
import org.restexpress.exception.MethodNotAllowedException;
import org.restexpress.http.HttpHeader;
import org.restexpress.http.HttpMethods;
import org.restexpress.pipeline.MessageContext;

/**
 * @author toddf
 * @since May 16, 2012
 */
public class RouteResolverTest {
    private static RouteResolver resolver;
    private static RouteDeclaration routeDeclarations;
	private static RestExpress restExpress;


    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        routeDeclarations = new RouteDeclaration();
        defineRoutes(routeDeclarations);
    	restExpress = RestExpressService.newBuilder();
    	restExpress.settings().serverSettings().setBaseUrl("http://localhost:8081");
        resolver = routeDeclarations.createRouteMapping(restExpress);
    }

    @Test
    public void shouldResolveGetRoute() {
        HttpRequest httpRequest = new DefaultHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.GET, "/foo/bar/bar432.json?value=ignored");
        httpRequest.headers().add("Host", "testing-host");
        Request request = TestToolKit.newRequest(httpRequest);
        Action action = resolver.resolve(new MessageContext(request, new Response()));
        assertNotNull(action);
        assertEquals(HttpMethod.GET, action.resolvedRoute().getMethod());
        assertEquals("/foo/bar/{barId}", action.resolvedRoute().getPattern());
    }

    @Test
    public void shouldResolveAliasBarGetRoute() {
        HttpRequest httpRequest = new DefaultHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.GET, "/bar/bar432.json?value=ignored");
        httpRequest.headers().add("Host", "testing-host");
        Request request = TestToolKit.newRequest(httpRequest);
        Action action = resolver.resolve(new MessageContext(request, new Response()));
        assertNotNull(action);
        assertEquals(HttpMethod.GET, action.resolvedRoute().getMethod());
        assertEquals("/foo/bar/{barId}", action.resolvedRoute().getPattern());
    }

    @Test
    public void shouldResolvePostRoute() {
        HttpRequest httpRequest = new DefaultHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.POST, "/foo.json?value=ignored");
        httpRequest.headers().add("Host", "testing-host");
        Request request = TestToolKit.newRequest(httpRequest);
        Action action = resolver.resolve(new MessageContext(request, new Response()));
        assertNotNull(action);
        assertEquals(HttpMethod.POST, action.resolvedRoute().getMethod());
        assertEquals("/foo", action.resolvedRoute().getPattern());
    }

    @Test
    public void shouldResolveAliasFooPostRoute() {
        HttpRequest httpRequest = new DefaultHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.POST, "/yada/yada.json?value=ignored");
        httpRequest.headers().add("Host", "testing-host");
        Request request = TestToolKit.newRequest(httpRequest);
        Action action = resolver.resolve(new MessageContext(request, new Response()));
        assertNotNull(action);
        assertEquals(HttpMethod.POST, action.resolvedRoute().getMethod());
        assertEquals("/foo", action.resolvedRoute().getPattern());
    }

    @Test
    public void shouldResolveCrudRouteForGet() {
        HttpRequest httpRequest = new DefaultHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.GET, "/foo/foo23.json?value=ignored");
        httpRequest.headers().add("Host", "testing-host");
        Request request = TestToolKit.newRequest(httpRequest);
        Action action = resolver.resolve(new MessageContext(request, new Response()));
        assertNotNull(action);
        assertEquals(HttpMethod.GET, action.resolvedRoute().getMethod());
        assertEquals("/foo/{fooId}", action.resolvedRoute().getPattern());
    }

    @Test
    public void shouldResolveAliasCrudRouteForGet() {
        HttpRequest httpRequest = new DefaultHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.GET, "/blah/foo/foo23.json?value=ignored");
        httpRequest.headers().add("Host", "testing-host");
        Request request = TestToolKit.newRequest(httpRequest);
        Action action = resolver.resolve(new MessageContext(request, new Response()));
        assertNotNull(action);
        assertEquals(HttpMethod.GET, action.resolvedRoute().getMethod());
        assertEquals("/foo/{fooId}", action.resolvedRoute().getPattern());
    }

    @Test
    public void shouldResolveCrudRouteForPut() {
        HttpRequest httpRequest = new DefaultHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.PUT, "/foo/foo23.json?value=ignored");
        httpRequest.headers().add("Host", "testing-host");
        Request request = TestToolKit.newRequest(httpRequest);
        Action action = resolver.resolve(new MessageContext(request, new Response()));
        assertNotNull(action);
        assertEquals(HttpMethod.PUT, action.resolvedRoute().getMethod());
        assertEquals("/foo/{fooId}", action.resolvedRoute().getPattern());
    }

    @Test
    public void shouldResolveAliasCrudRouteForPut() {
        HttpRequest httpRequest = new DefaultHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.PUT, "/blah/foo/foo23.json?value=ignored");
        httpRequest.headers().add("Host", "testing-host");
        Request request = TestToolKit.newRequest(httpRequest);
        Action action = resolver.resolve(new MessageContext(request, new Response()));
        assertNotNull(action);
        assertEquals(HttpMethod.PUT, action.resolvedRoute().getMethod());
        assertEquals("/foo/{fooId}", action.resolvedRoute().getPattern());
    }

    @Test
    public void shouldResolveCrudRouteForPost() {
        HttpRequest httpRequest = new DefaultHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.POST, "/foo/foo23.json?value=ignored");
        httpRequest.headers().add("Host", "testing-host");
        Request request = TestToolKit.newRequest(httpRequest);
        Action action = resolver.resolve(new MessageContext(request, new Response()));
        assertNotNull(action);
        assertEquals(HttpMethod.POST, action.resolvedRoute().getMethod());
        assertEquals("/foo/{fooId}", action.resolvedRoute().getPattern());
    }

    @Test
    public void shouldResolveAliasCrudRouteForPost() {
        HttpRequest httpRequest = new DefaultHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.POST, "/blah/foo/foo23.json?value=ignored");
        httpRequest.headers().add("Host", "testing-host");
        Request request = TestToolKit.newRequest(httpRequest);
        Action action = resolver.resolve(new MessageContext(request, new Response()));
        assertNotNull(action);
        assertEquals(HttpMethod.POST, action.resolvedRoute().getMethod());
        assertEquals("/foo/{fooId}", action.resolvedRoute().getPattern());
    }

    @Test
    public void shouldResolveCrudRouteForDelete() {
        HttpRequest httpRequest = new DefaultHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.DELETE, "/foo/foo23.json?value=ignored");
        httpRequest.headers().add("Host", "testing-host");
        Request request = TestToolKit.newRequest(httpRequest);
        Action action = resolver.resolve(new MessageContext(request, new Response()));
        assertNotNull(action);
        assertEquals(HttpMethod.DELETE, action.resolvedRoute().getMethod());
        assertEquals("/foo/{fooId}", action.resolvedRoute().getPattern());
    }

    @Test
    public void shouldResolveAliasCrudRouteForDelete() {
        HttpRequest httpRequest = new DefaultHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.DELETE, "/blah/foo/foo23.json?value=ignored");
        httpRequest.headers().add("Host", "testing-host");
        Request request = TestToolKit.newRequest(httpRequest);
        Action action = resolver.resolve(new MessageContext(request, new Response()));
        assertNotNull(action);
        assertEquals(HttpMethod.DELETE, action.resolvedRoute().getMethod());
        assertEquals("/foo/{fooId}", action.resolvedRoute().getPattern());
    }

    @Test(expected = MethodNotAllowedException.class)
    public void shouldThrowMethodNotAllowed() {
        HttpRequest httpRequest = new DefaultHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.OPTIONS, "/foo/foo23.json?value=ignored");
        httpRequest.headers().add("Host", "testing-host");
        Request request = TestToolKit.newRequest(httpRequest);
        resolver.resolve(new MessageContext(request, new Response()));
    }

    @Test
    public void shouldSendAllowedMethodsForCrudRoute() {
        HttpRequest httpRequest = new DefaultHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.OPTIONS, "/foo/foo23.json?value=ignored");
        httpRequest.headers().add("Host", "testing-host");
        Request request = TestToolKit.newRequest(httpRequest);
        MessageContext context = new MessageContext(request, new Response());
        try {
            resolver.resolve(context);
        } catch (MethodNotAllowedException e) {
            List<String> allowed = context.getResponse().getHeaders(HttpHeader.ALLOW);
            assertEquals(4, allowed.size());
            assertTrue(allowed.contains(HttpMethods.GET.toString()));
            assertTrue(allowed.contains(HttpMethods.PUT.toString()));
            assertTrue(allowed.contains(HttpMethods.POST.toString()));
            assertTrue(allowed.contains(HttpMethods.DELETE.toString()));
        }
    }

    @Test
    public void shouldSendAllowedMethodsForPostRoute() {
        HttpRequest httpRequest = new DefaultHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.OPTIONS, "/foo.json?value=ignored");
        httpRequest.headers().add("Host", "testing-host");
        Request request = TestToolKit.newRequest(httpRequest);
        MessageContext context = new MessageContext(request, new Response());
        try {
            resolver.resolve(context);
        } catch (MethodNotAllowedException e) {

            List<String> allowed = context.getResponse().getHeaders(HttpHeader.ALLOW);
            assertEquals(1, allowed.size());
            assertTrue(allowed.contains(HttpMethods.POST.toString()));
        }
    }

    @Test
    public void shouldSendAllowedMethodsForGetRoute() {
        HttpRequest httpRequest = new DefaultHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.OPTIONS, "/foo/bar/bar23.json?value=ignored");
        httpRequest.headers().add("Host", "testing-host");
        Request request = TestToolKit.newRequest(httpRequest);
        MessageContext context = new MessageContext(request, new Response());
        try {
            resolver.resolve(context);
        } catch (MethodNotAllowedException e) {
            List<String> allowed = context.getResponse().getHeaders(HttpHeader.ALLOW);
            assertEquals(1, allowed.size());
            assertTrue(allowed.contains(HttpMethods.GET.toString()));
        }
    }

    public static RouteDeclaration defineRoutes(RouteDeclaration routeDeclaration) {
        InnerService service = new InnerService();
        routeDeclaration.uri("/foo/bar/{barId}.{format}", service).alias("/bar/{barId}.{format}").name("BAR_CRUD_ROUTE")
                .action("readBar", HttpMethod.GET);
        routeDeclaration.uri("/foo.{format}", service).alias("/yada/yada.{format}").method(HttpMethod.POST);
        routeDeclaration.uri("/foo/{fooId}.{format}", service).alias("/blah/foo/{fooId}.{format}").name("CRUD_ROUTE");
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

        public Object readBar(Request request, Response response) {
            return null;
        }
    }
}
