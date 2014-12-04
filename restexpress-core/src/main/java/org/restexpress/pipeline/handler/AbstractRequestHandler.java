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

import java.util.ArrayList;
import java.util.List;

import org.intelligentsia.commons.http.exception.HttpRuntimeException;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;
import org.jboss.netty.handler.codec.http.HttpResponseStatus;
import org.restexpress.Request;
import org.restexpress.Response;
import org.restexpress.exception.Exceptions;
import org.restexpress.pipeline.HttpResponseWriter;
import org.restexpress.pipeline.MessageContext;
import org.restexpress.pipeline.MessageObserver;
import org.restexpress.pipeline.MessageObserverDispatcher;
import org.restexpress.pipeline.Postprocessor;
import org.restexpress.pipeline.Preprocessor;
import org.restexpress.util.HttpSpecification;

/**
 * {@link AbstractRequestHandler} extends {@link SimpleChannelUpstreamHandler}
 * and:
 * <ul>
 * <li>define main pipeline step</li>
 * <li>manage all pre/post/finally org.restexpress.serialization and their
 * invoker</li>
 * <li>manage {@link MessageObserver} and their notifier</li>
 * <li>implement base method like initial context creation, response writer,
 * HTTP specification enforcer</li>
 * </ul>
 * 
 * @author <a href="mailto:jguibert@intelligents-ia.com" >Jerome Guibert</a>
 * 
 */
public abstract class AbstractRequestHandler extends SimpleChannelUpstreamHandler {

	private final List<Preprocessor> preprocessors = new ArrayList<>();
	private final List<Postprocessor> postprocessors = new ArrayList<>();
	private final List<Postprocessor> finallyProcessors = new ArrayList<>();

	private final HttpResponseWriter responseWriter;
	private final boolean shouldEnforceHttpSpec;

	private MessageObserverDispatcher dispatcher;

	/**
	 * Build a new instance of {@link AbstractRequestHandler}.
	 * 
	 * @param responseWriter
	 */
	public AbstractRequestHandler(HttpResponseWriter responseWriter) {
		this(responseWriter, Boolean.TRUE);
	}

	/**
	 * Build a new instance of {@link AbstractRequestHandler}.
	 * 
	 * @param responseWriter
	 * @param shouldEnforceHttpSpec
	 */
	public AbstractRequestHandler(HttpResponseWriter responseWriter, boolean shouldEnforceHttpSpec) {
		this(responseWriter, shouldEnforceHttpSpec, new MessageObserverDispatcher());
	}

	/**
	 * Build a new instance of {@link AbstractRequestHandler}.
	 * 
	 * @param responseWriter
	 * @param shouldEnforceHttpSpec
	 * @param dispatcher
	 */
	public AbstractRequestHandler(HttpResponseWriter responseWriter, boolean shouldEnforceHttpSpec, MessageObserverDispatcher dispatcher) {
		super();
		this.responseWriter = responseWriter;
		this.shouldEnforceHttpSpec = shouldEnforceHttpSpec;
		this.dispatcher = dispatcher;
	}

	@Override
	public void messageReceived(final ChannelHandlerContext ctx, final MessageEvent event) throws Exception {
		final MessageContext context = createInitialContext(ctx, event);

		try {
			dispatcher.notifyReceived(context);
			resolveRoute(context);
			resolveResponseProcessor(context);
			invokePreprocessors(context);
			// invoke controller
			final Object result = context.getAction().invoke(context.getRequest(), context.getResponse());
			if (result != null) {
				context.getResponse().setBody(result);
			}
			invokePostprocessors(context);
			serializeResponse(context, false);
			enforceHttpSpecification(context);
			invokeFinallyProcessors(context);
			writeResponse(ctx, context);
			dispatcher.notifySuccess(context);
		} catch (final Throwable cause) {
			// handle exception
			Throwable rootCause = cause;
			if (HttpRuntimeException.class.isAssignableFrom(cause.getClass())) {
				final HttpRuntimeException httpRuntimeException = (HttpRuntimeException) cause;
				context.setHttpStatus(HttpResponseStatus.valueOf(httpRuntimeException.getHttpResponseStatus().getCode()));
			} else {
				rootCause = Exceptions.findRootCause(cause);
				context.setHttpStatus(HttpResponseStatus.INTERNAL_SERVER_ERROR);
			}

			context.setException(rootCause);
			dispatcher.notifyException(context);
			serializeResponse(context, true);
			invokeFinallyProcessors(context);
			writeResponse(ctx, context);
		} finally {
			dispatcher.notifyComplete(context);
		}
	}

