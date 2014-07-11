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
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFactory;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.UpstreamMessageEvent;
import org.jboss.netty.channel.local.DefaultLocalServerChannelFactory;
import org.jboss.netty.handler.codec.http.DefaultHttpRequest;
import org.jboss.netty.handler.codec.http.HttpMethod;
import org.jboss.netty.handler.codec.http.HttpRequest;
import org.jboss.netty.handler.codec.http.HttpVersion;
import org.junit.Before;
import org.junit.Test;
import org.restexpress.pipeline.DefaultRequestHandler;
import org.restexpress.pipeline.PipelineBuilder;
import org.restexpress.response.DefaultHttpResponseWriter;
import org.restexpress.response.StringBufferHttpResponseWriter;
import org.restexpress.route.RouteDeclaration;
import org.restexpress.route.RouteResolver;
import org.restexpress.serialization.DefaultSerializationProvider;
import org.restexpress.serialization.SerializationProvider;
import org.restexpress.settings.RouteDefaults;


/**
 * @author toddf
 * @since Dec 15, 2010
 */
public class RawWrappedResponseTest
{
	private DefaultRequestHandler messageHandler;
	private WrappedResponseObserver observer;
	private Channel channel;
    private ChannelPipeline pl;
    private StringBuffer httpResponse;
	
	@Before
	public void initialize()
	throws Exception
	{
		SerializationProvider resolver = new DefaultSerializationProvider();
//		resolver.put(Format.JSON, new ResponseProcessor(new JacksonJsonProcessor(), new RawResponseWrapper()));
//		resolver.put(Format.XML, new ResponseProcessor(new XstreamXmlProcessor(), new RawResponseWrapper()));
//		resolver.setDefaultFormat(Format.JSON);
		
		DummyRoutes routes = new DummyRoutes();
		routes.defineRoutes();
		messageHandler = new DefaultRequestHandler(new RouteResolver(routes.createRouteMapping(new RouteDefaults())), resolver, new DefaultHttpResponseWriter(), true);
		observer = new WrappedResponseObserver();
		messageHandler.addMessageObserver(observer);
		httpResponse = new StringBuffer();
		messageHandler.setResponseWriter(new StringBufferHttpResponseWriter(httpResponse));
		PipelineBuilder pf = new PipelineBuilder()
			.addRequestHandler(messageHandler);
	    pl = pf.getPipeline();
	    ChannelFactory channelFactory = new DefaultLocalServerChannelFactory();
	    channel = channelFactory.newChannel(pl);
	}

	@Test
	public void shouldWrapGetInRawJson()
	{
		sendEvent(HttpMethod.GET, "/normal_get.json", null);
		assertEquals(1, observer.getReceivedCount());
		assertEquals(1, observer.getCompleteCount());
		assertEquals(1, observer.getSuccessCount());
		assertEquals(0, observer.getExceptionCount());
//		System.out.println(httpResponse.toString());
		assertEquals("\"Normal GET action\"", httpResponse.toString());
	}

	@Test
	public void shouldWrapGetInRawJsonUsingQueryString()
	{
		sendEvent(HttpMethod.GET, "/normal_get?format=json", null);
		assertEquals(1, observer.getReceivedCount());
		assertEquals(1, observer.getCompleteCount());
		assertEquals(1, observer.getSuccessCount());
		assertEquals(0, observer.getExceptionCount());
//		System.out.println(httpResponse.toString());
		assertEquals("\"Normal GET action\"", httpResponse.toString());
	}

	@Test
	public void shouldWrapGetInRawXml()
	{
		sendEvent(HttpMethod.GET, "/normal_get.xml", null);
		assertEquals(1, observer.getReceivedCount());
		assertEquals(1, observer.getCompleteCount());
		assertEquals(1, observer.getSuccessCount());
		assertEquals(0, observer.getExceptionCount());
//		System.out.println(httpResponse.toString());
		assertEquals("<string>Normal GET action</string>", httpResponse.toString());
	}

