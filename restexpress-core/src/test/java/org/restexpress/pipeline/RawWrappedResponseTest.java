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
import org.restexpress.response.Wrapper;
import org.restexpress.serialization.JacksonJsonProcessor;
import org.restexpress.serialization.JacksonXmlProcessor;

/**
 * @author toddf
 * @since Dec 15, 2010
 */
public class RawWrappedResponseTest extends AbstractWrapperResponse {

    private JacksonJsonProcessor jacksonJsonProcessor;
    private JacksonXmlProcessor jacksonXmlProcessor;

    @Before
    public void initialize() throws Exception {
        DummyRoutes routes = new DummyRoutes();
        routes.defineRoutes();
        initialize(routes);
        jacksonJsonProcessor = new JacksonJsonProcessor();
        jacksonXmlProcessor = new JacksonXmlProcessor();
        messageHandler.serializationProvider().add(jacksonJsonProcessor, Wrapper.newRawResponseWrapper(), true);
        messageHandler.serializationProvider().add(jacksonXmlProcessor, Wrapper.newRawResponseWrapper());
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
        assertEquals("<String>Normal GET action</String>", responseBody.toString());
    }

    @Test
    public void shouldWrapGetInRawXmlUsingQueryString() {
        sendEvent(HttpMethod.GET, "/normal_get?format=xml", null);
        assertEquals(1, observer.getReceivedCount());
        assertEquals(1, observer.getCompleteCount());
        assertEquals(1, observer.getSuccessCount());
        assertEquals(0, observer.getExceptionCount());

        assertEquals("<String>Normal GET action</String>", responseBody.toString());
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

        assertEquals("<String>Normal PUT action</String>", responseBody.toString());
    }

    @Test
    public void shouldWrapPutInRawXmlUsingQueryString() {
        sendEvent(HttpMethod.PUT, "/normal_put?format=xml", null);
        assertEquals(1, observer.getReceivedCount());
        assertEquals(1, observer.getCompleteCount());
        assertEquals(1, observer.getSuccessCount());
        assertEquals(0, observer.getExceptionCount());

        assertEquals("<String>Normal PUT action</String>", responseBody.toString());
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

        assertEquals("<String>Normal POST action</String>", responseBody.toString());
    }

    @Test
    public void shouldWrapPostInRawXmlUsingQueryString() {
        sendEvent(HttpMethod.POST, "/normal_post?format=xml", null);
        assertEquals(1, observer.getReceivedCount());
        assertEquals(1, observer.getCompleteCount());
        assertEquals(1, observer.getSuccessCount());
        assertEquals(0, observer.getExceptionCount());

        assertEquals("<String>Normal POST action</String>", responseBody.toString());
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

        assertEquals("<String>Normal DELETE action</String>", responseBody.toString());
    }

    @Test
    public void shouldWrapDeleteInRawXmlUsingQueryString() {
        sendEvent(HttpMethod.DELETE, "/normal_delete?format=xml", null);
        assertEquals(1, observer.getReceivedCount());
        assertEquals(1, observer.getCompleteCount());
        assertEquals(1, observer.getSuccessCount());
        assertEquals(0, observer.getExceptionCount());

        assertEquals("<String>Normal DELETE action</String>", responseBody.toString());
    }

    @Test
    public void shouldWrapNotFoundInRawJson() {
        sendEvent(HttpMethod.GET, "/not_found.json", null);
        assertEquals(1, observer.getReceivedCount());
        assertEquals(1, observer.getCompleteCount());
        assertEquals(0, observer.getSuccessCount());
        assertEquals(1, observer.getExceptionCount());

        assertEquals("Item not found", responseBody.toString());
    }

    @Test
    public void shouldWrapNotFoundInRawJsonUsingQueryString() {
        sendEvent(HttpMethod.GET, "/not_found?format=json", null);
        assertEquals(1, observer.getReceivedCount());
        assertEquals(1, observer.getCompleteCount());
        assertEquals(0, observer.getSuccessCount());
        assertEquals(1, observer.getExceptionCount());

        assertEquals("Item not found", responseBody.toString());
    }

    @Test
    public void shouldWrapNotFoundInRawXml() {
        sendEvent(HttpMethod.GET, "/not_found.xml", null);
        assertEquals(1, observer.getReceivedCount());
        assertEquals(1, observer.getCompleteCount());
        assertEquals(0, observer.getSuccessCount());
        assertEquals(1, observer.getExceptionCount());

        assertEquals("Item not found", responseBody.toString());
    }

