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
package org.restexpress.util;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.handler.codec.http.HttpRequest;
import org.restexpress.Request;
import org.restexpress.SerializationProvider;
import org.restexpress.domain.CharacterSet;
import org.restexpress.pipeline.DefaultHttpResponseWriter;
import org.restexpress.pipeline.DefaultRequestHandler;
import org.restexpress.pipeline.HttpResponseWriter;
import org.restexpress.response.ResponseProcessorManager;
import org.restexpress.route.RouteDeclaration;
import org.restexpress.route.RouteResolver;
import org.restexpress.serialization.Processor;

/**
 * {@link TestUtilities} give utilities method for testing purpose.
 * 
 * @author <a href="mailto:jguibert@intelligents-ia.com" >Jerome Guibert</a>
 * 
 */
public enum TestUtilities {
	;

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

	public static DefaultRequestHandler newDefaultRequestHandler(RouteDeclaration routeDeclaration) {
		return new DefaultRequestHandler(new RouteResolver(routeDeclaration.createRouteMapping()), new ResponseProcessorManager(), new DefaultHttpResponseWriter(), false);
	}

	public static DefaultRequestHandler newDefaultRequestHandler(RouteDeclaration routeDeclaration, HttpResponseWriter httpResponseWriter) {
		return new DefaultRequestHandler(new RouteResolver(routeDeclaration.createRouteMapping()), new ResponseProcessorManager(), httpResponseWriter, false);
	}
}
