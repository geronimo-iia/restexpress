/*
 * Copyright 2009, Strategic Gains, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 *
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.restexpress.pipeline;

import java.util.ArrayList;
import java.util.List;

import org.intelligentsia.commons.http.exception.HttpRuntimeException;
import org.jboss.netty.channel.ChannelHandler.Sharable;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;
import org.jboss.netty.handler.codec.http.HttpHeaders;
import org.jboss.netty.handler.codec.http.HttpResponseStatus;
import org.restexpress.ContentType;
import org.restexpress.Request;
import org.restexpress.Response;
import org.restexpress.common.exception.Exceptions;
import org.restexpress.response.HttpResponseWriter;
import org.restexpress.route.Action;
import org.restexpress.route.RouteResolver;
import org.restexpress.serialization.SerializationProvider;
import org.restexpress.serialization.SerializationSettings;
import org.restexpress.util.HttpSpecification;

/**
 * @author toddf
 * @since Nov 13, 2009
 */
@Sharable
public class DefaultRequestHandler extends SimpleChannelUpstreamHandler {
	// SECTION: INSTANCE VARIABLES

	private final RouteResolver routeResolver;
	private final SerializationProvider serializationProvider;
	private HttpResponseWriter responseWriter;
	private final List<Preprocessor> preprocessors = new ArrayList<Preprocessor>();
	private final List<Postprocessor> postprocessors = new ArrayList<Postprocessor>();
	private final List<Postprocessor> finallyProcessors = new ArrayList<Postprocessor>();
	private final List<MessageObserver> messageObservers = new ArrayList<MessageObserver>();
	private boolean shouldEnforceHttpSpec = true;

	// SECTION: CONSTRUCTORS

	public DefaultRequestHandler(final RouteResolver routeResolver, final SerializationProvider serializationProvider, final HttpResponseWriter responseWriter, final boolean enforceHttpSpec) {
		super();
		this.routeResolver = routeResolver;
		this.serializationProvider = serializationProvider;
		setResponseWriter(responseWriter);
		this.shouldEnforceHttpSpec = enforceHttpSpec;
	}

	// SECTION: MUTATORS

	public void addMessageObserver(final MessageObserver... observers) {
		for (final MessageObserver observer : observers) {
			if (!messageObservers.contains(observer)) {
				messageObservers.add(observer);
			}
		}
	}

	public HttpResponseWriter getResponseWriter() {
		return this.responseWriter;
	}

	public void setResponseWriter(final HttpResponseWriter writer) {
		this.responseWriter = writer;
	}

	// SECTION: SIMPLE-CHANNEL-UPSTREAM-HANDLER

	@Override
	public void messageReceived(final ChannelHandlerContext ctx, final MessageEvent event) throws Exception {
		final MessageContext context = createInitialContext(ctx, event);

		try {
			notifyReceived(context);
			resolveRoute(context);
			resolveResponseProcessor(context);
			invokePreprocessors(preprocessors, context);
			final Object result = context.getAction().invoke(context.getRequest(), context.getResponse());

			if (result != null) {
				context.getResponse().setBody(result);
			}

			invokePostprocessors(postprocessors, context);
			serializeResponse(context, false);
			enforceHttpSpecification(context);
			invokeFinallyProcessors(finallyProcessors, context);
			writeResponse(ctx, context);
			notifySuccess(context);
		} catch (final Throwable t) {
			handleRestExpressException(ctx, t);
		} finally {
			notifyComplete(context);
		}
	}

	private void resolveResponseProcessor(final MessageContext context) {
		final SerializationSettings s = serializationProvider.resolveResponse(context.getRequest(), context.getResponse(), false);
		context.setSerializationSettings(s);
	}

	/**
	 * @param context
	 */
	private void enforceHttpSpecification(final MessageContext context) {
		if (shouldEnforceHttpSpec) {
			HttpSpecification.enforce(context.getResponse());
		}
	}

	private void handleRestExpressException(final ChannelHandlerContext ctx, final Throwable cause) throws Exception {
		final MessageContext context = (MessageContext) ctx.getAttachment();
		Throwable rootCause = cause;
		if (HttpRuntimeException.class.isAssignableFrom(cause.getClass())) {
			final HttpRuntimeException httpRuntimeException = (HttpRuntimeException) cause;
			context.setHttpStatus(HttpResponseStatus.valueOf(httpRuntimeException.getHttpResponseStatus().getCode()));
		} else {
			rootCause = Exceptions.findRootCause(cause);
			context.setHttpStatus(HttpResponseStatus.INTERNAL_SERVER_ERROR);
		}

		context.setException(rootCause);
		notifyException(context);
		serializeResponse(context, true);
		invokeFinallyProcessors(finallyProcessors, context);
		writeResponse(ctx, context);
	}

