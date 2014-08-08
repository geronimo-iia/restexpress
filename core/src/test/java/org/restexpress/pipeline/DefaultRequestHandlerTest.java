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
import static org.junit.Assert.assertTrue;

import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.util.List;

import org.intelligentsia.commons.http.exception.BadRequestException;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.UpstreamMessageEvent;
import org.jboss.netty.handler.codec.http.DefaultHttpRequest;
import org.jboss.netty.handler.codec.http.HttpHeaders;
import org.jboss.netty.handler.codec.http.HttpMethod;
import org.jboss.netty.handler.codec.http.HttpRequest;
import org.jboss.netty.handler.codec.http.HttpVersion;
import org.junit.Before;
import org.junit.Test;
import org.restexpress.Request;
import org.restexpress.Response;
import org.restexpress.SerializationProvider;
import org.restexpress.domain.CharacterSet;
import org.restexpress.domain.Format;
import org.restexpress.domain.MediaType;
import org.restexpress.response.Wrapper;
import org.restexpress.route.RouteDeclaration;
import org.restexpress.serialization.json.jackson.JacksonJsonProcessor;
import org.restexpress.serialization.xml.jackson.JacksonXmlProcessor;

/**
 * @author toddf
 * @since Dec 15, 2010
 */
public class DefaultRequestHandlerTest extends AbstractWrapperResponse {

	@Before
	public void initialize() throws Exception {
		DummyRoutes routes = new DummyRoutes();
		routes.defineRoutes();
		initialize(routes);
		SerializationProvider provider = messageHandler.serializationProvider();
		provider.add(new JacksonJsonProcessor(), Wrapper.newJsendResponseWrapper());
		provider.add(new JacksonXmlProcessor(Format.XML.getMediaType()), Wrapper.newJsendResponseWrapper());
		provider.alias("dated", Dated.class);
	}

	@Test
	public void shouldReturnTextPlainContentTypeByDefault() throws Exception {
		sendGetEvent("/unserializedDefault");
		assertEquals(0, observer.getExceptionCount());
		assertEquals(1, observer.getReceivedCount());
		assertEquals(1, observer.getCompleteCount());
		assertEquals(1, observer.getSuccessCount());
		// System.out.println(responseBody.toString());
		assertEquals("should be text plain, here", responseBody.toString());
		assertTrue(responseHeaders.containsKey("Content-Type"));
		List<String> contentTypes = responseHeaders.get(HttpHeaders.Names.CONTENT_TYPE);
		assertEquals(1, contentTypes.size());
		assertEquals("text/plain; charset=UTF-8", contentTypes.get(0));
	}

	@Test
	public void shouldAllowSettingOfContentType() throws Exception {
		sendGetEvent("/unserialized");
		assertEquals(0, observer.getExceptionCount());
		assertEquals(1, observer.getReceivedCount());
		assertEquals(1, observer.getCompleteCount());
		assertEquals(1, observer.getSuccessCount());
		// System.out.println(responseBody.toString());
		assertEquals("<html><body>Some kinda wonderful!</body></html>", responseBody.toString());
		assertTrue(responseHeaders.containsKey("Content-Type"));
		List<String> contentTypes = responseHeaders.get(HttpHeaders.Names.CONTENT_TYPE);
		assertEquals(1, contentTypes.size());
		assertEquals("text/html", contentTypes.get(0));
	}

	@Test
	public void shouldAllowSettingOfContentTypeViaHeader() throws Exception {
		sendGetEvent("/unserializedToo");
		assertEquals(0, observer.getExceptionCount());
		assertEquals(1, observer.getReceivedCount());
		assertEquals(1, observer.getCompleteCount());
		assertEquals(1, observer.getSuccessCount());
		// System.out.println(responseBody.toString());
		assertEquals("<html><body>Wow! What a fabulous HTML body...</body></html>", responseBody.toString());
		List<String> contentTypes = responseHeaders.get(HttpHeaders.Names.CONTENT_TYPE);
		assertEquals(1, contentTypes.size());
		assertEquals("text/html", contentTypes.get(0));
	}

