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
import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.restexpress.RestExpress;
import org.restexpress.RestExpressService;
import org.restexpress.route.parameterized.ParameterizedRouteBuilder;
import org.restexpress.route.regex.RegexRouteBuilder;

public class RouterMappingTest {

	protected RestExpress restExpress;

	@Before
	public void before() {
		restExpress = RestExpressService.newBuilder();
	}

	@Test
	public void shouldPrefixUriWithSlash() {
		String prefix = "foo";
		ParameterizedRouteBuilder rb = new ParameterizedRouteBuilder(prefix, new NoopController());
		List<Route> routes = rb.build(restExpress).routes();
		assertNotNull(routes);
		assertEquals(4, routes.size());
		assertEquals("/" + prefix, routes.get(0).getPattern());
	}

	@Test
	public void shouldNotModifyUri() {
		String pattern = "^/foo(.*)";
		RegexRouteBuilder rb = new RegexRouteBuilder(pattern, new NoopController());
		List<Route> routes = rb.build(restExpress).routes();
		assertNotNull(routes);
		assertEquals(4, routes.size());
		assertEquals(pattern, routes.get(0).getPattern());
	}

	@Test
	public void shouldMapBasicController() {
		String prefix = "foo";
		ParameterizedRouteBuilder rb = new ParameterizedRouteBuilder(prefix, new BasicController());
		List<Route> routes = rb.build(restExpress).routes();
		assertNotNull(routes);
		assertEquals(1, routes.size());
		assertEquals("/" + prefix, routes.get(0).getPattern());
	}
}
