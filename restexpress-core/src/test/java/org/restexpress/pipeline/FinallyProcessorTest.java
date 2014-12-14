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
package org.restexpress.pipeline;

import static org.junit.Assert.assertEquals;

import org.jboss.netty.handler.codec.http.HttpMethod;
import org.junit.Before;
import org.junit.Test;
import org.restexpress.Request;
import org.restexpress.Response;
import org.restexpress.domain.Format;
import org.restexpress.pipeline.handler.RestExpressRequestHandler;
import org.restexpress.pipeline.handler.RestExpressRequestHandlerBuilder;
import org.restexpress.response.Wrapper;
import org.restexpress.route.RouteDeclaration;
import org.restexpress.serialization.JacksonJsonProcessor;
import org.restexpress.serialization.JacksonXmlProcessor;

public class FinallyProcessorTest extends AbstractWrapperResponse {

	protected DummyRoutes routes;

	@Before
	public void initialize() {
		routes = new DummyRoutes();
		routes.defineRoutes();
	}

	@Override
	protected RestExpressRequestHandler build(RestExpressRequestHandlerBuilder builder) throws Exception {
		builder.add(new JacksonJsonProcessor(), Wrapper.newJsendResponseWrapper());
		builder.add(new JacksonXmlProcessor(Format.XML.getMediaType()), Wrapper.newJsendResponseWrapper());
		return super.build(builder);
	}

	@Test
	public void shouldCallAllFinallyProcessors() throws Exception {
		NoopPostprocessor p1 = new NoopPostprocessor();
		NoopPostprocessor p2 = new NoopPostprocessor();
		NoopPostprocessor p3 = new NoopPostprocessor();

		preInitialize(routes);

		builder.addPostprocessor(p1);
		builder.addPostprocessor(p2);
		builder.addPostprocessor(p3);
		builder.addFinallyProcessor(p1);
		builder.addFinallyProcessor(p2);
		builder.addFinallyProcessor(p3);

		postInitialize();

		sendGetEvent("/foo");
		assertEquals(2, p1.getCallCount());
		assertEquals(2, p2.getCallCount());
		assertEquals(2, p3.getCallCount());
	}

	@Test
	public void shouldCallAllFinallyProcessorsOnRouteException() throws Exception {
		NoopPostprocessor p1 = new NoopPostprocessor();
		NoopPostprocessor p2 = new NoopPostprocessor();
		NoopPostprocessor p3 = new NoopPostprocessor();

		preInitialize(routes);

		builder.addPostprocessor(p1);
		builder.addPostprocessor(p2);
		builder.addPostprocessor(p3);
		builder.addFinallyProcessor(p1);
		builder.addFinallyProcessor(p2);
		builder.addFinallyProcessor(p3);

		postInitialize();

		sendGetEvent("/xyzt.html");
		assertEquals(1, p1.getCallCount());
		assertEquals(1, p2.getCallCount());
		assertEquals(1, p3.getCallCount());
	}

	@Test
	public void shouldCallAllFinallyProcessorsOnProcessorException() throws Exception {
		NoopPostprocessor p1 = new ExceptionPostprocessor();
		NoopPostprocessor p2 = new ExceptionPostprocessor();
		NoopPostprocessor p3 = new ExceptionPostprocessor();

		preInitialize(routes);

		builder.addPostprocessor(p1);
		builder.addPostprocessor(p2);
		builder.addPostprocessor(p3);
		builder.addFinallyProcessor(p1);
		builder.addFinallyProcessor(p2);
		builder.addFinallyProcessor(p3);

		postInitialize();

		sendGetEvent("/foo");
		assertEquals(2, p1.getCallCount());
		assertEquals(1, p2.getCallCount());
		assertEquals(1, p3.getCallCount());
	}

	private class NoopPostprocessor implements Postprocessor {
		private int callCount = 0;

		@Override
		public void process(MessageContext context) {
			++callCount;
		}

		public int getCallCount() {
			return callCount;
		}
	}

	private class ExceptionPostprocessor extends NoopPostprocessor {
		@Override
		public void process(MessageContext context) {
			super.process(context);
			throw new RuntimeException("RuntimeException thrown...");
		}
	}

	public static class DummyRoutes extends RouteDeclaration {
		private Object controller = new FooBarController();

		public DummyRoutes defineRoutes() {
			uri("/foo.{format}", controller).action("fooAction", HttpMethod.GET);

			return this;
		}
	}

	public static class FooBarController {
		public void fooAction(Request request, Response response) {
			// do nothing.
		}

	}
}
