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
 Copyright 2010, Strategic Gains, Inc.

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
package org.restexpress.pipeline;

import static org.junit.Assert.assertEquals;

import java.net.InetSocketAddress;
import java.nio.charset.Charset;

import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.UpstreamMessageEvent;
import org.jboss.netty.handler.codec.http.DefaultHttpRequest;
import org.jboss.netty.handler.codec.http.HttpMethod;
import org.jboss.netty.handler.codec.http.HttpRequest;
import org.jboss.netty.handler.codec.http.HttpVersion;
import org.junit.Before;
import org.junit.Test;
import org.restexpress.plugin.xstream.XstreamXmlProcessor;
import org.restexpress.response.Wrapper;
import org.restexpress.route.RouteDeclaration;
import org.restexpress.serialization.json.jackson.JacksonJsonProcessor;

/**
 * @author toddf
 * @since Dec 15, 2010
 */
public class RawWrappedResponseTest extends AbstractWrapperResponse {

	@Before
	public void initialize() throws Exception {
		DummyRoutes routes = new DummyRoutes();
		routes.defineRoutes();
		initialize(routes);
		messageHandler.serializationProvider().add(new JacksonJsonProcessor(), Wrapper.newRawResponseWrapper(), true);
		messageHandler.serializationProvider().add(new XstreamXmlProcessor(), Wrapper.newRawResponseWrapper());
	}

	@Test
	public void shouldWrapGetInRawJson() {
		sendEvent(HttpMethod.GET, "/normal_get.json", null);
		assertEquals(1, observer.getReceivedCount());
		assertEquals(1, observer.getCompleteCount());
		assertEquals(1, observer.getSuccessCount());
		assertEquals(0, observer.getExceptionCount());

		assertEquals("\"Normal GET action\"", responseBody.toString());
	}

	@Test
	public void shouldWrapGetInRawJsonUsingQueryString() {
		sendEvent(HttpMethod.GET, "/normal_get?format=json", null);
		assertEquals(1, observer.getReceivedCount());
		assertEquals(1, observer.getCompleteCount());
		assertEquals(1, observer.getSuccessCount());
		assertEquals(0, observer.getExceptionCount());

		assertEquals("\"Normal GET action\"", responseBody.toString());
	}

	@Test
	public void shouldWrapGetInRawXml() {
		sendEvent(HttpMethod.GET, "/normal_get.xml", null);
		assertEquals(1, observer.getReceivedCount());
		assertEquals(1, observer.getCompleteCount());
		assertEquals(1, observer.getSuccessCount());
		assertEquals(0, observer.getExceptionCount());

		assertEquals("<string>Normal GET action</string>", responseBody.toString());
	}

	@Test
	public void shouldWrapGetInRawXmlUsingQueryString() {
		sendEvent(HttpMethod.GET, "/normal_get?format=xml", null);
		assertEquals(1, observer.getReceivedCount());
		assertEquals(1, observer.getCompleteCount());
		assertEquals(1, observer.getSuccessCount());
		assertEquals(0, observer.getExceptionCount());

		assertEquals("<string>Normal GET action</string>", responseBody.toString());
	}

	@Test
	public void shouldWrapPutInRawJson() {
		sendEvent(HttpMethod.PUT, "/normal_put.json", null);
		assertEquals(1, observer.getReceivedCount());
		assertEquals(1, observer.getCompleteCount());
		assertEquals(1, observer.getSuccessCount());
		assertEquals(0, observer.getExceptionCount());

		assertEquals("\"Normal PUT action\"", responseBody.toString());
	}

	@Test
	public void shouldWrapPutInRawJsonUsingQueryString() {
		sendEvent(HttpMethod.PUT, "/normal_put?format=json", null);
		assertEquals(1, observer.getReceivedCount());
		assertEquals(1, observer.getCompleteCount());
		assertEquals(1, observer.getSuccessCount());
		assertEquals(0, observer.getExceptionCount());

		assertEquals("\"Normal PUT action\"", responseBody.toString());
	}

