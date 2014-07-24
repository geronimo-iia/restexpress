package org.restexpress.pipeline;

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
import org.restexpress.util.HttpSpecification;

/**
 * {@link AbstractRequestHandler} extends {@link SimpleChannelUpstreamHandler}
 * and:
 * <ul>
 * <li>define main pipeline step</li>
 * <li>manage all pre/post/finally org.restexpress.processor and their invoker</li>
 * <li>manage {@link MessageObserver} and their notifier</li>
 * <li>implement base method like initial context creation, response writer,
 * HTTP specification enforcer</li>
 * </ul>
 * 
 * @author <a href="mailto:jguibert@intelligents-ia.com" >Jerome Guibert</a>
 * 
 */
public abstract class AbstractRequestHandler extends SimpleChannelUpstreamHandler {

	private final List<Preprocessor> preprocessors = new ArrayList<Preprocessor>();
	private final List<Postprocessor> postprocessors = new ArrayList<Postprocessor>();
	private final List<Postprocessor> finallyProcessors = new ArrayList<Postprocessor>();
	private final List<MessageObserver> messageObservers = new ArrayList<MessageObserver>();
	private final HttpResponseWriter responseWriter;
	private final boolean shouldEnforceHttpSpec;

	public AbstractRequestHandler(HttpResponseWriter responseWriter) {
		this(responseWriter, Boolean.TRUE);
	}

	public AbstractRequestHandler(HttpResponseWriter responseWriter, boolean shouldEnforceHttpSpec) {
		super();
		this.responseWriter = responseWriter;
		this.shouldEnforceHttpSpec = shouldEnforceHttpSpec;
	}

	@Override
	public void messageReceived(final ChannelHandlerContext ctx, final MessageEvent event) throws Exception {
		final MessageContext context = createInitialContext(ctx, event);

		try {
			notifyReceived(context);
			resolveRoute(context);
			resolveResponseProcessor(context);
			invokePreprocessors(preprocessors(), context);
			final Object result = context.getAction().invoke(context.getRequest(), context.getResponse());

			if (result != null) {
				context.getResponse().setBody(result);
			}

			invokePostprocessors(postprocessors(), context);
			serializeResponse(context, false);
			enforceHttpSpecification(context);

			invokeFinallyProcessors(finallyProcessors(), context);
			writeResponse(ctx, context);
			notifySuccess(context);
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
			notifyException(context);
			serializeResponse(context, true);
			invokeFinallyProcessors(finallyProcessors(), context);
			writeResponse(ctx, context);
		} finally {
			notifyComplete(context);
		}
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
			System.err.print("RequestHandler.exceptionCaught() threw an exception.");
			t.printStackTrace();
		} finally {
			event.getChannel().close();
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

	public List<Postprocessor> finallyProcessors() {
		return finallyProcessors;
	}

	public List<Postprocessor> postprocessors() {
		return postprocessors;
	}

	public List<Preprocessor> preprocessors() {
		return preprocessors;
	}

	protected void invokePreprocessors(final List<Preprocessor> processors, final MessageContext context) {
		for (final Preprocessor handler : processors) {
			handler.process(context);
		}
		context.getRequest().getBody().resetReaderIndex();
	}

	protected void invokePostprocessors(final List<Postprocessor> processors, final MessageContext context) {
		for (final Postprocessor handler : processors) {
			handler.process(context);
		}
	}

	protected void invokeFinallyProcessors(final List<Postprocessor> processors, final MessageContext context) {
		for (final Postprocessor handler : processors) {
			try {
				handler.process(context);
			} catch (final Throwable t) {
				t.printStackTrace(System.err);
			}
		}
	}

	public void addMessageObserver(final MessageObserver... observers) {
		for (final MessageObserver observer : observers) {
			if (!messageObservers.contains(observer)) {
				messageObservers.add(observer);
			}
		}
	}

	public void addMessageObserver(final List<MessageObserver> observers) {
		for (final MessageObserver observer : observers) {
			if (!messageObservers.contains(observer)) {
				messageObservers.add(observer);
			}
		}
	}

	public List<MessageObserver> messageObservers() {
		return messageObservers;
	}

	protected void notifyReceived(MessageContext context) {
		for (final MessageObserver observer : messageObservers) {
			observer.onReceived(context.getRequest(), context.getResponse());
		}
	}

	protected void notifyException(final MessageContext context) {
		final Throwable exception = context.getException();
		for (final MessageObserver observer : messageObservers) {
			observer.onException(exception, context.getRequest(), context.getResponse());
		}
	}

	protected void notifyComplete(final MessageContext context) {
		for (final MessageObserver observer : messageObservers) {
			observer.onComplete(context.getRequest(), context.getResponse());
		}
	}

	protected void notifySuccess(final MessageContext context) {
		for (final MessageObserver observer : messageObservers) {
			observer.onSuccess(context.getRequest(), context.getResponse());
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
	 * Serialize response object.
	 * 
	 * @param context
	 * @param force
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
