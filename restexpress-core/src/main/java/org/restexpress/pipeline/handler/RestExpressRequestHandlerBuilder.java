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

import java.util.Collections;
import java.util.List;

import org.restexpress.observer.MessageObserverDispatcher;
import org.restexpress.pipeline.HttpResponseWriter;
import org.restexpress.pipeline.MessageObserver;
import org.restexpress.pipeline.Postprocessor;
import org.restexpress.pipeline.Preprocessor;
import org.restexpress.pipeline.writer.DefaultHttpResponseWriter;
import org.restexpress.response.ResponseProcessorManager;
import org.restexpress.response.ResponseWrapper;
import org.restexpress.route.RouteResolver;
import org.restexpress.serialization.Processor;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.Lists;

/**
 * {@link RestExpressRequestHandlerBuilder} implements a builder for
 * {@link RestExpressRequestHandler}.
 * 
 * @author <a href="mailto:jguibert@intelligents-ia.com" >Jerome Guibert</a>
 */
public final class RestExpressRequestHandlerBuilder {

	private List<Preprocessor> preprocessors = Lists.newArrayList();
	private List<Postprocessor> postprocessors = Lists.newArrayList();
	private List<Postprocessor> finallyProcessors = Lists.newArrayList();

	private HttpResponseWriter httpResponseWriter;

	private boolean shouldEnforceHttpSpec = Boolean.TRUE;

	private List<MessageObserver> messageObservers = Lists.newArrayList();

	private RouteResolver routeResolver;

	private ResponseProcessorManager responseProcessorManager;

	private RestExpressRequestHandlerBuilder() {
		super();
	}

	/**
	 * @return {@link RestExpressRequestHandlerBuilder} with default
	 *         {@link HttpResponseWriter} and {@link ResponseProcessorManager}.
	 */
	public static RestExpressRequestHandlerBuilder newBuilder() {
		return newBuilder(new DefaultHttpResponseWriter());
	}

	/**
	 * @param httpResponseWriter
	 * @return {@link RestExpressRequestHandlerBuilder} with default
	 *         {@link ResponseProcessorManager}.
	 */
	public static RestExpressRequestHandlerBuilder newBuilder(HttpResponseWriter httpResponseWriter) {
		return newBuilder(httpResponseWriter, new ResponseProcessorManager());
	}

	/**
	 * @param httpResponseWriter
	 * @param responseProcessorManager
	 * @return an {@link RestExpressRequestHandlerBuilder}
	 */
	public static RestExpressRequestHandlerBuilder newBuilder(HttpResponseWriter httpResponseWriter, ResponseProcessorManager responseProcessorManager) {
		return new RestExpressRequestHandlerBuilder().setHttpResponseWriter(httpResponseWriter).setResponseProcessorManager(responseProcessorManager);
	}

	/**
	 * @return a builder {@link RestExpressRequestHandler}.
	 */
	public RestExpressRequestHandler build() {
		// choose right observers
		MessageObserver observer = null;
		if (!messageObservers.isEmpty()) {
			observer = messageObservers.size() > 1 ? new MessageObserverDispatcher(messageObservers) : messageObservers.get(0);
		}

		return new RestExpressRequestHandler(//
				preprocessors, //
				postprocessors,//
				finallyProcessors,//
				httpResponseWriter, //
				shouldEnforceHttpSpec, //
				observer, //
				routeResolver, //
				responseProcessorManager);
	}

	@VisibleForTesting
	public List<Postprocessor> finallyProcessors() {
		return Collections.unmodifiableList(finallyProcessors);
	}

	@VisibleForTesting
	public List<Postprocessor> postprocessors() {
		return Collections.unmodifiableList(postprocessors);
	}

	@VisibleForTesting
	public List<Preprocessor> preprocessors() {
		return Collections.unmodifiableList(preprocessors);
	}

	@VisibleForTesting
	public List<MessageObserver> messageObservers() {
		return Collections.unmodifiableList(messageObservers);
	}

	/**
	 * Add the specified {@link Processor} and {@link ResponseWrapper}.
	 * 
	 * @param processor
	 * @param responseWrapper
	 */
	public RestExpressRequestHandlerBuilder add(Processor processor, ResponseWrapper responseWrapper) {
		responseProcessorManager.add(processor, responseWrapper);
		return this;
	}

	/**
	 * Add the specified {@link Processor} and {@link ResponseWrapper}.
	 * 
	 * @param processor
	 * @param responseWrapper
	 * @param isDefault
	 */
	public RestExpressRequestHandlerBuilder add(Processor processor, ResponseWrapper responseWrapper, boolean isDefault) {
		responseProcessorManager.add(processor, responseWrapper, isDefault);
		return this;
	}

	public RestExpressRequestHandlerBuilder addMessageObserver(MessageObserver observer) {
		if (!messageObservers.contains(observer)) {
			messageObservers.add(observer);
		}
		return this;
	}

	public RestExpressRequestHandlerBuilder addPreprocessor(final Preprocessor preprocessor) {
		if (!preprocessors.contains(preprocessor)) {
			preprocessors.add(preprocessor);
		}
		return this;
	}

	public RestExpressRequestHandlerBuilder addPostprocessor(final Postprocessor processor) {
		if (!postprocessors.contains(processor)) {
			postprocessors.add(processor);
		}
		return this;
	}

	public RestExpressRequestHandlerBuilder addFinallyProcessor(final Postprocessor processor) {
		if (!finallyProcessors.contains(processor)) {
			finallyProcessors.add(processor);
		}
		return this;
	}

	public RestExpressRequestHandlerBuilder setHttpResponseWriter(HttpResponseWriter httpResponseWriter) {
		this.httpResponseWriter = httpResponseWriter;
		return this;
	}

	public RestExpressRequestHandlerBuilder setShouldEnforceHttpSpec(boolean shouldEnforceHttpSpec) {
		this.shouldEnforceHttpSpec = shouldEnforceHttpSpec;
		return this;
	}

	public RestExpressRequestHandlerBuilder shouldEnforceHttpSpec() {
		this.shouldEnforceHttpSpec = Boolean.TRUE;
		return this;
	}

	public RestExpressRequestHandlerBuilder shouldNotEnforceHttpSpec() {
		this.shouldEnforceHttpSpec = Boolean.FALSE;
		return this;
	}

	public RestExpressRequestHandlerBuilder setRouteResolver(RouteResolver routeResolver) {
		this.routeResolver = routeResolver;
		return this;
	}

	public RestExpressRequestHandlerBuilder setResponseProcessorManager(ResponseProcessorManager responseProcessorManager) {
		this.responseProcessorManager = responseProcessorManager;
		return this;
	}

}