	@Test
	public void shouldAllowSettingOfArbitraryBody() throws Exception {
		sendGetEvent("/setBodyAction.html");
		assertEquals(0, observer.getExceptionCount());
		assertEquals(1, observer.getReceivedCount());
		assertEquals(1, observer.getCompleteCount());
		assertEquals(1, observer.getSuccessCount());
		// System.out.println(responseBody.toString());
		assertEquals("<html><body>Arbitrarily set HTML body...</body></html>", responseBody.toString());
		List<String> contentTypes = responseHeaders.get(HttpHeaders.Names.CONTENT_TYPE);
		assertEquals(1, contentTypes.size());
		assertEquals(MediaType.TEXT_HTML.withCharset(CharacterSet.UTF_8.getCharsetName()), contentTypes.get(0));
	}

	@Test
	public void shouldNotifyObserverOnSuccess() throws Exception {
		sendGetEvent("/foo");
		assertEquals(1, observer.getReceivedCount());
		assertEquals(1, observer.getCompleteCount());
		assertEquals(1, observer.getSuccessCount());
		assertEquals(0, observer.getExceptionCount());

		assertEquals("{\"status\":\"success\"}", responseBody.toString());
	}

	@Test
	public void shouldUrlDecodeUrlParameters() throws Exception {
		sendGetEvent("/foo/Todd%7CFredrich%2Bwas%20here.json");
		assertEquals(1, observer.getReceivedCount());
		assertEquals(1, observer.getCompleteCount());
		assertEquals(1, observer.getSuccessCount());
		assertEquals(0, observer.getExceptionCount());

		assertEquals("{\"status\":\"success\",\"data\":\"Todd|Fredrich+was here\"}", responseBody.toString());
	}

	@Test
	public void shouldNotifyObserverOnError() throws Exception {
		sendGetEvent("/bar");
		assertEquals(1, observer.getReceivedCount());
		assertEquals(1, observer.getCompleteCount());
		assertEquals(1, observer.getExceptionCount());
		assertEquals(0, observer.getSuccessCount());

		assertEquals("{\"status\":\"error\",\"message\":\"foobar'd\",\"data\":\"BadRequestException\"}", responseBody.toString());
	}

	@Test
	public void shouldHandleNonDecodableValueInQueryString() throws Exception {
		sendGetEvent("/bar?value=%target");
		assertEquals(1, observer.getReceivedCount());
		assertEquals(1, observer.getCompleteCount());
		assertEquals(1, observer.getExceptionCount());
		assertEquals(0, observer.getSuccessCount());
		assertEquals("{\"status\":\"error\",\"message\":\"foobar'd\",\"data\":\"BadRequestException\"}", responseBody.toString());
	}

	@Test
	public void shouldHandleUrlDecodeErrorInFormat() throws Exception {
		sendGetEvent("/foo.%target");
		assertEquals(1, observer.getReceivedCount());
		assertEquals(1, observer.getCompleteCount());
		assertEquals(1, observer.getExceptionCount());
		assertEquals(0, observer.getSuccessCount());
		assertTrue(responseBody.toString().startsWith("{\"status\":\"error\",\"message\":\"Requested representation format not supported: %target"));
	}

	@Test
	public void shouldShouldThrowExceptionForErrorInFormat() throws Exception {
		sendGetEvent("/foo.unsupported");
		assertEquals(1, observer.getReceivedCount());
		assertEquals(1, observer.getCompleteCount());
		assertEquals(1, observer.getExceptionCount());
		assertEquals(0, observer.getSuccessCount());
		assertTrue(responseBody.toString().startsWith("{\"status\":\"error\",\"message\":\"Requested representation format not supported: unsupported."));
	}

