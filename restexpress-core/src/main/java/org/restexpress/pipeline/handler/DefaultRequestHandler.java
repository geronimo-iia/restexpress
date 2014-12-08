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
import org.jboss.netty.handler.codec.http.HttpRequest;
import org.restexpress.Request;
import org.restexpress.Response;
import org.restexpress.SerializationProvider;
import org.restexpress.pipeline.HttpResponseWriter;
import org.restexpress.pipeline.MessageContext;
import org.restexpress.pipeline.MessageObserverDispatcher;
import org.restexpress.pipeline.Postprocessor;
import org.restexpress.pipeline.Preprocessor;
import org.restexpress.response.ResponseProcessorManager;
import org.restexpress.response.ResponseProcessorSetting;
import org.restexpress.route.Action;
import org.restexpress.route.RouteResolver;
import org.restexpress.util.HttpSpecification;

import com.google.common.base.Preconditions;

/**
 * {@link DefaultRequestHandler} implements an {@link AbstractRequestHandler}
 * for our {@link ResponseProcessorSetting} and {@link RouteResolver}.
 * 
 * @author <a href="mailto:jguibert@intelligents-ia.com" >Jerome Guibert</a>
 * @author toddf
 * @since Nov 13, 2009
 */
@Sharable
public final class DefaultRequestHandler extends AbstractRequestHandler {

	private final RouteResolver routeResolver;
	private final ResponseProcessorManager responseProcessorManager;

	public DefaultRequestHandler(final Preprocessor[] preprocessors, final Postprocessor[] postprocessors, final Postprocessor[] finallyProcessors, final HttpResponseWriter responseWriter, final boolean shouldEnforceHttpSpec,
			final MessageObserverDispatcher dispatcher, final RouteResolver routeResolver, final ResponseProcessorManager responseProcessorManager) {
		super(preprocessors, postprocessors, finallyProcessors, responseWriter, shouldEnforceHttpSpec, dispatcher);
		this.routeResolver = Preconditions.checkNotNull(routeResolver);
		this.responseProcessorManager = Preconditions.checkNotNull(responseProcessorManager);
	}

	/**
	 * @return {@link SerializationProvider}.
	 */
	public SerializationProvider serializationProvider() {
		return responseProcessorManager;
	}

	/**
	 * @return {@link RouteResolver}.
	 */
	public RouteResolver routeResolver() {
		return routeResolver;
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
	protected void handleResponseContent(final MessageContext context, final boolean force) {
		final Response response = context.getResponse();
		if (HttpSpecification.isContentTypeAllowed(response)) {
			/*
			 * Mind ResponseProcessorSetting is something that can handle
			 * exception and Serialization. We assume that for a File, we have
			 * no serialization on response.
			 */
			ResponseProcessorSetting settings = context.getResponseProcessorSetting();
			if ((settings == null) && force) {
				settings = responseProcessorManager.resolve(context.getRequest(), response, force);
			}
			// process serialization if one was found
			if (settings != null) {
				settings.serialize(response);
			}
		}
	}

}
