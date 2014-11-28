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
import org.restexpress.SerializationProvider;
import org.restexpress.domain.response.JsendResult;
import org.restexpress.response.Wrapper;
import org.restexpress.response.Wrapper.JsendResponseWrapper;
import org.restexpress.serialization.JacksonJsonProcessor;
import org.restexpress.serialization.JacksonXmlProcessor;
import org.restexpress.serialization.Processor;

/**
 * {@link JsendResponseWrapper} test case.
 * 
 * @author <a href="mailto:jguibert@intelligents-ia.com" >Jerome Guibert</a>
 * @author toddf
 * @since Dec 15, 2010
 */
public class JsendWrappedResponseTest extends AbstractWrapperResponse {

    private Processor processor;

    @Before
    public void initialize() throws Exception {
        DummyRoutes routes = new DummyRoutes();
        routes.defineRoutes();
        initialize(routes);
        SerializationProvider provider = messageHandler.serializationProvider();
        processor = new JacksonJsonProcessor();
        provider.add(processor, Wrapper.newJsendResponseWrapper(), true);
        
    }

    @Test
    public void shouldSendFormatNotSupported() {
        sendEvent(HttpMethod.GET, "/normal_get.xml", null);
        assertEquals(1, observer.getExceptionCount());
    }
    
    @Test
    public void shouldSendInXml() {
        Processor xmlProcessor = new JacksonXmlProcessor();
        messageHandler.serializationProvider().add(xmlProcessor, Wrapper.newJsendResponseWrapper());
        sendEvent(HttpMethod.GET, "/normal_get.xml", null);
        assertEquals(0, observer.getExceptionCount());
        System.err.println(responseBody.toString());
        JsendResult jsendResult = read(xmlProcessor, JsendResult.class);
        assertEquals(JsendResult.State.SUCCESS.toString(), jsendResult.getStatus());
        assertEquals("Normal GET action", jsendResult.getData());
        System.err.println(responseBody.toString());
    }
    
    @Test
    public void shouldWrapGetInJsendJson() {
        sendEvent(HttpMethod.GET, "/normal_get.json", null);
        assertEquals(1, observer.getReceivedCount());
        assertEquals(1, observer.getCompleteCount());
        assertEquals(1, observer.getSuccessCount());
        assertEquals(0, observer.getExceptionCount());
        JsendResult jsendResult = read(processor, JsendResult.class);
        assertEquals(JsendResult.State.SUCCESS.toString(), jsendResult.getStatus());
        assertEquals("Normal GET action", jsendResult.getData());
    }

    @Test
    public void shouldWrapGetInJsendJsonUsingQueryString() {
        sendEvent(HttpMethod.GET, "/normal_get?format=json", null);
        assertEquals(1, observer.getReceivedCount());
        assertEquals(1, observer.getCompleteCount());
        assertEquals(1, observer.getSuccessCount());
        assertEquals(0, observer.getExceptionCount());

        JsendResult jsendResult = read(processor, JsendResult.class);
        assertEquals(JsendResult.State.SUCCESS.toString(), jsendResult.getStatus());
        assertEquals("Normal GET action", jsendResult.getData());

    }

    @Test
    public void shouldWrapPutInJsendJson() {
        sendEvent(HttpMethod.PUT, "/normal_put.json", null);
        assertEquals(1, observer.getReceivedCount());
        assertEquals(1, observer.getCompleteCount());
        assertEquals(1, observer.getSuccessCount());
        assertEquals(0, observer.getExceptionCount());

        JsendResult jsendResult = read(processor, JsendResult.class);
        assertEquals(JsendResult.State.SUCCESS.toString(), jsendResult.getStatus());
        assertEquals("Normal PUT action", jsendResult.getData());
    }

    @Test
    public void shouldWrapPutInJsendJsonUsingQueryString() {
        sendEvent(HttpMethod.PUT, "/normal_put?format=json", null);
        assertEquals(1, observer.getReceivedCount());
        assertEquals(1, observer.getCompleteCount());
        assertEquals(1, observer.getSuccessCount());
        assertEquals(0, observer.getExceptionCount());

        JsendResult jsendResult = read(processor, JsendResult.class);
        assertEquals(JsendResult.State.SUCCESS.toString(), jsendResult.getStatus());
        assertEquals("Normal PUT action", jsendResult.getData());
    }

    @Test
    public void shouldWrapPostInJsendJson() {
        sendEvent(HttpMethod.POST, "/normal_post.json", null);
        assertEquals(1, observer.getReceivedCount());
        assertEquals(1, observer.getCompleteCount());
        assertEquals(1, observer.getSuccessCount());
        assertEquals(0, observer.getExceptionCount());

        JsendResult jsendResult = read(processor, JsendResult.class);
        assertEquals(JsendResult.State.SUCCESS.toString(), jsendResult.getStatus());
        assertEquals("Normal POST action", jsendResult.getData());
    }

    @Test
    public void shouldWrapPostInJsendJsonUsingQueryString() {
        sendEvent(HttpMethod.POST, "/normal_post?format=json", null);
        assertEquals(1, observer.getReceivedCount());
        assertEquals(1, observer.getCompleteCount());
        assertEquals(1, observer.getSuccessCount());
        assertEquals(0, observer.getExceptionCount());

        JsendResult jsendResult = read(processor, JsendResult.class);
        assertEquals(JsendResult.State.SUCCESS.toString(), jsendResult.getStatus());
        assertEquals("Normal POST action", jsendResult.getData());
    }

