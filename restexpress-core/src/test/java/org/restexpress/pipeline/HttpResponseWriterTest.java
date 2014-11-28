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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.junit.Test;
import org.restexpress.Request;
import org.restexpress.Response;
import org.restexpress.RestExpress;

import com.google.common.io.ByteStreams;
import com.google.common.io.Closer;

/**
 * {@link HttpResponseWriter} test case.
 * 
 * 
 * @author <a href="mailto:jguibert@intelligents-ia.com" >Jerome Guibert</a>
 */
public class HttpResponseWriterTest {
    private static final String TEST_PATH = "/restexpress/test1";
    private static final int TEST_PORT = 8901;
    private static final String TEST_URL = "http://localhost:" + TEST_PORT + TEST_PATH;

    public HttpResponseWriterTest() {

    }

    // Useful for debug..
    public static void main(String[] args) {
        RestExpress restExpress = new RestExpress();
        NoopController controller = new NoopController();
        restExpress.uri(TEST_PATH, controller);
        restExpress.bind(TEST_PORT);
        restExpress.awaitShutdown();
        restExpress.shutdown();
    }

    @Test
    public void testFileAccess() throws ClientProtocolException, IOException {
        RestExpress restExpress = new RestExpress();
        NoopController controller = new NoopController();
        restExpress.uri(TEST_PATH, controller);
        restExpress.bind(TEST_PORT);

        HttpClient client = new DefaultHttpClient();
        HttpGet get = new HttpGet(TEST_URL);
        HttpResponse response = (HttpResponse) client.execute(get);
        assertEquals(200, response.getStatusLine().getStatusCode());
        File result = File.createTempFile("restexpress", "result");

        final Closer closer = Closer.create();
        try {
            // get a copy.

            final OutputStream out = closer.register(new FileOutputStream(result));
            response.getEntity().writeTo(out);

            out.flush();
        } catch (final Throwable e) {
            e.printStackTrace();
            if (result != null)
                result.delete();
            result = null;
        } finally {
            closer.close();
        }

        get.releaseConnection();

        final InputStream in = closer.register(new FileInputStream(result));
        ByteStreams.copy(in, System.out);
        closer.close();

    }

    public static class NoopController {

        public File read(Request req, Response res) {
            return new File("src/test/resources/unicode-chinese.json");
        }

    }
}
