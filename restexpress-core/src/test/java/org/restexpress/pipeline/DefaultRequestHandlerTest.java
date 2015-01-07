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

import java.util.List;

import org.jboss.netty.handler.codec.http.HttpHeaders;
import org.jboss.netty.handler.codec.http.HttpMethod;
import org.junit.Before;
import org.junit.Test;
import org.restexpress.Request;
import org.restexpress.Response;
import org.restexpress.domain.CharacterSet;
import org.restexpress.domain.Format;
import org.restexpress.domain.MediaType;
import org.restexpress.exception.BadRequestException;
import org.restexpress.pipeline.handler.RestExpressRequestHandler;
import org.restexpress.pipeline.handler.RestExpressRequestHandlerBuilder;
import org.restexpress.response.Wrapper;
import org.restexpress.route.RouteDeclaration;
import org.restexpress.serialization.JacksonJsonProcessor;
import org.restexpress.serialization.JacksonXmlProcessor;

/**
 * @author toddf
 * @since Dec 15, 2010
 */
public class DefaultRequestHandlerTest extends AbstractWrapperResponse {

    

    @Before
    public void initialize() throws Exception {
        defineRoutes(routes);
        initialize(routes);
    }

    @Override
    protected RestExpressRequestHandler build(RestExpressRequestHandlerBuilder builder) throws Exception {
        builder.add(new JacksonJsonProcessor(), Wrapper.newJsendResponseWrapper(), true);
        builder.add(new JacksonXmlProcessor(Format.XML.getMediaType()), Wrapper.newJsendResponseWrapper());
        return super.build(builder);
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
        assertTrue(responseBody.toString().startsWith(
                "{\"status\":\"error\",\"message\":\"Requested representation format not supported: %target"));
    }

    @Test
    public void shouldShouldThrowExceptionForErrorInFormat() throws Exception {
        sendGetEvent("/foo.unsupported");
        assertEquals(1, observer.getReceivedCount());
        assertEquals(1, observer.getCompleteCount());
        assertEquals(1, observer.getExceptionCount());
        assertEquals(0, observer.getSuccessCount());
        assertTrue(responseBody.toString().startsWith(
                "{\"status\":\"error\",\"message\":\"Requested representation format not supported: unsupported."));
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

        assertEquals("{\"status\":\"error\",\"message\":\"Unresolvable URL: http://null/%bar\",\"data\":\"NotFoundException\"}",
                responseBody.toString());
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

    public RouteDeclaration defineRoutes(RouteDeclaration routeDeclaration) {

        Object controller = new FooBarController();

        routeDeclaration.uri("/foo.{format}", controller).action("fooAction", HttpMethod.GET);

        routeDeclaration.uri("/foo/{userPhrase}.{format}", controller).action("verifyUrlDecodedParameters", HttpMethod.GET);

        routeDeclaration.uri("/bar.{format}", controller).action("barAction", HttpMethod.GET);

        routeDeclaration.uri("/date.{format}", controller).action("dateAction", HttpMethod.GET);

        routeDeclaration.uri("/unserializedDefault", controller).action("unserializedDefault", HttpMethod.GET);

        routeDeclaration.uri("/unserialized", controller).action("unserializedAction", HttpMethod.GET);

        routeDeclaration.uri("/unserializedToo", controller).action("contentHeaderAction", HttpMethod.GET);

        routeDeclaration.uri("/serializedString.{format}", controller).action("serializedStringAction", HttpMethod.GET);

        routeDeclaration.uri("/setBodyAction.html", controller).action("setBodyAction", HttpMethod.GET);
        return routeDeclaration;

    }

    public static class FooBarController {
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
            response.setEntity("<html><body>Arbitrarily set HTML body...</body></html>");
        }
    }

}