	@Override
	public void exceptionCaught(final ChannelHandlerContext ctx, final ExceptionEvent event) throws Exception {
		try {
			final MessageContext messageContext = (MessageContext) ctx.getAttachment();

			if (messageContext != null) {
				messageContext.setException(event.getCause());
				notifyException(messageContext);
			}
		} catch (final Throwable t) {
			System.err.print("DefaultRequestHandler.exceptionCaught() threw an exception.");
			t.printStackTrace();
		} finally {
			event.getChannel().close();
		}
	}

	private MessageContext createInitialContext(final ChannelHandlerContext ctx, final MessageEvent event) {
		final Request request = createRequest(event, ctx);
		final Response response = createResponse();
		final MessageContext context = new MessageContext(request, response);
		ctx.setAttachment(context);
		return context;
	}

	private void resolveRoute(final MessageContext context) {
		final Action action = routeResolver.resolve(context);
		context.setAction(action);
	}

	/**
	 * @param request
	 * @param response
	 */
	private void notifyReceived(final MessageContext context) {
		for (final MessageObserver observer : messageObservers) {
			observer.onReceived(context.getRequest(), context.getResponse());
		}
	}

	/**
	 * @param request
	 * @param response
	 */
	private void notifyComplete(final MessageContext context) {
		for (final MessageObserver observer : messageObservers) {
			observer.onComplete(context.getRequest(), context.getResponse());
		}
	}

	// SECTION: UTILITY -- PRIVATE

	/**
	 * @param exception
	 * @param request
	 * @param response
	 */
	private void notifyException(final MessageContext context) {
		final Throwable exception = context.getException();

		for (final MessageObserver observer : messageObservers) {
			observer.onException(exception, context.getRequest(), context.getResponse());
		}
	}

	/**
	 * @param request
	 * @param response
	 */
	private void notifySuccess(final MessageContext context) {
		for (final MessageObserver observer : messageObservers) {
			observer.onSuccess(context.getRequest(), context.getResponse());
		}
	}

	public void addPreprocessor(final Preprocessor handler) {
		if (!preprocessors.contains(handler)) {
			preprocessors.add(handler);
		}
	}

	public void addPostprocessor(final Postprocessor handler) {
		if (!postprocessors.contains(handler)) {
			postprocessors.add(handler);
		}
	}

	public void addFinallyProcessor(final Postprocessor handler) {
		if (!finallyProcessors.contains(handler)) {
			finallyProcessors.add(handler);
		}
	}

	private void invokePreprocessors(final List<Preprocessor> processors, final MessageContext context) {
		for (final Preprocessor handler : processors) {
			handler.process(context);
		}
		context.getRequest().getBody().resetReaderIndex();
	}

	private void invokePostprocessors(final List<Postprocessor> processors, final MessageContext context) {
		for (final Postprocessor handler : processors) {
			handler.process(context);
		}
	}

	private void invokeFinallyProcessors(final List<Postprocessor> processors, final MessageContext context) {
		for (final Postprocessor handler : processors) {
			try {
				handler.process(context);
			} catch (final Throwable t) {
				t.printStackTrace(System.err);
			}
		}
	}

	/**
	 * @param request
	 * @return
	 */
	private Request createRequest(final MessageEvent event, final ChannelHandlerContext context) {
		return new Request(event, routeResolver, serializationProvider);
	}

	/**
	 * @param request
	 * @return
	 */
	private Response createResponse() {
		return new Response();
	}

	/**
	 * @param message
	 * @return
	 */
	private void writeResponse(final ChannelHandlerContext ctx, final MessageContext context) {
		getResponseWriter().write(ctx, context.getRequest(), context.getResponse());
	}

	private void serializeResponse(final MessageContext context, final boolean force) {
		final Response response = context.getResponse();

		if (HttpSpecification.isContentTypeAllowed(response)) {
			SerializationSettings settings = null;

			if (response.hasSerializationSettings()) {
				settings = response.getSerializationSettings();
			} else if (force) {
				settings = serializationProvider.resolveResponse(context.getRequest(), response, force);
			}

			if (settings != null) {
				if (response.isSerialized()) {
					final String serialized = settings.serialize(response);

					if (serialized != null) {
						response.setBody(serialized);

						if (!response.hasHeader(HttpHeaders.Names.CONTENT_TYPE)) {
							response.setContentType(settings.getMediaType());
						}
					}
				}
			}

			if (!response.hasHeader(HttpHeaders.Names.CONTENT_TYPE)) {
				response.setContentType(ContentType.TEXT_PLAIN);
			}
		}
	}
}