    @Test
    public void shouldWrapDeleteInJsendJson() {
        sendEvent(HttpMethod.DELETE, "/normal_delete.json", null);
        assertEquals(1, observer.getReceivedCount());
        assertEquals(1, observer.getCompleteCount());
        assertEquals(1, observer.getSuccessCount());
        assertEquals(0, observer.getExceptionCount());

        JsendResult jsendResult = read(processor, JsendResult.class);
        assertEquals(JsendResult.State.SUCCESS.toString(), jsendResult.getStatus());
        assertEquals("Normal DELETE action", jsendResult.getData());
    }

    @Test
    public void shouldWrapDeleteInJsendJsonUsingQueryString() {
        sendEvent(HttpMethod.DELETE, "/normal_delete?format=json", null);
        assertEquals(1, observer.getReceivedCount());
        assertEquals(1, observer.getCompleteCount());
        assertEquals(1, observer.getSuccessCount());
        assertEquals(0, observer.getExceptionCount());

        JsendResult jsendResult = read(processor, JsendResult.class);
        assertEquals(JsendResult.State.SUCCESS.toString(), jsendResult.getStatus());
        assertEquals("Normal DELETE action", jsendResult.getData());
    }

    @Test
    public void shouldWrapNotFoundInJsendJson() {
        sendEvent(HttpMethod.GET, "/not_found.json", null);
        assertEquals(1, observer.getReceivedCount());
        assertEquals(1, observer.getCompleteCount());
        assertEquals(0, observer.getSuccessCount());
        assertEquals(1, observer.getExceptionCount());

        JsendResult jsendResult = read(processor, JsendResult.class);
        assertEquals(JsendResult.State.ERROR.toString(), jsendResult.getStatus());
        assertEquals("NotFoundException", jsendResult.getData());
        assertEquals("Item not found", jsendResult.getMessage());
    }

    @Test
    public void shouldWrapNotFoundInJsendJsonUsingQueryString() {
        sendEvent(HttpMethod.GET, "/not_found?format=json", null);
        assertEquals(1, observer.getReceivedCount());
        assertEquals(1, observer.getCompleteCount());
        assertEquals(0, observer.getSuccessCount());
        assertEquals(1, observer.getExceptionCount());

        JsendResult jsendResult = read(processor, JsendResult.class);
        assertEquals(JsendResult.State.ERROR.toString(), jsendResult.getStatus());
        assertEquals("NotFoundException", jsendResult.getData());
        assertEquals("Item not found", jsendResult.getMessage());
    }

    @Test
    public void shouldWrapNullPointerInJsendJson() {
        sendEvent(HttpMethod.GET, "/null_pointer.json", null);
        assertEquals(1, observer.getReceivedCount());
        assertEquals(1, observer.getCompleteCount());
        assertEquals(0, observer.getSuccessCount());
        assertEquals(1, observer.getExceptionCount());

        JsendResult jsendResult = read(processor, JsendResult.class);
        assertEquals(JsendResult.State.FAIL.toString(), jsendResult.getStatus());
        assertEquals("NullPointerException", jsendResult.getData());
        assertEquals("Null and void", jsendResult.getMessage());
    }

    @Test
    public void shouldWrapNullPointerInJsendJsonUsingQueryString() {
        sendEvent(HttpMethod.GET, "/null_pointer?format=json", null);
        assertEquals(1, observer.getReceivedCount());
        assertEquals(1, observer.getCompleteCount());
        assertEquals(0, observer.getSuccessCount());
        assertEquals(1, observer.getExceptionCount());

        JsendResult jsendResult = read(processor, JsendResult.class);
        assertEquals(JsendResult.State.FAIL.toString(), jsendResult.getStatus());
        assertEquals("NullPointerException", jsendResult.getData());
        assertEquals("Null and void", jsendResult.getMessage());
    }

    @Test
    public void shouldWrapInvalidUrlWithJsonFormat() {
        sendEvent(HttpMethod.GET, "/xyzt.json", null);
        assertEquals(1, observer.getReceivedCount());
        assertEquals(1, observer.getCompleteCount());
        assertEquals(0, observer.getSuccessCount());
        assertEquals(1, observer.getExceptionCount());

        JsendResult jsendResult = read(processor, JsendResult.class);
        assertEquals(JsendResult.State.ERROR.toString(), jsendResult.getStatus());
        assertEquals("NotFoundException", jsendResult.getData());
        assertEquals("Unresolvable URL: http://null/xyzt.json", jsendResult.getMessage());
    }

    @Test
    public void shouldWrapInvalidUrlWithJsonFormatUsingQueryString() {
        sendEvent(HttpMethod.GET, "/xyzt?format=json", null);
        assertEquals(1, observer.getReceivedCount());
        assertEquals(1, observer.getCompleteCount());
        assertEquals(0, observer.getSuccessCount());
        assertEquals(1, observer.getExceptionCount());

        JsendResult jsendResult = read(processor, JsendResult.class);
        assertEquals(JsendResult.State.ERROR.toString(), jsendResult.getStatus());
        assertEquals("NotFoundException", jsendResult.getData());
        assertEquals("Unresolvable URL: http://null/xyzt?format=json", jsendResult.getMessage());
    }

  
}
