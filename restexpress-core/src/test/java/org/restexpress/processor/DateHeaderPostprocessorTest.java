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
 * Copyright 2011, Strategic Gains, Inc.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You
 * may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS"
 * BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing
 * permissions and limitations under the License.
 */
package org.restexpress.processor;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.text.ParseException;
import java.util.Date;

import org.intelligentsia.commons.http.HttpHeaderDateTimeFormat;
import org.jboss.netty.handler.codec.http.DefaultHttpRequest;
import org.jboss.netty.handler.codec.http.HttpHeaders;
import org.jboss.netty.handler.codec.http.HttpMethod;
import org.jboss.netty.handler.codec.http.HttpRequest;
import org.jboss.netty.handler.codec.http.HttpVersion;
import org.junit.Test;
import org.restexpress.Response;
import org.restexpress.pipeline.MessageContext;
import org.restexpress.pipeline.Postprocessor;
import org.restexpress.util.TestUtilities;

/**
 * 
 * @author <a href="mailto:jguibert@intelligents-ia.com" >Jerome Guibert</a>
 * @author toddf
 * @since Oct 4, 2011
 */
public class DateHeaderPostprocessorTest {
    private Postprocessor processor = new DateHeaderPostprocessor();

    @Test
    public void shouldAddDateHeaderOnGet() throws ParseException {
        HttpRequest httpRequest = new DefaultHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.GET, "/foo?param1=bar&param2=blah&yada");
        httpRequest.headers().add("Host", "testing-host");
        Response response = new Response();
        processor.process(new MessageContext(TestUtilities.newRequest(httpRequest), response));
        assertTrue(response.hasHeaders());
        String dateHeader = response.getHeader(HttpHeaders.Names.DATE);
        assertNotNull(dateHeader);
        Date dateValue = HttpHeaderDateTimeFormat.parseAny(dateHeader);
        assertNotNull(dateValue);
    }

    @Test
    public void shouldAddDateHeaderOnHead() throws ParseException {
        HttpRequest httpRequest = new DefaultHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.HEAD, "/foo?param1=bar&param2=blah&yada");
        httpRequest.headers().add("Host", "testing-host");
        Response response = new Response();
        processor.process(new MessageContext(TestUtilities.newRequest(httpRequest), response));
        assertTrue(response.hasHeaders());
        String dateHeader = response.getHeader(HttpHeaders.Names.DATE);
        assertNotNull(dateHeader);
        Date dateValue = HttpHeaderDateTimeFormat.parseAny(dateHeader);
        assertNotNull(dateValue);
    }

    @Test
    public void shouldNotAddDateHeaderOnPost() {
        HttpRequest httpRequest = new DefaultHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.POST, "/foo?param1=bar&param2=blah&yada");
        httpRequest.headers().add("Host", "testing-host");
        Response response = new Response();
        processor.process(new MessageContext(TestUtilities.newRequest(httpRequest), response));
        assertFalse(response.hasHeader(HttpHeaders.Names.DATE));
    }

    @Test
    public void shouldNotAddDateHeaderOnPut() {
        HttpRequest httpRequest = new DefaultHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.PUT, "/foo?param1=bar&param2=blah&yada");
        httpRequest.headers().add("Host", "testing-host");
        Response response = new Response();
        processor.process(new MessageContext(TestUtilities.newRequest(httpRequest), response));
        assertFalse(response.hasHeader(HttpHeaders.Names.DATE));
    }

    @Test
    public void shouldNotAddDateHeaderOnDelete() {
        HttpRequest httpRequest = new DefaultHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.DELETE, "/foo?param1=bar&param2=blah&yada");
        httpRequest.headers().add("Host", "testing-host");
        Response response = new Response();
        processor.process(new MessageContext(TestUtilities.newRequest(httpRequest), response));
        assertFalse(response.hasHeader(HttpHeaders.Names.DATE));
    }

    @Test
    public void shouldNotAddDateHeaderOnOptions() {
        HttpRequest httpRequest = new DefaultHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.OPTIONS, "/foo?param1=bar&param2=blah&yada");
        httpRequest.headers().add("Host", "testing-host");
        Response response = new Response();
        processor.process(new MessageContext(TestUtilities.newRequest(httpRequest), response));
        assertFalse(response.hasHeader(HttpHeaders.Names.DATE));
    }
}