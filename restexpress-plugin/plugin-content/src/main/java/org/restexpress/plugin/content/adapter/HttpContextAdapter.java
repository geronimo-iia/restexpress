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
package org.restexpress.plugin.content.adapter;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.ExecutionException;

import org.restexpress.plugin.content.ContextAdapter;
import org.restexpress.plugin.content.resolver.Resolver;

import com.ning.http.client.AsyncHttpClient;
import com.ning.http.client.AsyncHttpClientConfig;
import com.ning.http.client.ListenableFuture;
import com.ning.http.client.Response;

/**
 * {@link HttpContextAdapter} implement a {@link ContextAdapter} which retrieve resource from another HTTP resource provider.
 * 
 * @author <a href="mailto:jguibert@intelligents-ia.com" >Jerome Guibert</a>
 */
public class HttpContextAdapter extends AbstractContextAdapter<AsyncHttpClient> {

    private final AsyncHttpClient asyncHttpClient;

    /**
     * Build a new instance of {@link HttpContextAdapter} with default {@link AsyncHttpClient} (compression and pooling connection
     * enabled, and request timeout set to 30000 ms).
     */
    public HttpContextAdapter(String name, Resolver resolver, String remoteDocumentBase, File tempDirectory)
            throws NullPointerException {
        this(name, resolver, remoteDocumentBase, tempDirectory, new AsyncHttpClient(new AsyncHttpClientConfig.Builder()
                .setCompressionEnabled(true)//
                .setAllowPoolingConnection(true)//
                .setRequestTimeoutInMs(30000).build()));
    }

    public HttpContextAdapter(String name, Resolver resolver, String remoteDocumentBase, File tempDirectory,
            AsyncHttpClient asyncHttpClient) {
        super(name, resolver, remoteDocumentBase, tempDirectory);
        this.asyncHttpClient = asyncHttpClient;
    }

    @Override
    protected AsyncHttpClient createContext() {
        return asyncHttpClient;
    }

    @Override
    protected void destroyContext(AsyncHttpClient context) {
        // nothing to do
    }

    @Override
    protected boolean exists(AsyncHttpClient context, String remotePath) throws IOException {
        final ListenableFuture<Response> response = asyncHttpClient.prepareHead(remotePath).execute();
        try {
            final int status = response.get().getStatusCode();
            return status == 200;
        } catch (final InterruptedException e) {
            throw new IOException(e);
        } catch (final ExecutionException e) {
            throw new IOException(e);
        }
    }

    @Override
    protected InputStream get(AsyncHttpClient context, String remotePath) throws IOException {
        try {
            final Response response = asyncHttpClient.prepareGet(remotePath).execute().get();
            if (response.getStatusCode() == 200)
                return response.getResponseBodyAsStream();
        } catch (final InterruptedException e) {
            throw new IOException(e);
        } catch (final ExecutionException e) {
            throw new IOException(e);
        }
        return null;
    }

}
