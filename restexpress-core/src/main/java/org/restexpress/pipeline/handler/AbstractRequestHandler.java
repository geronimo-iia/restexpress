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

import com.google.common.base.Preconditions;

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

	private final Preprocessor[] preprocessors;
	private final int preprocessorSize;
	private final Postprocessor[] postprocessors;
	private final int postprocessorSize;
	private final Postprocessor[] finallyProcessors;
	private final int finallyProcessorSize;

	private final HttpResponseWriter responseWriter;
	private final boolean shouldEnforceHttpSpec;

	private MessageObserverDispatcher dispatcher;

	public AbstractRequestHandler(Preprocessor[] preprocessors, Postprocessor[] postprocessors, Postprocessor[] finallyProcessors, HttpResponseWriter responseWriter, boolean shouldEnforceHttpSpec, MessageObserverDispatcher dispatcher) {
		super();
		this.preprocessors = Preconditions.checkNotNull(preprocessors);
		preprocessorSize = preprocessors.length;
		this.postprocessors = Preconditions.checkNotNull(postprocessors);
		postprocessorSize = postprocessors.length;
		this.finallyProcessors = Preconditions.checkNotNull(finallyProcessors);
		finallyProcessorSize = finallyProcessors.length;
		this.responseWriter = Preconditions.checkNotNull(responseWriter);
		this.shouldEnforceHttpSpec = shouldEnforceHttpSpec;
		this.dispatcher = Preconditions.checkNotNull(dispatcher);
	}

	@Override
	public final void messageReceived(final ChannelHandlerContext ctx, final MessageEvent event) throws Exception {
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
			handleResponseContent(context, false);
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
			handleResponseContent(context, true);
			invokeFinallyProcessors(context);
			writeResponse(ctx, context);
		} finally {
			dispatcher.notifyComplete(context);
		}
	}

	@Override
	public final void exceptionCaught(final ChannelHandlerContext ctx, final ExceptionEvent event) throws Exception {
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
	public final MessageObserverDispatcher dispatcher() {
		return dispatcher;
	}

	/**
	 * @return an array of {@link Preprocessor}.
	 */
	public Preprocessor[] preprocessors() {
		return preprocessors;
	}

	/**
	 * @return an array of {@link Postprocessor}.
	 */
	public Postprocessor[] postprocessors() {
		return postprocessors;
	}

	/**
	 * @return an array of finally processor.
	 */
	public Postprocessor[] finallyProcessors() {
		return finallyProcessors;
	}

	public HttpResponseWriter responseWriter() {
		return responseWriter;
	}

	protected final void invokePreprocessors(final MessageContext context) {
		for (int i = 0; i < preprocessorSize; i++) {
			preprocessors[i].process(context);
		}
		if (context.getRequest().getBody() != null)
			context.getRequest().getBody().resetReaderIndex();
	}

	protected final void invokePostprocessors(final MessageContext context) {
		for (int i = 0; i < postprocessorSize; i++) {
			postprocessors[i].process(context);
		}
	}

	protected final void invokeFinallyProcessors(final MessageContext context) {
		for (int i = 0; i < finallyProcessorSize; i++) {
			try {
				finallyProcessors[i].process(context);
			} catch (final Throwable t) {
				t.printStackTrace(System.err);
			}
		}
	}

	protected final MessageContext createInitialContext(final ChannelHandlerContext ctx, final MessageEvent event) {
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
	protected final void writeResponse(final ChannelHandlerContext ctx, final MessageContext context) {
		responseWriter.write(ctx, context.getRequest(), context.getResponse());
	}

	/**
	 * Enforce HTTP specification if needed.
	 * 
	 * @param context
	 */
	protected final void enforceHttpSpecification(final MessageContext context) {
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
	 * Handle response content:
	 * <ul>
	 * <li>serialize response object and wrap exception if necessary</li>
	 * <li>doing some stuff with default header</li>
	 * </ul>
	 * 
	 * @param context
	 * @param force
	 *            true if response must be rendered even if requested format is
	 *            not supported.
	 */
	protected abstract void handleResponseContent(final MessageContext context, final boolean force);

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
