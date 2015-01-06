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

import org.junit.Test;
import org.restexpress.Request;
import org.restexpress.Response;
import org.restexpress.RestExpress;
import org.restexpress.RestExpressService;
import org.restexpress.domain.metadata.RouteMetadata;

/**
 * {@link RouteDefaultMappingTest} test default action mapping.
 *
 * @author <a href="mailto:jguibert@intelligents-ia.com" >Jerome Guibert</a>
 *
 */
public class RouteDefaultMappingTest {

	@Test
	public void shouldGenerateDefaultMethods() {
		RestExpress server = RestExpressService.newBuilder();
		RouteBuilder routeBuilder = server.uri("/route/builder/test1", new SupportedMapping());
		RouteMetadata routeMetadata = routeBuilder.build(server).metadata();
		assertEquals(4, routeMetadata.getMethods().size());
		assertTrue(routeMetadata.getMethods().contains("GET"));
		assertTrue(routeMetadata.getMethods().contains("PUT"));
		assertTrue(routeMetadata.getMethods().contains("POST"));
		assertTrue(routeMetadata.getMethods().contains("DELETE"));
	}

	public class SupportedMapping {
		// standard
		public void create(Request request, Response response) {
		}

		// only response
		public void read(Response response) {
		}

		// only request
		public void update(Request request) {
		}

		// no arg
		public void delete() {
		}
	}

}
