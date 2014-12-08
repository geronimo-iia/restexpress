package org.restexpress.pipeline.handler;

import java.util.List;

import org.jboss.netty.channel.ChannelHandler;
import org.restexpress.pipeline.HttpResponseWriter;
import org.restexpress.pipeline.MessageObserverDispatcher;
import org.restexpress.pipeline.Postprocessor;
import org.restexpress.pipeline.Preprocessor;
import org.restexpress.response.ResponseProcessorManager;
import org.restexpress.route.RouteResolver;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;

/**
 * {@link ChannelHandlerBuilder} implements a builder for {@link DefaultRequestHandler}.
 * 
 * @author <a href="mailto:jguibert@intelligents-ia.com" >Jerome Guibert</a>
 */
public final class ChannelHandlerBuilder {

	private List<Preprocessor> preprocessors = Lists.newArrayList();
	private List<Postprocessor> postprocessors = Lists.newArrayList();
	private List<Postprocessor> finallyProcessors = Lists.newArrayList();

	private HttpResponseWriter responseWriter;
	private boolean shouldEnforceHttpSpec = Boolean.TRUE;

	private MessageObserverDispatcher dispatcher;

	private RouteResolver routeResolver;

	private ResponseProcessorManager responseProcessorManager;

	public ChannelHandlerBuilder() {
		super();
	}

	public ChannelHandler build() {
		return new DefaultRequestHandler(//
				preprocessors.toArray(new Preprocessor[preprocessors.size()]), //
				postprocessors.toArray(new Postprocessor[postprocessors.size()]),//
				finallyProcessors.toArray(new Postprocessor[finallyProcessors.size()]),//
				responseWriter, //
				shouldEnforceHttpSpec, //
				dispatcher, //
				routeResolver, //
				responseProcessorManager);
	}

	public ChannelHandlerBuilder addPreprocessor(final Preprocessor preprocessor) {
		if (!preprocessors.contains(preprocessor)) {
			preprocessors.add(preprocessor);
		}
		return this;
	}

	public ChannelHandlerBuilder addPostprocessor(final Postprocessor processor) {
		if (!postprocessors.contains(processor)) {
			postprocessors.add(processor);
		}
		return this;
	}

	public ChannelHandlerBuilder addFinallyProcessor(final Postprocessor processor) {
		if (!finallyProcessors.contains(processor)) {
			finallyProcessors.add(processor);
		}
		return this;
	}

	public ChannelHandlerBuilder setPreprocessors(List<Preprocessor> preprocessors) {
		this.preprocessors = Preconditions.checkNotNull(preprocessors);
		return this;
	}

	public ChannelHandlerBuilder setPostprocessors(List<Postprocessor> postprocessors) {
		this.postprocessors = Preconditions.checkNotNull(postprocessors);
		return this;
	}

	public ChannelHandlerBuilder setFinallyProcessors(List<Postprocessor> finallyProcessors) {
		this.finallyProcessors = Preconditions.checkNotNull(finallyProcessors);
		return this;
	}

	public ChannelHandlerBuilder setResponseWriter(HttpResponseWriter responseWriter) {
		this.responseWriter = responseWriter;
		return this;
	}

	public ChannelHandlerBuilder setShouldEnforceHttpSpec(boolean shouldEnforceHttpSpec) {
		this.shouldEnforceHttpSpec = shouldEnforceHttpSpec;
		return this;
	}

	public ChannelHandlerBuilder setDispatcher(MessageObserverDispatcher dispatcher) {
		this.dispatcher = dispatcher;
		return this;
	}

	public ChannelHandlerBuilder setRouteResolver(RouteResolver routeResolver) {
		this.routeResolver = routeResolver;
		return this;
	}

	public ChannelHandlerBuilder setResponseProcessorManager(ResponseProcessorManager responseProcessorManager) {
		this.responseProcessorManager = responseProcessorManager;
		return this;
	}

}