	@Test
	public void shouldHandleInvalidDecodeInQueryString() throws Exception {
		sendGetEvent("/foo?value=%target");
		assertEquals(1, observer.getReceivedCount());
		assertEquals(1, observer.getCompleteCount());
		assertEquals(1, observer.getSuccessCount());
		assertEquals(0, observer.getExceptionCount());

		assertEquals("{\"status\":\"success\"}", responseBody.toString());
	}

	@Test
	public void shouldHandleUrlDecodeErrorInUrl() throws Exception {
		sendGetEvent("/%bar");
		assertEquals(1, observer.getReceivedCount());
		assertEquals(1, observer.getCompleteCount());
		assertEquals(1, observer.getExceptionCount());
		assertEquals(0, observer.getSuccessCount());

		assertEquals("{\"status\":\"error\",\"message\":\"Unresolvable URL: http://null/%bar\",\"data\":\"NotFoundException\"}", responseBody.toString());
	}

	@Test
	public void shouldParseTimepointJson() {
		sendGetEvent("/date.wjson", "{\"at\":\"2010-12-17T120000Z\"}");
		assertEquals(1, observer.getReceivedCount());
		assertEquals(1, observer.getCompleteCount());
		assertEquals(1, observer.getSuccessCount());
		assertEquals(0, observer.getExceptionCount());

		assertEquals("{\"status\":\"success\",\"data\":{\"at\":\"2010-12-17T12:00:00.000Z\"}}", responseBody.toString());
	}

	@Test
	public void shouldParseTimepointJsonUsingQueryString() {
		sendGetEvent("/date?format=wjson", "{\"at\":\"2010-12-17T120000Z\"}");
		assertEquals(1, observer.getReceivedCount());
		assertEquals(1, observer.getCompleteCount());
		assertEquals(1, observer.getSuccessCount());
		assertEquals(0, observer.getExceptionCount());

		assertEquals("{\"status\":\"success\",\"data\":{\"at\":\"2010-12-17T12:00:00.000Z\"}}", responseBody.toString());
	}

	@Test
	public void shouldParseTimepointXml() {
		sendGetEvent("/date.xml", "<org.restexpress.pipeline.Dated><at>2010-12-17T120000Z</at></org.restexpress.pipeline.Dated>");
		assertEquals(1, observer.getReceivedCount());
		assertEquals(1, observer.getCompleteCount());
		assertEquals(1, observer.getSuccessCount());
		assertEquals(0, observer.getExceptionCount());
		System.err.println(responseBody.toString());
		assertTrue(responseBody.toString().startsWith("<response>"));
		assertTrue(responseBody.toString().contains("<data>"));
		assertTrue(responseBody.toString().contains("<at>2010-12-17T12:00:00.000Z</at>"));
		assertTrue(responseBody.toString().contains("</data>"));
		assertTrue(responseBody.toString().endsWith("</response>"));
	}

	@Test
	public void shouldParseTimepointXmlUsingQueryString() {
		sendGetEvent("/date?format=xml", "<dated><at>2010-12-17T120000Z</at></dated>");
		assertEquals(1, observer.getReceivedCount());
		assertEquals(1, observer.getCompleteCount());
		assertEquals(1, observer.getSuccessCount());
		assertEquals(0, observer.getExceptionCount());

		assertTrue(responseBody.toString().startsWith("<response>"));

		assertTrue(responseBody.toString().contains("<data>"));
		assertTrue(responseBody.toString().contains("<at>2010-12-17T12:00:00.000Z</at>"));
		assertTrue(responseBody.toString().contains("</data>"));
		assertTrue(responseBody.toString().endsWith("</response>"));
	}

	@Test
	public void shouldThrowExceptionOnInvalidUrl() {
		sendGetEvent("/xyzt.xml");
		assertEquals(1, observer.getReceivedCount());
		assertEquals(1, observer.getCompleteCount());
		assertEquals(0, observer.getSuccessCount());
		assertEquals(1, observer.getExceptionCount());
		System.out.println(responseBody.toString());
		assertTrue(responseBody.toString().startsWith("<response>"));

		assertTrue(responseBody.toString().contains("<status>error</status>"));
		assertTrue(responseBody.toString().contains("<message>Unresolvable URL: http://null/xyzt.xml</message>"));
		assertTrue(responseBody.toString().endsWith("</response>"));
	}

