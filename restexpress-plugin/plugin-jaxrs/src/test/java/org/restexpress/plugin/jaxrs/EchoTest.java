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
package org.restexpress.plugin.jaxrs;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.restexpress.RestExpressService;
import org.restexpress.observer.SimpleConsoleLogMessageObserver;

/**
 * {@link EchoTest}
 * 
 * 
 */
public class EchoTest {
    private RestExpressService server;

    private HttpClient http = new DefaultHttpClient();

    @Before
    public void createServer() {
        server = RestExpressService.newBuilder();
        server.addMessageObserver(new SimpleConsoleLogMessageObserver());
        server.bind();
    }

    @After
    public void shutdownServer() {
        server.shutdown();
    }

    @Test
    @SuppressWarnings("unused")
    public void shouldHandleGetRequests() throws Exception {

        HttpGet request = new HttpGet("http://localhost:8081/");
        HttpResponse response = (HttpResponse) http.execute(request);
        // assertEquals(HttpResponseStatus.OK.getCode(), response.getStatusLine().getStatusCode());
        // HttpEntity entity = response.getEntity();

        request.releaseConnection();
    }
}
