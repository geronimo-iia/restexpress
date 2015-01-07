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
import org.restexpress.TestToolKit;
import org.restexpress.domain.CharacterSet;
import org.restexpress.observer.CounterMessageObserver;
import org.restexpress.pipeline.handler.RestExpressRequestHandler;
import org.restexpress.pipeline.handler.RestExpressRequestHandlerBuilder;
import org.restexpress.pipeline.writer.StringBufferHttpResponseWriter;
import org.restexpress.route.RouteDeclaration;
import org.restexpress.serialization.Processor;

/**
 * {@link AbstractWrapperResponse} is a class facility for testing all wrapper.
 * 
 * @author <a href="mailto:jguibert@intelligents-ia.com" >Jerome Guibert</a>
 */
public class AbstractWrapperResponse {

    protected RestExpressRequestHandler messageHandler;
    protected CounterMessageObserver observer;
    protected Channel channel;
    protected ChannelPipeline pl;
    protected StringBuffer responseBody;
    protected Map<String, List<String>> responseHeaders;
    protected RestExpressRequestHandlerBuilder builder;
    protected RouteDeclaration routes = new RouteDeclaration();

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
        preInitialize(routes);
        messageHandler = build(builder);
        postInitialize();
    }

    protected void postInitialize() throws Exception {
        messageHandler = build(builder);
        RestExpressPipelineFactory pipelineFactory = new RestExpressPipelineFactory().addRequestHandler(messageHandler);
        pl = pipelineFactory.getPipeline();
        ChannelFactory channelFactory = new DefaultLocalServerChannelFactory();
        channel = channelFactory.newChannel(pl);
    }

    protected void preInitialize(RouteDeclaration routes) {
        responseBody = new StringBuffer();
        responseHeaders = new HashMap<String, List<String>>();
        observer = new CounterMessageObserver();
        builder = TestToolKit.newBuilder(routes)//
                .setHttpResponseWriter(new StringBufferHttpResponseWriter(responseHeaders, responseBody))//
                .addMessageObserver(observer);
    }

    protected RestExpressRequestHandler build(RestExpressRequestHandlerBuilder builder) throws Exception {
        return builder.build();
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

    protected void sendGetEvent(String path, String body) {
        sendEvent(HttpMethod.GET, path, body);
    }

    protected void sendGetEvent(String path) {
        sendEvent(HttpMethod.GET, path, null);
    }

    public RouteDeclaration defineRoutes(RouteDeclaration routeDeclaration) {
        WrappedResponseController controller = new WrappedResponseController();

        routeDeclaration.uri("/normal_get.{format}", controller).action("normalGetAction", HttpMethod.GET);

        routeDeclaration.uri("/normal_put.{format}", controller).action("normalPutAction", HttpMethod.PUT);

        routeDeclaration.uri("/normal_post.{format}", controller).action("normalPostAction", HttpMethod.POST);

        routeDeclaration.uri("/normal_delete.{format}", controller).action("normalDeleteAction", HttpMethod.DELETE);

        routeDeclaration.uri("/no_content_delete.{format}", controller).action("noContentDeleteAction", HttpMethod.DELETE);

        routeDeclaration.uri("/no_content_with_body_delete.{format}", controller).action(
                "noContentWithBodyDeleteActionThrowException", HttpMethod.DELETE);
        routeDeclaration.uri("/not_found.{format}", controller).action("notFoundAction", HttpMethod.GET);

        routeDeclaration.uri("/null_pointer.{format}", controller).action("nullPointerAction", HttpMethod.GET);
        return routeDeclaration;
    }

}