	@Test
	public void shouldThrowExceptionOnInvalidUrlWithXmlFormatQueryString() {
		sendGetEvent("/xyzt?format=xml");
		assertEquals(1, observer.getReceivedCount());
		assertEquals(1, observer.getCompleteCount());
		assertEquals(0, observer.getSuccessCount());
		assertEquals(1, observer.getExceptionCount());

		assertTrue(responseBody.toString().startsWith("<response>"));

		assertTrue(responseBody.toString().contains("<status>error</status>"));
		assertTrue(responseBody.toString().contains("<message>Unresolvable URL: http://null/xyzt?format=xml</message>"));
		assertTrue(responseBody.toString().endsWith("</response>"));
	}

	@Test
	public void shouldCallAllFinallyProcessors() {
		NoopPostprocessor p1 = new NoopPostprocessor();
		NoopPostprocessor p2 = new NoopPostprocessor();
		NoopPostprocessor p3 = new NoopPostprocessor();
		messageHandler.addPostprocessor(p1);
		messageHandler.addPostprocessor(p2);
		messageHandler.addPostprocessor(p3);
		messageHandler.addFinallyProcessor(p1);
		messageHandler.addFinallyProcessor(p2);
		messageHandler.addFinallyProcessor(p3);
		sendGetEvent("/foo");
		assertEquals(2, p1.getCallCount());
		assertEquals(2, p2.getCallCount());
		assertEquals(2, p3.getCallCount());
	}

	@Test
	public void shouldCallAllFinallyProcessorsOnRouteException() {
		NoopPostprocessor p1 = new NoopPostprocessor();
		NoopPostprocessor p2 = new NoopPostprocessor();
		NoopPostprocessor p3 = new NoopPostprocessor();
		messageHandler.addPostprocessor(p1);
		messageHandler.addPostprocessor(p2);
		messageHandler.addPostprocessor(p3);
		messageHandler.addFinallyProcessor(p1);
		messageHandler.addFinallyProcessor(p2);
		messageHandler.addFinallyProcessor(p3);
		sendGetEvent("/xyzt.html");
		assertEquals(1, p1.getCallCount());
		assertEquals(1, p2.getCallCount());
		assertEquals(1, p3.getCallCount());
	}

	@Test
	public void shouldCallAllFinallyProcessorsOnProcessorException() {
		NoopPostprocessor p1 = new ExceptionPostprocessor();
		NoopPostprocessor p2 = new ExceptionPostprocessor();
		NoopPostprocessor p3 = new ExceptionPostprocessor();
		messageHandler.addPostprocessor(p1); // this one throws the exception in
												// Postprocessors
		messageHandler.addPostprocessor(p2); // not called
		messageHandler.addPostprocessor(p3); // not called
		messageHandler.addFinallyProcessor(p1);
		messageHandler.addFinallyProcessor(p2);
		messageHandler.addFinallyProcessor(p3);
		sendGetEvent("/foo");
		assertEquals(2, p1.getCallCount());
		assertEquals(1, p2.getCallCount());
		assertEquals(1, p3.getCallCount());
	}

	@Test
	public void shouldSetJSONContentType() throws Exception {
		sendGetEvent("/serializedString.json?returnValue=raw string");
		assertEquals(0, observer.getExceptionCount());
		assertEquals(1, observer.getReceivedCount());
		assertEquals(1, observer.getCompleteCount());
		assertEquals(1, observer.getSuccessCount());
		assertEquals("{\"status\":\"success\",\"data\":\"raw string\"}", responseBody.toString());
		assertTrue(responseHeaders.containsKey("Content-Type"));
		List<String> contentTypes = responseHeaders.get(HttpHeaders.Names.CONTENT_TYPE);
		assertEquals(1, contentTypes.size());
		assertEquals("application/json; charset=UTF-8", contentTypes.get(0));
	}

