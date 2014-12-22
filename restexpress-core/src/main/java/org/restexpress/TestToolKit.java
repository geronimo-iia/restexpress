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
package org.restexpress;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.handler.codec.http.HttpRequest;
import org.restexpress.domain.CharacterSet;
import org.restexpress.pipeline.HttpResponseWriter;
import org.restexpress.pipeline.handler.RestExpressRequestHandler;
import org.restexpress.pipeline.handler.RestExpressRequestHandlerBuilder;
import org.restexpress.processor.DefaultContentTypeFinallyProcessor;
import org.restexpress.processor.FileHeaderPostProcessor;
import org.restexpress.response.ResponseProcessorManager;
import org.restexpress.response.SerializationProvider;
import org.restexpress.route.RouteDeclaration;
import org.restexpress.serialization.Processor;

/**
 * {@link TestToolKit} give utilities method for testing purpose.
 * 
 * @author <a href="mailto:jguibert@intelligents-ia.com" >Jerome Guibert</a>
 * 
 */
public final class TestToolKit {

    public static final String BASE_URL = "http://localhost:8081";

    public static Request newRequest(HttpRequest httpRequest) {
        return new Request(httpRequest, null);
    }

    public static String serialize(Object value, Processor processor) {
        ChannelBuffer channelBuffer = ChannelBuffers.dynamicBuffer();
        processor.write(value, channelBuffer);
        return channelBuffer.toString(CharacterSet.UTF_8.getCharset());
    }

    public static <T> T deserialize(String buffer, Class<T> valueType, Processor processor) {
        return buffer == null ? null : processor.read(ChannelBuffers.copiedBuffer(buffer, CharacterSet.UTF_8.getCharset()), valueType);
    }

    public static <T> T deserialize(ChannelBuffer buffer, Class<T> valueType, Processor processor) {
        return processor.read(buffer, valueType);
    }

    public static SerializationProvider newSerializationProvider() {
        return new ResponseProcessorManager();
    }

    public static RestExpressRequestHandlerBuilder newBuilder(RouteDeclaration routeDeclaration) {
        RestExpressRequestHandlerBuilder builder = RestExpressRequestHandlerBuilder.newBuilder()//
                .addFinallyProcessor(new DefaultContentTypeFinallyProcessor())//
                .addPostprocessor(new FileHeaderPostProcessor());
        return builder.setRouteResolver(routeDeclaration.createRouteMapping(BASE_URL))//
                .setShouldEnforceHttpSpec(false);
    }

    public static RestExpressRequestHandler newDefaultRequestHandler(RouteDeclaration routeDeclaration) {
        return (RestExpressRequestHandler) newBuilder(routeDeclaration).build();
    }

    public static RestExpressRequestHandler newDefaultRequestHandler(RouteDeclaration routeDeclaration,
            HttpResponseWriter httpResponseWriter) {
        return (RestExpressRequestHandler) newBuilder(routeDeclaration).setHttpResponseWriter(httpResponseWriter).build();
    }
}