	@Test
	public void shouldWrapPutInRawXml() {
		sendEvent(HttpMethod.PUT, "/normal_put.xml", null);
		assertEquals(1, observer.getReceivedCount());
		assertEquals(1, observer.getCompleteCount());
		assertEquals(1, observer.getSuccessCount());
		assertEquals(0, observer.getExceptionCount());

		assertEquals("<string>Normal PUT action</string>", responseBody.toString());
	}

	@Test
	public void shouldWrapPutInRawXmlUsingQueryString() {
		sendEvent(HttpMethod.PUT, "/normal_put?format=xml", null);
		assertEquals(1, observer.getReceivedCount());
		assertEquals(1, observer.getCompleteCount());
		assertEquals(1, observer.getSuccessCount());
		assertEquals(0, observer.getExceptionCount());

		assertEquals("<string>Normal PUT action</string>", responseBody.toString());
	}

	@Test
	public void shouldWrapPostInRawJson() {
		sendEvent(HttpMethod.POST, "/normal_post.json", null);
		assertEquals(1, observer.getReceivedCount());
		assertEquals(1, observer.getCompleteCount());
		assertEquals(1, observer.getSuccessCount());
		assertEquals(0, observer.getExceptionCount());

		assertEquals("\"Normal POST action\"", responseBody.toString());
	}

	@Test
	public void shouldWrapPostInRawJsonUsingQueryString() {
		sendEvent(HttpMethod.POST, "/normal_post?format=json", null);
		assertEquals(1, observer.getReceivedCount());
		assertEquals(1, observer.getCompleteCount());
		assertEquals(1, observer.getSuccessCount());
		assertEquals(0, observer.getExceptionCount());

		assertEquals("\"Normal POST action\"", responseBody.toString());
	}

	@Test
	public void shouldWrapPostInRawXml() {
		sendEvent(HttpMethod.POST, "/normal_post.xml", null);
		assertEquals(1, observer.getReceivedCount());
		assertEquals(1, observer.getCompleteCount());
		assertEquals(1, observer.getSuccessCount());
		assertEquals(0, observer.getExceptionCount());

		assertEquals("<string>Normal POST action</string>", responseBody.toString());
	}

	@Test
	public void shouldWrapPostInRawXmlUsingQueryString() {
		sendEvent(HttpMethod.POST, "/normal_post?format=xml", null);
		assertEquals(1, observer.getReceivedCount());
		assertEquals(1, observer.getCompleteCount());
		assertEquals(1, observer.getSuccessCount());
		assertEquals(0, observer.getExceptionCount());

		assertEquals("<string>Normal POST action</string>", responseBody.toString());
	}

	@Test
	public void shouldWrapDeleteInRawJson() {
		sendEvent(HttpMethod.DELETE, "/normal_delete.json", null);
		assertEquals(1, observer.getReceivedCount());
		assertEquals(1, observer.getCompleteCount());
		assertEquals(1, observer.getSuccessCount());
		assertEquals(0, observer.getExceptionCount());

		assertEquals("\"Normal DELETE action\"", responseBody.toString());
	}

	@Test
	public void shouldWrapDeleteInRawJsonUsingQueryString() {
		sendEvent(HttpMethod.DELETE, "/normal_delete?format=json", null);
		assertEquals(1, observer.getReceivedCount());
		assertEquals(1, observer.getCompleteCount());
		assertEquals(1, observer.getSuccessCount());
		assertEquals(0, observer.getExceptionCount());

		assertEquals("\"Normal DELETE action\"", responseBody.toString());
	}

	@Test
	public void shouldWrapDeleteInRawXml() {
		sendEvent(HttpMethod.DELETE, "/normal_delete.xml", null);
		assertEquals(1, observer.getReceivedCount());
		assertEquals(1, observer.getCompleteCount());
		assertEquals(1, observer.getSuccessCount());
		assertEquals(0, observer.getExceptionCount());

		assertEquals("<string>Normal DELETE action</string>", responseBody.toString());
	}

	@Test
	public void shouldWrapDeleteInRawXmlUsingQueryString() {
		sendEvent(HttpMethod.DELETE, "/normal_delete?format=xml", null);
		assertEquals(1, observer.getReceivedCount());
		assertEquals(1, observer.getCompleteCount());
		assertEquals(1, observer.getSuccessCount());
		assertEquals(0, observer.getExceptionCount());

		assertEquals("<string>Normal DELETE action</string>", responseBody.toString());
	}