	@Test
	public void shouldSetJSONContentTypeOnNullReturn() throws Exception {
		sendGetEvent("/serializedString.json");
		assertEquals(0, observer.getExceptionCount());
		assertEquals(1, observer.getReceivedCount());
		assertEquals(1, observer.getCompleteCount());
		assertEquals(1, observer.getSuccessCount());
		assertTrue(responseHeaders.containsKey("Content-Type"));
		List<String> contentTypes = responseHeaders.get(HttpHeaders.Names.CONTENT_TYPE);
		assertEquals(1, contentTypes.size());
		assertEquals("application/json; charset=UTF-8", contentTypes.get(0));
		assertEquals("{\"status\":\"success\"}", responseBody.toString());
	}

	private void sendGetEvent(String path) {
		try {
			pl.sendUpstream(new UpstreamMessageEvent(channel, new DefaultHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.GET, path), new InetSocketAddress(1)));
		} catch (Throwable t) {
			System.out.println(t.getMessage());
		}
	}

	private void sendGetEvent(String path, String body) {
		try {
			HttpRequest request = new DefaultHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.GET, path);
			request.setContent(ChannelBuffers.copiedBuffer(body, Charset.defaultCharset()));

			pl.sendUpstream(new UpstreamMessageEvent(channel, request, new InetSocketAddress(1)));
		} catch (Throwable t) {
			System.out.println(t.getMessage());
		}
	}

	public class DummyRoutes extends RouteDeclaration {
		private Object controller = new FooBarController();

		public DummyRoutes defineRoutes() {
			uri("/foo.{format}", controller).action("fooAction", HttpMethod.GET);

			uri("/foo/{userPhrase}.{format}", controller).action("verifyUrlDecodedParameters", HttpMethod.GET);

			uri("/bar.{format}", controller).action("barAction", HttpMethod.GET);

			uri("/date.{format}", controller).action("dateAction", HttpMethod.GET);

			uri("/unserializedDefault", controller).action("unserializedDefault", HttpMethod.GET);

			uri("/unserialized", controller).action("unserializedAction", HttpMethod.GET);

			uri("/unserializedToo", controller).action("contentHeaderAction", HttpMethod.GET);

			uri("/serializedString.{format}", controller).action("serializedStringAction", HttpMethod.GET);

			uri("/setBodyAction.html", controller).action("setBodyAction", HttpMethod.GET);
			return this;
		}
	}

	public class FooBarController {
		public void fooAction(Request request, Response response) {
			// do nothing.
		}

		public String verifyUrlDecodedParameters(Request request, Response response) {
			return request.getHeader("userPhrase");
		}

		public void barAction(Request request, Response response) {
			throw new BadRequestException("foobar'd");
		}

		public Object dateAction(Request request, Response response) {
			return request.getBodyAs(Dated.class);
		}

		public String unserializedDefault(Request request, Response response) {
			response.noSerialization();
			return "should be text plain, here";
		}

		public String unserializedAction(Request request, Response response) {
			response.setContentType("text/html");
			response.noSerialization();
			return "<html><body>Some kinda wonderful!</body></html>";
		}

		public String serializedStringAction(Request request, Response response) {
			return request.getHeader("returnValue");
		}

		public String contentHeaderAction(Request request, Response response) {
			response.addHeader("Content-Type", "text/html");
			response.noSerialization();
			return "<html><body>Wow! What a fabulous HTML body...</body></html>";
		}

		public void setBodyAction(Request request, Response response) {
			response.setContentType(MediaType.TEXT_HTML.withCharset(CharacterSet.UTF_8.getCharsetName()));
			response.noSerialization();
			response.setBody("<html><body>Arbitrarily set HTML body...</body></html>");
		}
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
}