	@Override
	public void exceptionCaught(final ChannelHandlerContext ctx, final ExceptionEvent event) throws Exception {
		try {
			final MessageContext messageContext = (MessageContext) ctx.getAttachment();
			if (messageContext != null) {
				messageContext.setException(event.getCause());
				dispatcher.notifyException(messageContext);
			}
		} catch (final Throwable t) {
			System.err.print("RequestHandler.exceptionCaught() threw an exception.");
			t.printStackTrace();
		} finally {
			event.getChannel().close();
		}
	}

	/**
	 * @return {@link MessageObserverDispatcher} instance.
	 */
	public MessageObserverDispatcher dispatcher() {
		return dispatcher;
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

	public List<Postprocessor> finallyProcessors() {
		return finallyProcessors;
	}

	public List<Postprocessor> postprocessors() {
		return postprocessors;
	}

	public List<Preprocessor> preprocessors() {
		return preprocessors;
	}

	protected void invokePreprocessors(final MessageContext context) {
		for (final Preprocessor handler : preprocessors) {
			handler.process(context);
		}
		if (context.getRequest().getBody() != null)
			context.getRequest().getBody().resetReaderIndex();
	}

	protected void invokePostprocessors(final MessageContext context) {
		for (final Postprocessor handler : postprocessors) {
			handler.process(context);
		}
	}

	protected void invokeFinallyProcessors(final MessageContext context) {
		for (final Postprocessor handler : finallyProcessors) {
			try {
				handler.process(context);
			} catch (final Throwable t) {
				t.printStackTrace(System.err);
			}
		}
	}

	protected MessageContext createInitialContext(final ChannelHandlerContext ctx, final MessageEvent event) {
		final Request request = createRequest(event, ctx);
		final Response response = createResponse();
		final MessageContext context = new MessageContext(request, response);
		ctx.setAttachment(context);
		return context;
	}

	/**
	 * Write {@link Response} in {@link ChannelHandlerContext}.
	 * 
	 * @param ctx
	 * @param context
	 */
	protected void writeResponse(final ChannelHandlerContext ctx, final MessageContext context) {
		responseWriter.write(ctx, context.getRequest(), context.getResponse());
	}

	/**
	 * Enforce HTTP specification if needed.
	 * 
	 * @param context
	 */
	protected void enforceHttpSpecification(final MessageContext context) {
		if (shouldEnforceHttpSpec) {
			HttpSpecification.enforce(context.getResponse());
		}
	}

	/**
	 * @return new {@link Response} instance.
	 */
	protected abstract Response createResponse();

	/**
	 * @param event
	 * @param ctx
	 * @return new {@link Request} instance.
	 */
	protected abstract Request createRequest(MessageEvent event, ChannelHandlerContext ctx);

	/**
	 * Serialize response object and wrap exception if necessary.
	 * 
	 * @param context
	 * @param force
	 *            true if response must be rendered even if requested format is
	 *            not supported.
	 */
	protected abstract void serializeResponse(final MessageContext context, final boolean force);

	/**
	 * Resolve Route.
	 * 
	 * @param context
	 */
	protected abstract void resolveRoute(MessageContext context);

	/**
	 * Resolve Response Processor.
	 * 
	 * @param context
	 */
	protected abstract void resolveResponseProcessor(final MessageContext context);

}