	@Test
	public void shouldWrapGetInRawXmlUsingQueryString()
	{
		sendEvent(HttpMethod.GET, "/normal_get?format=xml", null);
		assertEquals(1, observer.getReceivedCount());
		assertEquals(1, observer.getCompleteCount());
		assertEquals(1, observer.getSuccessCount());
		assertEquals(0, observer.getExceptionCount());
//		System.out.println(httpResponse.toString());
		assertEquals("<string>Normal GET action</string>", httpResponse.toString());
	}

	@Test
	public void shouldWrapPutInRawJson()
	{
		sendEvent(HttpMethod.PUT, "/normal_put.json", null);
		assertEquals(1, observer.getReceivedCount());
		assertEquals(1, observer.getCompleteCount());
		assertEquals(1, observer.getSuccessCount());
		assertEquals(0, observer.getExceptionCount());
//		System.out.println(httpResponse.toString());
		assertEquals("\"Normal PUT action\"", httpResponse.toString());
	}

	@Test
	public void shouldWrapPutInRawJsonUsingQueryString()
	{
		sendEvent(HttpMethod.PUT, "/normal_put?format=json", null);
		assertEquals(1, observer.getReceivedCount());
		assertEquals(1, observer.getCompleteCount());
		assertEquals(1, observer.getSuccessCount());
		assertEquals(0, observer.getExceptionCount());
//		System.out.println(httpResponse.toString());
		assertEquals("\"Normal PUT action\"", httpResponse.toString());
	}

	@Test
	public void shouldWrapPutInRawXml()
	{
		sendEvent(HttpMethod.PUT, "/normal_put.xml", null);
		assertEquals(1, observer.getReceivedCount());
		assertEquals(1, observer.getCompleteCount());
		assertEquals(1, observer.getSuccessCount());
		assertEquals(0, observer.getExceptionCount());
//		System.out.println(httpResponse.toString());
		assertEquals("<string>Normal PUT action</string>", httpResponse.toString());
	}

	@Test
	public void shouldWrapPutInRawXmlUsingQueryString()
	{
		sendEvent(HttpMethod.PUT, "/normal_put?format=xml", null);
		assertEquals(1, observer.getReceivedCount());
		assertEquals(1, observer.getCompleteCount());
		assertEquals(1, observer.getSuccessCount());
		assertEquals(0, observer.getExceptionCount());
//		System.out.println(httpResponse.toString());
		assertEquals("<string>Normal PUT action</string>", httpResponse.toString());
	}

	@Test
	public void shouldWrapPostInRawJson()
	{
		sendEvent(HttpMethod.POST, "/normal_post.json", null);
		assertEquals(1, observer.getReceivedCount());
		assertEquals(1, observer.getCompleteCount());
		assertEquals(1, observer.getSuccessCount());
		assertEquals(0, observer.getExceptionCount());
//		System.out.println(httpResponse.toString());
		assertEquals("\"Normal POST action\"", httpResponse.toString());
	}

	@Test
	public void shouldWrapPostInRawJsonUsingQueryString()
	{
		sendEvent(HttpMethod.POST, "/normal_post?format=json", null);
		assertEquals(1, observer.getReceivedCount());
		assertEquals(1, observer.getCompleteCount());
		assertEquals(1, observer.getSuccessCount());
		assertEquals(0, observer.getExceptionCount());
//		System.out.println(httpResponse.toString());
		assertEquals("\"Normal POST action\"", httpResponse.toString());
	}

	@Test
	public void shouldWrapPostInRawXml()
	{
		sendEvent(HttpMethod.POST, "/normal_post.xml", null);
		assertEquals(1, observer.getReceivedCount());
		assertEquals(1, observer.getCompleteCount());
		assertEquals(1, observer.getSuccessCount());
		assertEquals(0, observer.getExceptionCount());
//		System.out.println(httpResponse.toString());
		assertEquals("<string>Normal POST action</string>", httpResponse.toString());
	}