	@Test
	public void shouldWrapNotFoundInRawJson() {
		sendEvent(HttpMethod.GET, "/not_found.json", null);
		assertEquals(1, observer.getReceivedCount());
		assertEquals(1, observer.getCompleteCount());
		assertEquals(0, observer.getSuccessCount());
		assertEquals(1, observer.getExceptionCount());

		assertEquals("\"Item not found\"", responseBody.toString());
	}

	@Test
	public void shouldWrapNotFoundInRawJsonUsingQueryString() {
		sendEvent(HttpMethod.GET, "/not_found?format=json", null);
		assertEquals(1, observer.getReceivedCount());
		assertEquals(1, observer.getCompleteCount());
		assertEquals(0, observer.getSuccessCount());
		assertEquals(1, observer.getExceptionCount());

		assertEquals("\"Item not found\"", responseBody.toString());
	}

	@Test
	public void shouldWrapNotFoundInRawXml() {
		sendEvent(HttpMethod.GET, "/not_found.xml", null);
		assertEquals(1, observer.getReceivedCount());
		assertEquals(1, observer.getCompleteCount());
		assertEquals(0, observer.getSuccessCount());
		assertEquals(1, observer.getExceptionCount());

		assertEquals("<string>Item not found</string>", responseBody.toString());
	}

	@Test
	public void shouldWrapNotFoundInRawXmlUsingQueryString() {
		sendEvent(HttpMethod.GET, "/not_found?format=xml", null);
		assertEquals(1, observer.getReceivedCount());
		assertEquals(1, observer.getCompleteCount());
		assertEquals(0, observer.getSuccessCount());
		assertEquals(1, observer.getExceptionCount());

		assertEquals("<string>Item not found</string>", responseBody.toString());
	}

	@Test
	public void shouldWrapNullPointerInRawJson() {
		sendEvent(HttpMethod.GET, "/null_pointer.json", null);
		assertEquals(1, observer.getReceivedCount());
		assertEquals(1, observer.getCompleteCount());
		assertEquals(0, observer.getSuccessCount());
		assertEquals(1, observer.getExceptionCount());

		assertEquals("\"Null and void\"", responseBody.toString());
	}

	@Test
	public void shouldWrapNullPointerInRawJsonUsingQueryString() {
		sendEvent(HttpMethod.GET, "/null_pointer?format=json", null);
		assertEquals(1, observer.getReceivedCount());
		assertEquals(1, observer.getCompleteCount());
		assertEquals(0, observer.getSuccessCount());
		assertEquals(1, observer.getExceptionCount());

		assertEquals("\"Null and void\"", responseBody.toString());
	}

	@Test
	public void shouldWrapNullPointerInRawXml() {
		sendEvent(HttpMethod.GET, "/null_pointer.xml", null);
		assertEquals(1, observer.getReceivedCount());
		assertEquals(1, observer.getCompleteCount());
		assertEquals(0, observer.getSuccessCount());
		assertEquals(1, observer.getExceptionCount());

		assertEquals("<string>Null and void</string>", responseBody.toString());
	}

	@Test
	public void shouldWrapNullPointerInRawXmlUsingQueryString() {
		sendEvent(HttpMethod.GET, "/null_pointer?format=xml", null);
		assertEquals(1, observer.getReceivedCount());
		assertEquals(1, observer.getCompleteCount());
		assertEquals(0, observer.getSuccessCount());
		assertEquals(1, observer.getExceptionCount());

		assertEquals("<string>Null and void</string>", responseBody.toString());
	}

	@Test
	public void shouldWrapInvalidUrlWithRawJson() {
		sendEvent(HttpMethod.GET, "/xyzt.json", null);
		assertEquals(1, observer.getReceivedCount());
		assertEquals(1, observer.getCompleteCount());
		assertEquals(0, observer.getSuccessCount());
		assertEquals(1, observer.getExceptionCount());

		assertEquals("\"Unresolvable URL: http://null/xyzt.json\"", responseBody.toString());
	}

