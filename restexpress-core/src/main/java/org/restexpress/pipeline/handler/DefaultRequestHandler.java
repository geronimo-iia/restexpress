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
package org.restexpress.pipeline.handler;

import java.net.InetSocketAddress;

import org.jboss.netty.channel.ChannelHandler.Sharable;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.handler.codec.http.HttpHeaders;
import org.jboss.netty.handler.codec.http.HttpRequest;
import org.restexpress.Request;
import org.restexpress.Response;
import org.restexpress.SerializationProvider;
import org.restexpress.domain.CharacterSet;
import org.restexpress.domain.MediaType;
import org.restexpress.pipeline.HttpResponseWriter;
import org.restexpress.pipeline.MessageContext;
import org.restexpress.response.ResponseProcessorManager;
import org.restexpress.response.ResponseProcessorSetting;
import org.restexpress.response.ResponseProcessorSettingResolver;
import org.restexpress.route.Action;
import org.restexpress.route.RouteResolver;
import org.restexpress.util.HttpSpecification;

/**
 * @author toddf
 * @since Nov 13, 2009
 */
@Sharable
public class DefaultRequestHandler extends AbstractRequestHandler {

	private final RouteResolver routeResolver;
	private final ResponseProcessorManager responseProcessorManager;

	/**
	 * Build a new instance of {@link DefaultRequestHandler}.
	 * 
	 * @param routeResolver
	 *            {@link RouteResolver}.
	 * @param responseProcessorManager
	 *            {@link ResponseProcessorManager}
	 * @param responseWriter
	 *            {@link HttpResponseWriter}
	 * @param enforceHttpSpec
	 */
	public DefaultRequestHandler(final RouteResolver routeResolver, //
			final ResponseProcessorManager responseProcessorManager,//
			final HttpResponseWriter responseWriter, final boolean enforceHttpSpec) {
		super(responseWriter, enforceHttpSpec);
		this.routeResolver = routeResolver;
		this.responseProcessorManager = responseProcessorManager;
	}

	/**
	 * Build a new instance of {@link DefaultRequestHandler}.
	 * 
	 * @param routeResolver
	 *            {@link RouteResolver}.
	 * @param responseProcessorSettingResolver
	 *            {@link ResponseProcessorSettingResolver}
	 * @param responseWriter
	 *            {@link HttpResponseWriter}
	 * @param enforceHttpSpec
	 */
	public DefaultRequestHandler(final RouteResolver routeResolver, //
			final HttpResponseWriter responseWriter, final boolean enforceHttpSpec) {
		this(routeResolver, new ResponseProcessorManager(), responseWriter, enforceHttpSpec);
	}

	/**
	 * Testing purpose.
	 * 
	 * @return {@link SerializationProvider}.
	 */
	public SerializationProvider serializationProvider() {
		return responseProcessorManager;
	}

	@Override
	protected void resolveResponseProcessor(final MessageContext context) {
		final ResponseProcessorSetting setting = responseProcessorManager.resolve(context.getRequest(), context.getResponse(), false);
		context.setResponseProcessorSetting(setting);
	}

	@Override
	protected void resolveRoute(final MessageContext context) {
		final Action action = routeResolver.resolve(context);
		context.setAction(action);
	}

	@Override
	protected Request createRequest(final MessageEvent event, final ChannelHandlerContext context) {
		return new Request((HttpRequest) event.getMessage(), routeResolver, responseProcessorManager, (InetSocketAddress) event.getRemoteAddress());
	}

	@Override
	protected Response createResponse() {
		return new Response();
	}

	@Override
	protected void serializeResponse(final MessageContext context, final boolean force) {
		final Response response = context.getResponse();
		if (HttpSpecification.isContentTypeAllowed(response)) {
			if (response.isSerialized()) {
				ResponseProcessorSetting settings = context.getResponseProcessorSetting();
				if (settings == null && force) {
					settings = responseProcessorManager.resolve(context.getRequest(), response, force);
				}
				if (settings != null) {
					settings.serialize(response);
				}
			}
			// add default content type if none was provided only if content type is allowed
			if (!response.hasHeader(HttpHeaders.Names.CONTENT_TYPE)) {
				response.setContentType(MediaType.TEXT_PLAIN.withCharset(CharacterSet.UTF_8.getCharsetName()));
			}
		}
	}
}