	@Test
	public void shouldWrapPostInRawXmlUsingQueryString()
	{
		sendEvent(HttpMethod.POST, "/normal_post?format=xml", null);
		assertEquals(1, observer.getReceivedCount());
		assertEquals(1, observer.getCompleteCount());
		assertEquals(1, observer.getSuccessCount());
		assertEquals(0, observer.getExceptionCount());
//		System.out.println(httpResponse.toString());
		assertEquals("<string>Normal POST action</string>", httpResponse.toString());
	}

	@Test
	public void shouldWrapDeleteInRawJson()
	{
		sendEvent(HttpMethod.DELETE, "/normal_delete.json", null);
		assertEquals(1, observer.getReceivedCount());
		assertEquals(1, observer.getCompleteCount());
		assertEquals(1, observer.getSuccessCount());
		assertEquals(0, observer.getExceptionCount());
//		System.out.println(httpResponse.toString());
		assertEquals("\"Normal DELETE action\"", httpResponse.toString());
	}

	@Test
	public void shouldWrapDeleteInRawJsonUsingQueryString()
	{
		sendEvent(HttpMethod.DELETE, "/normal_delete?format=json", null);
		assertEquals(1, observer.getReceivedCount());
		assertEquals(1, observer.getCompleteCount());
		assertEquals(1, observer.getSuccessCount());
		assertEquals(0, observer.getExceptionCount());
//		System.out.println(httpResponse.toString());
		assertEquals("\"Normal DELETE action\"", httpResponse.toString());
	}

	@Test
	public void shouldWrapDeleteInRawXml()
	{
		sendEvent(HttpMethod.DELETE, "/normal_delete.xml", null);
		assertEquals(1, observer.getReceivedCount());
		assertEquals(1, observer.getCompleteCount());
		assertEquals(1, observer.getSuccessCount());
		assertEquals(0, observer.getExceptionCount());
//		System.out.println(httpResponse.toString());
		assertEquals("<string>Normal DELETE action</string>", httpResponse.toString());
	}

	@Test
	public void shouldWrapDeleteInRawXmlUsingQueryString()
	{
		sendEvent(HttpMethod.DELETE, "/normal_delete?format=xml", null);
		assertEquals(1, observer.getReceivedCount());
		assertEquals(1, observer.getCompleteCount());
		assertEquals(1, observer.getSuccessCount());
		assertEquals(0, observer.getExceptionCount());
//		System.out.println(httpResponse.toString());
		assertEquals("<string>Normal DELETE action</string>", httpResponse.toString());
	}

	@Test
	public void shouldWrapNotFoundInRawJson()
	{
		sendEvent(HttpMethod.GET, "/not_found.json", null);
		assertEquals(1, observer.getReceivedCount());
		assertEquals(1, observer.getCompleteCount());
		assertEquals(0, observer.getSuccessCount());
		assertEquals(1, observer.getExceptionCount());
//		System.out.println(httpResponse.toString());
		assertEquals("\"Item not found\"", httpResponse.toString());
	}

	@Test
	public void shouldWrapNotFoundInRawJsonUsingQueryString()
	{
		sendEvent(HttpMethod.GET, "/not_found?format=json", null);
		assertEquals(1, observer.getReceivedCount());
		assertEquals(1, observer.getCompleteCount());
		assertEquals(0, observer.getSuccessCount());
		assertEquals(1, observer.getExceptionCount());
//		System.out.println(httpResponse.toString());
		assertEquals("\"Item not found\"", httpResponse.toString());
	}

	@Test
	public void shouldWrapNotFoundInRawXml()
	{
		sendEvent(HttpMethod.GET, "/not_found.xml", null);
		assertEquals(1, observer.getReceivedCount());
		assertEquals(1, observer.getCompleteCount());
		assertEquals(0, observer.getSuccessCount());
		assertEquals(1, observer.getExceptionCount());
//		System.out.println(httpResponse.toString());
		assertEquals("<string>Item not found</string>", httpResponse.toString());
	}