    @Test
    public void shouldWrapNotFoundInRawXmlUsingQueryString() {
        sendEvent(HttpMethod.GET, "/not_found?format=xml", null);
        assertEquals(1, observer.getReceivedCount());
        assertEquals(1, observer.getCompleteCount());
        assertEquals(0, observer.getSuccessCount());
        assertEquals(1, observer.getExceptionCount());

        assertEquals("Item not found", responseBody.toString());
    }

    @Test
    public void shouldWrapNullPointerInRawJson() {
        sendEvent(HttpMethod.GET, "/null_pointer.json", null);
        assertEquals(1, observer.getReceivedCount());
        assertEquals(1, observer.getCompleteCount());
        assertEquals(0, observer.getSuccessCount());
        assertEquals(1, observer.getExceptionCount());

        assertEquals("Null and void", responseBody.toString());
    }

    @Test
    public void shouldWrapNullPointerInRawJsonUsingQueryString() {
        sendEvent(HttpMethod.GET, "/null_pointer?format=json", null);
        assertEquals(1, observer.getReceivedCount());
        assertEquals(1, observer.getCompleteCount());
        assertEquals(0, observer.getSuccessCount());
        assertEquals(1, observer.getExceptionCount());

        assertEquals("Null and void", responseBody.toString());
    }

    @Test
    public void shouldWrapNullPointerInRawXml() {
        sendEvent(HttpMethod.GET, "/null_pointer.xml", null);
        assertEquals(1, observer.getReceivedCount());
        assertEquals(1, observer.getCompleteCount());
        assertEquals(0, observer.getSuccessCount());
        assertEquals(1, observer.getExceptionCount());

        assertEquals("Null and void", responseBody.toString());
    }

    @Test
    public void shouldWrapNullPointerInRawXmlUsingQueryString() {
        sendEvent(HttpMethod.GET, "/null_pointer?format=xml", null);
        assertEquals(1, observer.getReceivedCount());
        assertEquals(1, observer.getCompleteCount());
        assertEquals(0, observer.getSuccessCount());
        assertEquals(1, observer.getExceptionCount());

        assertEquals("Null and void", responseBody.toString());
    }

    @Test
    public void shouldWrapInvalidUrlWithRawJson() {
        sendEvent(HttpMethod.GET, "/xyzt.json", null);
        assertEquals(1, observer.getReceivedCount());
        assertEquals(1, observer.getCompleteCount());
        assertEquals(0, observer.getSuccessCount());
        assertEquals(1, observer.getExceptionCount());

        assertEquals("Unresolvable URL: http://null/xyzt.json", responseBody.toString());
    }

    @Test
    public void shouldWrapInvalidUrlWithRawJsonUsingQueryString() {
        sendEvent(HttpMethod.GET, "/xyzt?format=json", null);
        assertEquals(1, observer.getReceivedCount());
        assertEquals(1, observer.getCompleteCount());
        assertEquals(0, observer.getSuccessCount());
        assertEquals(1, observer.getExceptionCount());

        assertEquals("Unresolvable URL: http://null/xyzt?format=json", responseBody.toString());
    }

    @Test
    public void shouldWrapInvalidUrlWithRawXml() {
        sendEvent(HttpMethod.GET, "/xyzt.xml", null);
        assertEquals(1, observer.getReceivedCount());
        assertEquals(1, observer.getCompleteCount());
        assertEquals(0, observer.getSuccessCount());
        assertEquals(1, observer.getExceptionCount());
        assertEquals("Unresolvable URL: http://null/xyzt.xml", responseBody.toString());
    }

    @Test
    public void shouldWrapInvalidUrlWithXmlUsingQueryString() {
        sendEvent(HttpMethod.GET, "/xyzt?format=xml", null);
        assertEquals(1, observer.getReceivedCount());
        assertEquals(1, observer.getCompleteCount());
        assertEquals(0, observer.getSuccessCount());
        assertEquals(1, observer.getExceptionCount());
        System.err.println(responseBody.toString());
        assertEquals("Unresolvable URL: http://null/xyzt?format=xml", responseBody.toString());
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

}
