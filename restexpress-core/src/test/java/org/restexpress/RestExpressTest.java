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
package org.restexpress;

import static org.junit.Assert.*;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.impl.client.DefaultHttpClient;
import org.intelligentsia.commons.http.ResponseHeader;
import org.jboss.netty.handler.codec.http.HttpHeaders;
import org.junit.Test;
import org.restexpress.domain.Format;
import org.restexpress.plugin.DummyPluginInterface;
import org.restexpress.plugin.PluginA;
import org.restexpress.plugin.PluginB;

/**
 * {@link RestExpress} test case.
 * 
 * @author toddf
 * @author <a href="mailto:jguibert@intelligents-ia.com" >Jerome Guibert</a>
 * @since Jan 28, 2011
 */
public class RestExpressTest {
    private static final String TEST_PATH = "/restexpress/test1";
    private static final int TEST_PORT = 8901;
    private static final String TEST_URL = "http://localhost:" + TEST_PORT + TEST_PATH;

    @Test
    public void lookupPluginByInterface() {
        RestExpress restExpress = new RestExpress();
        restExpress.registerPlugin(new PluginA());
        restExpress.registerPlugin(new PluginB());
        assertTrue(restExpress.findPlugin(DummyPluginInterface.class) != null);
    }

    @Test
    public void lookupPluginByName() {
        RestExpress restExpress = new RestExpress();
        assertTrue(restExpress.findPlugin("PluginB") == null);
        restExpress.registerPlugin(new PluginB());
        assertTrue(restExpress.findPlugin("PluginB") != null);
    }

    @Test
    public void shouldUseDefaults() {
        RestExpress restExpress = new RestExpress();
        assertTrue(restExpress.serializationProvider().processor(Format.JSON.getMediaType()) != null);
        assertTrue(restExpress.serializationProvider().processor(Format.JSON.getMediaType())
                .equals(restExpress.serializationProvider().defaultProcessor()));
        assertTrue(restExpress.serializationProvider().processor(Format.XML.getMediaType()) != null);
        assertTrue(restExpress.serializationProvider().processor(Format.TEXT.getMediaType()) != null);
        assertEquals(8081, restExpress.settings().serverSettings().getPort());
        assertTrue(restExpress.messageObservers().isEmpty());
        assertTrue(restExpress.postprocessors().isEmpty());
        assertTrue(restExpress.preprocessors().isEmpty());
        assertTrue(restExpress.settings().serverSettings().isUseSystemOut());
    }

    @Test
    public void shouldCallDefaultMethods() throws ClientProtocolException, IOException {
        RestExpress re = new RestExpress();
        NoopController controller = new NoopController();
        re.uri(TEST_PATH, controller);
        re.bind(TEST_PORT);

        HttpClient client = new DefaultHttpClient();
        HttpGet get = new HttpGet(TEST_URL);
        HttpResponse response = (HttpResponse) client.execute(get);
        assertEquals(200, response.getStatusLine().getStatusCode());
        assertEquals(1, controller.read);
        assertEquals(0, controller.create);
        assertEquals(0, controller.update);
        assertEquals(0, controller.delete);
        get.releaseConnection();

        HttpPost post = new HttpPost(TEST_URL);
        response = (HttpResponse) client.execute(post);
        assertEquals(201, response.getStatusLine().getStatusCode());
        assertEquals(1, controller.create);
        assertEquals(1, controller.read);
        assertEquals(0, controller.update);
        assertEquals(0, controller.delete);
        post.releaseConnection();

        HttpPut put = new HttpPut(TEST_URL);
        response = (HttpResponse) client.execute(put);
        assertEquals(200, response.getStatusLine().getStatusCode());
        assertEquals(1, controller.update);
        assertEquals(1, controller.read);
        assertEquals(1, controller.create);
        assertEquals(0, controller.delete);
        put.releaseConnection();

        HttpDelete delete = new HttpDelete(TEST_URL);
        response = (HttpResponse) client.execute(delete);
        assertEquals(200, response.getStatusLine().getStatusCode());
        assertEquals(1, controller.delete);
        assertEquals(1, controller.read);
        assertEquals(1, controller.create);
        assertEquals(1, controller.update);
        delete.releaseConnection();

        re.shutdown();
    }

    @Test
    public void shouldSetOutputMediaType() throws ClientProtocolException, IOException {
        RestExpress re = new RestExpress();
        NoopController controller = new NoopController();
        re.uri(TEST_PATH, controller);
        re.bind(TEST_PORT);

        HttpClient client = new DefaultHttpClient();

        HttpPost post = new HttpPost(TEST_URL);
        post.addHeader(HttpHeaders.Names.ACCEPT, "application/json");
        HttpResponse response = (HttpResponse) client.execute(post);
        assertEquals(201, response.getStatusLine().getStatusCode());
        assertEquals("application/json; charset=UTF-8", response.getFirstHeader(ResponseHeader.CONTENT_TYPE.getHeader()).getValue());
        post.releaseConnection();

        HttpGet get = new HttpGet(TEST_URL);
        get.addHeader(HttpHeaders.Names.ACCEPT, "application/json");
        response = (HttpResponse) client.execute(get);
        assertEquals(200, response.getStatusLine().getStatusCode());
        assertEquals("application/json; charset=UTF-8", response.getFirstHeader(ResponseHeader.CONTENT_TYPE.getHeader()).getValue());
        get.releaseConnection();

        HttpPut put = new HttpPut(TEST_URL);
        put.addHeader(HttpHeaders.Names.ACCEPT, "application/json");
        response = (HttpResponse) client.execute(put);
        assertEquals(200, response.getStatusLine().getStatusCode());
        assertEquals("application/json; charset=UTF-8", response.getFirstHeader(ResponseHeader.CONTENT_TYPE.getHeader()).getValue());
        put.releaseConnection();

        HttpDelete delete = new HttpDelete(TEST_URL);
        delete.addHeader(HttpHeaders.Names.ACCEPT, "application/json");
        response = (HttpResponse) client.execute(delete);
        assertEquals(200, response.getStatusLine().getStatusCode());
        assertEquals("application/json; charset=UTF-8", response.getFirstHeader(ResponseHeader.CONTENT_TYPE.getHeader()).getValue());
        delete.releaseConnection();

        re.shutdown();
    }

    public class NoopController {
        int create, read, update, delete = 0;

        public void create(Request req, Response res) {
            ++create;
            res.setResponseCreated();
        }

        public void read(Request req, Response res) {
            ++read;
        }

        public void update(Request req, Response res) {
            ++update;
        }

        public void delete(Request req, Response res) {
            ++delete;
        }
    }
}