	@Test
	public void shouldWrapNotFoundInRawXmlUsingQueryString()
	{
		sendEvent(HttpMethod.GET, "/not_found?format=xml", null);
		assertEquals(1, observer.getReceivedCount());
		assertEquals(1, observer.getCompleteCount());
		assertEquals(0, observer.getSuccessCount());
		assertEquals(1, observer.getExceptionCount());
//		System.out.println(httpResponse.toString());
		assertEquals("<string>Item not found</string>", httpResponse.toString());
	}

	@Test
	public void shouldWrapNullPointerInRawJson()
	{
		sendEvent(HttpMethod.GET, "/null_pointer.json", null);
		assertEquals(1, observer.getReceivedCount());
		assertEquals(1, observer.getCompleteCount());
		assertEquals(0, observer.getSuccessCount());
		assertEquals(1, observer.getExceptionCount());
//		System.out.println(httpResponse.toString());
		assertEquals("\"Null and void\"", httpResponse.toString());
	}

	@Test
	public void shouldWrapNullPointerInRawJsonUsingQueryString()
	{
		sendEvent(HttpMethod.GET, "/null_pointer?format=json", null);
		assertEquals(1, observer.getReceivedCount());
		assertEquals(1, observer.getCompleteCount());
		assertEquals(0, observer.getSuccessCount());
		assertEquals(1, observer.getExceptionCount());
//		System.out.println(httpResponse.toString());
		assertEquals("\"Null and void\"", httpResponse.toString());
	}

	@Test
	public void shouldWrapNullPointerInRawXml()
	{
		sendEvent(HttpMethod.GET, "/null_pointer.xml", null);
		assertEquals(1, observer.getReceivedCount());
		assertEquals(1, observer.getCompleteCount());
		assertEquals(0, observer.getSuccessCount());
		assertEquals(1, observer.getExceptionCount());
//		System.out.println(httpResponse.toString());
		assertEquals("<string>Null and void</string>", httpResponse.toString());
	}

	@Test
	public void shouldWrapNullPointerInRawXmlUsingQueryString()
	{
		sendEvent(HttpMethod.GET, "/null_pointer?format=xml", null);
		assertEquals(1, observer.getReceivedCount());
		assertEquals(1, observer.getCompleteCount());
		assertEquals(0, observer.getSuccessCount());
		assertEquals(1, observer.getExceptionCount());
//		System.out.println(httpResponse.toString());
		assertEquals("<string>Null and void</string>", httpResponse.toString());
	}

	@Test
	public void shouldWrapInvalidUrlWithRawJson()
	{
		sendEvent(HttpMethod.GET, "/xyzt.json", null);
		assertEquals(1, observer.getReceivedCount());
		assertEquals(1, observer.getCompleteCount());
		assertEquals(0, observer.getSuccessCount());
		assertEquals(1, observer.getExceptionCount());
//		System.out.println(httpResponse.toString());
		assertEquals("\"Unresolvable URL: http://null/xyzt.json\"", httpResponse.toString());
	}

	@Test
	public void shouldWrapInvalidUrlWithRawJsonUsingQueryString()
	{
		sendEvent(HttpMethod.GET, "/xyzt?format=json", null);
		assertEquals(1, observer.getReceivedCount());
		assertEquals(1, observer.getCompleteCount());
		assertEquals(0, observer.getSuccessCount());
		assertEquals(1, observer.getExceptionCount());
//		System.out.println(httpResponse.toString());
		assertEquals("\"Unresolvable URL: http://null/xyzt?format=json\"", httpResponse.toString());
	}

	@Test
	public void shouldWrapInvalidUrlWithRawXml()
	{
		sendEvent(HttpMethod.GET, "/xyzt.xml", null);
		assertEquals(1, observer.getReceivedCount());
		assertEquals(1, observer.getCompleteCount());
		assertEquals(0, observer.getSuccessCount());
		assertEquals(1, observer.getExceptionCount());
//		System.out.println(httpResponse.toString());
		assertEquals("<string>Unresolvable URL: http://null/xyzt.xml</string>", httpResponse.toString());
	}

