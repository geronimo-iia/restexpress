/**
 * Licensed to the Apache Software Foundation (ASF) under one or more contributor license agreements. See the NOTICE file distributed
 * with this work for additional information regarding copyright ownership. The ASF licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License
 * at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS"
 * BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing
 * permissions and limitations under the License.
 * 
 */
package org.restexpress.pipeline;

import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import org.restexpress.domain.CharacterSet;
import org.restexpress.pipeline.handler.DefaultRequestHandler;
import org.restexpress.pipeline.observer.CounterMessageObserver;
import org.restexpress.pipeline.writer.StringBufferHttpResponseWriter;
import org.restexpress.route.RouteDeclaration;
import org.restexpress.serialization.Processor;
import org.restexpress.util.TestUtilities;

public class AbstractWrapperResponse {

    protected DefaultRequestHandler messageHandler;
    protected CounterMessageObserver observer;
    protected Channel channel;
    protected ChannelPipeline pl;
    protected StringBuffer responseBody;
    protected Map<String, List<String>> responseHeaders;

    /**
     * Utility to read result.
     * 
     * @param processor
     * @param valueType
     * @return
     */
    protected <T> T read(Processor processor, Class<T> valueType) {
        return processor.read(ChannelBuffers.copiedBuffer(responseBody.toString(), CharacterSet.UTF_8.getCharset()), valueType);
    }

    /**
     * Initialize route and pipeline.
     * 
     * @param routes
     * @throws Exception
     */
    protected void initialize(RouteDeclaration routes) throws Exception {

        responseBody = new StringBuffer();
        responseHeaders = new HashMap<String, List<String>>();

        messageHandler = TestUtilities.newDefaultRequestHandler(routes, new StringBufferHttpResponseWriter(responseHeaders,
                responseBody));

        observer = new CounterMessageObserver();
        messageHandler.dispatcher().addMessageObserver(observer);

        PipelineBuilder pf = new PipelineBuilder().addRequestHandler(messageHandler);
        pl = pf.getPipeline();
        ChannelFactory channelFactory = new DefaultLocalServerChannelFactory();
        channel = channelFactory.newChannel(pl);
    }

    /**
     * Utility to send an event.
     * 
     * @param method
     * @param path
     * @param body
     */
    protected void sendEvent(HttpMethod method, String path, String body) {
        HttpRequest request = new DefaultHttpRequest(HttpVersion.HTTP_1_1, method, path);
        if (body != null) {
            request.setContent(ChannelBuffers.copiedBuffer(body, CharacterSet.UTF_8.getCharset()));
        }
        pl.sendUpstream(new UpstreamMessageEvent(channel, request, new InetSocketAddress(1)));
    }

    /**
     * Route Declaration for JSendResult test.
     */
    public class DummyRoutes extends RouteDeclaration {

        private Object controller = new WrappedResponseController();

        public DummyRoutes defineRoutes() {
            uri("/normal_get.{format}", controller).action("normalGetAction", HttpMethod.GET);

            uri("/normal_put.{format}", controller).action("normalPutAction", HttpMethod.PUT);

            uri("/normal_post.{format}", controller).action("normalPostAction", HttpMethod.POST);

            uri("/normal_delete.{format}", controller).action("normalDeleteAction", HttpMethod.DELETE);

            uri("/no_content_delete.{format}", controller).action("noContentDeleteAction", HttpMethod.DELETE);

            uri("/no_content_with_body_delete.{format}", controller).action("noContentWithBodyDeleteActionThrowException",
                    HttpMethod.DELETE);
            uri("/not_found.{format}", controller).action("notFoundAction", HttpMethod.GET);

            uri("/null_pointer.{format}", controller).action("nullPointerAction", HttpMethod.GET);
            return this;
        }
    }
}