	@Test
	public void shouldWrapInvalidUrlWithRawJsonUsingQueryString() {
		sendEvent(HttpMethod.GET, "/xyzt?format=json", null);
		assertEquals(1, observer.getReceivedCount());
		assertEquals(1, observer.getCompleteCount());
		assertEquals(0, observer.getSuccessCount());
		assertEquals(1, observer.getExceptionCount());

		assertEquals("\"Unresolvable URL: http://null/xyzt?format=json\"", responseBody.toString());
	}

	@Test
	public void shouldWrapInvalidUrlWithRawXml() {
		sendEvent(HttpMethod.GET, "/xyzt.xml", null);
		assertEquals(1, observer.getReceivedCount());
		assertEquals(1, observer.getCompleteCount());
		assertEquals(0, observer.getSuccessCount());
		assertEquals(1, observer.getExceptionCount());

		assertEquals("<string>Unresolvable URL: http://null/xyzt.xml</string>", responseBody.toString());
	}

	@Test
	public void shouldWrapInvalidUrlWithXmlUsingQueryString() {
		sendEvent(HttpMethod.GET, "/xyzt?format=xml", null);
		assertEquals(1, observer.getReceivedCount());
		assertEquals(1, observer.getCompleteCount());
		assertEquals(0, observer.getSuccessCount());
		assertEquals(1, observer.getExceptionCount());

		assertEquals("<string>Unresolvable URL: http://null/xyzt?format=xml</string>", responseBody.toString());
	}

	@Test
	public void shouldDeleteWithoutContent() {
		sendEvent(HttpMethod.DELETE, "/no_content_delete.json", null);
		assertEquals(1, observer.getReceivedCount());
		assertEquals(1, observer.getCompleteCount());
		assertEquals(1, observer.getSuccessCount());
		assertEquals(0, observer.getExceptionCount());

		assertEquals("null", responseBody.toString());
	}

	@Test
	public void shouldThrowExceptionOnDeleteNoContentContainingBody() {
		sendEvent(HttpMethod.DELETE, "/no_content_with_body_delete.json", null);
		assertEquals(1, observer.getReceivedCount());
		assertEquals(1, observer.getCompleteCount());
		assertEquals(0, observer.getSuccessCount());
		assertEquals(1, observer.getExceptionCount());

	}

	@Test
	public void shouldDeleteIgnoringJsonp() {
		sendEvent(HttpMethod.DELETE, "/normal_delete.json?jsonp=jsonp_callback", null);
		assertEquals(1, observer.getReceivedCount());
		assertEquals(1, observer.getCompleteCount());
		assertEquals(1, observer.getSuccessCount());
		assertEquals(0, observer.getExceptionCount());

		assertEquals("\"Normal DELETE action\"", responseBody.toString());
	}

	private void sendEvent(HttpMethod method, String path, String body) {
		HttpRequest request = new DefaultHttpRequest(HttpVersion.HTTP_1_1, method, path);

		if (body != null) {
			request.setContent(ChannelBuffers.copiedBuffer(body, Charset.defaultCharset()));
		}

		pl.sendUpstream(new UpstreamMessageEvent(channel, request, new InetSocketAddress(1)));
	}

	public class DummyRoutes extends RouteDeclaration {
		private Object controller = new WrappedResponseController();

		public void defineRoutes() {
			uri("/normal_get.{format}", controller).action("normalGetAction", HttpMethod.GET);

			uri("/normal_put.{format}", controller).action("normalPutAction", HttpMethod.PUT);

			uri("/normal_post.{format}", controller).action("normalPostAction", HttpMethod.POST);

			uri("/normal_delete.{format}", controller).action("normalDeleteAction", HttpMethod.DELETE);

			uri("/no_content_delete.{format}", controller).action("noContentDeleteAction", HttpMethod.DELETE);

			uri("/no_content_with_body_delete.{format}", controller).action("noContentWithBodyDeleteActionThrowException", HttpMethod.DELETE);

			uri("/not_found.{format}", controller).action("notFoundAction", HttpMethod.GET);

			uri("/null_pointer.{format}", controller).action("nullPointerAction", HttpMethod.GET);
		}
	}
}