	@Test
	public void shouldWrapInvalidUrlWithXmlUsingQueryString()
	{
		sendEvent(HttpMethod.GET, "/xyzt?format=xml", null);
		assertEquals(1, observer.getReceivedCount());
		assertEquals(1, observer.getCompleteCount());
		assertEquals(0, observer.getSuccessCount());
		assertEquals(1, observer.getExceptionCount());
//		System.out.println(httpResponse.toString());
		assertEquals("<string>Unresolvable URL: http://null/xyzt?format=xml</string>", httpResponse.toString());
	}

	@Test
	public void shouldDeleteWithoutContent()
	{
		sendEvent(HttpMethod.DELETE, "/no_content_delete.json", null);
		assertEquals(1, observer.getReceivedCount());
		assertEquals(1, observer.getCompleteCount());
		assertEquals(1, observer.getSuccessCount());
		assertEquals(0, observer.getExceptionCount());
//		System.out.println(httpResponse.toString());
		assertEquals("null", httpResponse.toString());
	}

	@Test
	public void shouldThrowExceptionOnDeleteNoContentContainingBody()
	{
		sendEvent(HttpMethod.DELETE, "/no_content_with_body_delete.json", null);
		assertEquals(1, observer.getReceivedCount());
		assertEquals(1, observer.getCompleteCount());
		assertEquals(0, observer.getSuccessCount());
		assertEquals(1, observer.getExceptionCount());
//		System.out.println(httpResponse.toString());
	}

	@Test
	public void shouldDeleteIgnoringJsonp()
	{
		sendEvent(HttpMethod.DELETE, "/normal_delete.json?jsonp=jsonp_callback", null);
		assertEquals(1, observer.getReceivedCount());
		assertEquals(1, observer.getCompleteCount());
		assertEquals(1, observer.getSuccessCount());
		assertEquals(0, observer.getExceptionCount());
//		System.out.println(httpResponse.toString());
		assertEquals("\"Normal DELETE action\"", httpResponse.toString());
	}

	private void sendEvent(HttpMethod method, String path, String body)
    {
		HttpRequest request = new DefaultHttpRequest(HttpVersion.HTTP_1_1, method, path);
		
		if (body != null)
		{
			request.setContent(ChannelBuffers.copiedBuffer(body, Charset.defaultCharset()));
		}

	    pl.sendUpstream(new UpstreamMessageEvent(
	    	channel,
	    	request,
	    	new InetSocketAddress(1)));
    }
	
	public class DummyRoutes
	extends RouteDeclaration
	{
		private Object controller = new WrappedResponseController();
		private RouteDefaults defaults = new RouteDefaults();

        public void defineRoutes()
        {
        	uri("/normal_get.{format}", controller, defaults)
        		.action("normalGetAction", HttpMethod.GET);

        	uri("/normal_put.{format}", controller, defaults)
    		.action("normalPutAction", HttpMethod.PUT);

        	uri("/normal_post.{format}", controller, defaults)
    		.action("normalPostAction", HttpMethod.POST);

        	uri("/normal_delete.{format}", controller, defaults)
    		.action("normalDeleteAction", HttpMethod.DELETE);

        	uri("/no_content_delete.{format}", controller, defaults)
    		.action("noContentDeleteAction", HttpMethod.DELETE);

        	uri("/no_content_with_body_delete.{format}", controller, defaults)
    		.action("noContentWithBodyDeleteAction", HttpMethod.DELETE);

        	uri("/not_found.{format}", controller, defaults)
        		.action("notFoundAction", HttpMethod.GET);

        	uri("/null_pointer.{format}", controller, defaults)
        		.action("nullPointerAction", HttpMethod.GET);
        }
	}
}
