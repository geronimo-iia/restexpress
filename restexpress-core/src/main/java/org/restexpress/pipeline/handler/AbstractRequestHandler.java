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

import java.util.List;

import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;
import org.restexpress.Exceptions;
import org.restexpress.HttpSpecification;
import org.restexpress.Request;
import org.restexpress.Response;
import org.restexpress.http.HttpRuntimeException;
import org.restexpress.http.HttpStatus;
import org.restexpress.pipeline.HttpResponseWriter;
import org.restexpress.pipeline.MessageContext;
import org.restexpress.pipeline.MessageObserver;
import org.restexpress.pipeline.Postprocessor;
import org.restexpress.pipeline.Preprocessor;
import org.restexpress.plugin.Plugin;

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

	// avoid premature optimization (array usage)
	private final List<Preprocessor> preprocessors;
	private final List<Postprocessor> postprocessors;
	private final List<Postprocessor> finallyProcessors;

	private final HttpResponseWriter responseWriter;

	private final boolean shouldEnforceHttpSpec;

	private final MessageObserver observer;

	/**
	 * Build a new instance of {@link AbstractRequestHandler}.
	 * 
	 * @param preprocessors
	 * @param postprocessors
	 * @param finallyProcessors
	 * @param responseWriter
	 * @param shouldEnforceHttpSpec
	 * @param observer
	 */
	protected AbstractRequestHandler(List<Preprocessor> preprocessors, List<Postprocessor> postprocessors, List<Postprocessor> finallyProcessors, HttpResponseWriter responseWriter, boolean shouldEnforceHttpSpec, MessageObserver observer) {
		super();
		this.preprocessors = Preconditions.checkNotNull(preprocessors);
		this.postprocessors = Preconditions.checkNotNull(postprocessors);
		this.finallyProcessors = Preconditions.checkNotNull(finallyProcessors);
		this.responseWriter = Preconditions.checkNotNull(responseWriter);
		this.shouldEnforceHttpSpec = shouldEnforceHttpSpec;
		this.observer = observer;
	}

	@Override
	public final void messageReceived(final ChannelHandlerContext ctx, final MessageEvent event) throws Exception {
		final MessageContext context = createInitialContext(ctx, event);

		try {
			notifyReceived(context);
			resolveRoute(context);
			resolveResponseProcessor(context);
			invokePreprocessors(context);
			// invoke controller
			final Object result = context.getAction().invoke(context);
			if (result != null) {
				context.getResponse().setEntity(result);
			}
			invokePostprocessors(context);
			handleResponseContent(context, false);
			enforceHttpSpecification(context);
			invokeFinallyProcessors(context);
			writeResponse(ctx, context);
			notifySuccess(context);
		} catch (final Throwable cause) {
			// handle exception
			Throwable rootCause = cause;
			if (HttpRuntimeException.class.isAssignableFrom(cause.getClass())) {
				final HttpRuntimeException httpRuntimeException = (HttpRuntimeException) cause;
				context.setStatusInfo(httpRuntimeException.getHttpResponseStatus());
			} else {
				rootCause = Exceptions.findRootCause(cause);
				context.setStatusInfo(HttpStatus.INTERNAL_SERVER_ERROR);
			}
			context.setException(rootCause);
			notifyException(context);
			handleResponseContent(context, true);
			invokeFinallyProcessors(context);
			writeResponse(ctx, context);
		} finally {
			notifyComplete(context);
		}
	}

	@Override
	public final void exceptionCaught(final ChannelHandlerContext ctx, final ExceptionEvent event) throws Exception {
		try {
			final MessageContext messageContext = (MessageContext) ctx.getAttachment();
			if (messageContext != null) {
				messageContext.setException(event.getCause());
				notifyException(messageContext);
			}
		} catch (final Throwable t) {
			System.err.print("RequestHandler.exceptionCaught() threw an exception.");
			t.printStackTrace();
		} finally {
			event.getChannel().close();
		}
	}

	/**
	 * Used for runtime information.
	 * 
	 * @return {@link MessageObserver} instance.
	 */
	public final MessageObserver messageObserver() {
		return observer;
	}

	/**
	 * Used for runtime information.
	 * 
	 * @return a {@link List} of {@link Preprocessor}.
	 */
	public final List<Preprocessor> preprocessors() {
		return preprocessors;
	}

	/**
	 * Used for runtime information.
	 * 
	 * @return a {@link List} of {@link Postprocessor}.
	 */
	public final List<Postprocessor> postprocessors() {
		return postprocessors;
	}

	/**
	 * Used for runtime information.
	 * 
	 * @return a {@link List} finally processor.
	 */
	public final List<Postprocessor> finallyProcessors() {
		return finallyProcessors;
	}

	/**
	 * Used for runtime information.
	 * 
	 * @return {@link HttpResponseWriter}.
	 */
	public final HttpResponseWriter httpResponseWriter() {
		return responseWriter;
	}

	/**
	 * Add specified {@link Preprocessor}. This method is useful to add
	 * customization from {@link Plugin}.
	 * 
	 * @param preprocessor
	 *            {@link Preprocessor}
	 */
	public final void addPreprocessor(final Preprocessor preprocessor) {
		if (!preprocessors.contains(preprocessor)) {
			preprocessors.add(preprocessor);
		}
	}

	/**
	 * Add specified {@link Postprocessor}. This method is useful to add
	 * customization from {@link Plugin}.
	 * 
	 * 
	 * @param processor
	 *            {@link Postprocessor}
	 */
	public final void addPostprocessor(final Postprocessor processor) {
		if (!postprocessors.contains(processor)) {
			postprocessors.add(processor);
		}
	}

	/**
	 * Add specified finally {@link Postprocessor}. This method is useful to add
	 * customization from {@link Plugin}.
	 * 
	 * @param processor
	 *            {@link Postprocessor}
	 */
	public final void addFinallyProcessor(final Postprocessor processor) {
		if (!finallyProcessors.contains(processor)) {
			finallyProcessors.add(processor);
		}
	}

	/**
	 * Invoke all {@link Preprocessor}.
	 * 
	 * @param context
	 *            {@link MessageContext}
	 */
	protected final void invokePreprocessors(final MessageContext context) {
		for (Preprocessor preprocessor : preprocessors) {
			preprocessor.process(context);
		}
		if (context.getRequest().getEntity() != null)
			context.getRequest().getEntity().resetReaderIndex();
	}

	/**
	 * Invoke all {@link Postprocessor}.
	 * 
	 * @param context
	 *            {@link MessageContext}.
	 */
	protected final void invokePostprocessors(final MessageContext context) {
		for (Postprocessor postprocessor : postprocessors) {
			postprocessor.process(context);
		}
	}

	/**
	 * Invoke all finally {@link Postprocessor}.
	 * 
	 * @param context
	 *            {@link MessageContext}
	 */
	protected final void invokeFinallyProcessors(final MessageContext context) {
		for (Postprocessor finallyProcessor : finallyProcessors) {
			try {
				finallyProcessor.process(context);
			} catch (final Throwable t) {
				t.printStackTrace(System.err);
			}
		}
	}

	/**
	 * Create {@link MessageContext}
	 * 
	 * @param ctx
	 *            {@link ChannelHandlerContext}
	 * @param event
	 *            {@link MessageEvent}
	 * @return {@link MessageContext} instance.
	 */
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
	 * Notify observer.
	 * 
	 * @param context
	 *            {@link MessageContext} instance for this call.
	 */
	protected final void notifyReceived(MessageContext context) {
		if (observer != null)
			observer.onReceived(context.getRequest(), context.getResponse());
	}

	/**
	 * Notify observer.
	 * 
	 * @param context
	 *            {@link MessageContext} instance for this call.
	 */
	protected final void notifyException(final MessageContext context) {
		if (observer != null) {
			final Throwable exception = context.getException();
			observer.onException(exception, context.getRequest(), context.getResponse());
		}
	}

	/**
	 * Notify observer.
	 * 
	 * @param context
	 *            {@link MessageContext} instance for this call.
	 */
	protected final void notifyComplete(final MessageContext context) {
		if (observer != null)
			observer.onComplete(context.getRequest(), context.getResponse());

	}

	/**
	 * Notify observer.
	 * 
	 * @param context
	 *            {@link MessageContext} instance for this call.
	 */
	protected final void notifySuccess(final MessageContext context) {
		if (observer != null)
			observer.onSuccess(context.getRequest(), context.getResponse());
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
