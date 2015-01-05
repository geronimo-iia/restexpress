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
package org.restexpress;

import java.util.Map;

import javax.ws.rs.ext.ParamConverterProvider;

import org.restexpress.context.ServerContext;
import org.restexpress.domain.metadata.ServerMetadata;
import org.restexpress.exception.ConfigurationException;
import org.restexpress.pipeline.MessageObserver;
import org.restexpress.pipeline.Postprocessor;
import org.restexpress.pipeline.Preprocessor;
import org.restexpress.plugin.Plugin;
import org.restexpress.plugin.PluginService;
import org.restexpress.response.ResponseProcessorManager;
import org.restexpress.response.ResponseWrapper;
import org.restexpress.route.RouteMapping;
import org.restexpress.route.parameterized.ParameterizedRouteBuilder;
import org.restexpress.route.regex.RegexRouteBuilder;
import org.restexpress.serialization.Processor;
import org.restexpress.settings.RestExpressSettings;

/**
 * {@link RestExpress} declare methods to manage configuration of an instance.
 * 
 * @see RestExpressService.
 * 
 * 
 * @author <a href="mailto:jguibert@intelligents-ia.com" >Jerome Guibert</a>
 */
public interface RestExpress {

	/**
	 * Adds a {@link MessageObserver} if not ever added.
	 * 
	 * @param observer
	 * @return {@link RestExpress} instance.
	 */
	public RestExpress addMessageObserver(MessageObserver observer);

	/**
	 * Add a {@link Preprocessor} instance that gets called before an incoming
	 * message gets processed. Preprocessors get called in the order in which
	 * they are added. To break out of the chain, simply throw an exception.
	 * 
	 * @param processor
	 * @return {@link RestExpress} instance.
	 */
	public RestExpress addPreprocessor(Preprocessor preprocessor);

	/**
	 * Add a {@link Postprocessor} instance that gets called after an incoming
	 * message is processed. A Postprocessor is useful for augmenting or
	 * transforming the results of a controller or adding headers, etc.
	 * Postprocessors get called in the order in which they are added. Note
	 * however, they do NOT get called in the case of an exception or error
	 * within the route.
	 * 
	 * @param processor
	 * @return {@link RestExpress} instance.
	 */
	public RestExpress addPostprocessor(Postprocessor processor);

	/**
	 * Add a {@link Postprocessor} instance that gets called right before the
	 * serialized message is sent to the client, or in a finally block after the
	 * message is processed, if an error occurs. Finally preprocessors are
	 * {@link Postprocessor} instances that are guaranteed to run even if an
	 * error is thrown from the controller or somewhere else in the route. A
	 * Finally Processor is useful for adding headers or transforming results
	 * even during error conditions. Finally preprocessors get called in the
	 * order in which they are added.
	 * 
	 * If an exception is thrown during finally org.restexpress.serialization
	 * execution, the finally preprocessors following it are executed after
	 * printing a stack trace to the System.err stream.
	 * 
	 * @param processor
	 * @return RestExpress for method chaining.
	 */
	public RestExpress addFinallyProcessor(Postprocessor processor);

	/**
	 * Add the specified {@link Processor} and {@link ResponseWrapper}.
	 * 
	 * @param processor
	 *            {@link Processor} instance
	 * @param responseWrapper
	 *            associated {@link ResponseWrapper}
	 * @return {@link RestExpress} instance.
	 */
	public RestExpress add(Processor processor, ResponseWrapper responseWrapper);

	/**
	 * Add the specified {@link Processor} and {@link ResponseWrapper}.
	 * 
	 * @param processor
	 *            {@link Processor} instance
	 * @param responseWrapper
	 *            associated {@link ResponseWrapper}
	 * @param isDefault
	 *            if true, this configuration will be used as default
	 * @return this {@link RestExpress} instance.
	 */
	public RestExpress add(Processor processor, ResponseWrapper responseWrapper, boolean isDefault);

	/**
	 * Verifies the response contents prior to writing it to the output stream
	 * to ensure that it conforms to the HTTP 1.1. specification.
	 * 
	 * @return this {@link ResponseProcessorManager} instance.
	 */
	public RestExpress shouldEnforceHttpSpec();

	/**
	 * Do not ensure that response content conforms to the HTTP 1.1.
	 * specification (default setting).
	 * 
	 * @return this {@link ResponseProcessorManager} instance.
	 */
	public RestExpress shouldNotEnforceHttpSpec();

	/**
	 * Register specified {@link Plugin}. If the specified {@link Plugin} is
	 * ever registered, {@link #register(Plugin)} will do nothing.
	 * 
	 * 
	 * @param plugin
	 *            {@link Plugin} instance to register.
	 * @return this {@link RestExpress} instance.
	 */
	public RestExpress register(Plugin plugin);

	/**
	 * @return {@link PluginService} instance.
	 */
	public PluginService plugin();

	/**
	 * @return {@link RestExpressSettings} instance.
	 */
	public RestExpressSettings settings();

	/**
	 * @return {@link ServerContext} instance.
	 */
	public ServerContext context();

	/**
	 * Create a route.
	 * 
	 * @param uriPattern
	 * @param controller
	 * @return {@link ParameterizedRouteBuilder}.
	 */
	public ParameterizedRouteBuilder uri(String uriPattern, Object controller);

	/**
	 * Create a route.
	 * 
	 * @param uriPattern
	 * @param controller
	 * @return {@link RegexRouteBuilder}.
	 */
	public RegexRouteBuilder regex(String uriPattern, Object controller);

	/**
	 * Retrieve meta data about the routes in this RestExpress server.
	 * 
	 * @return ServerMetadata instance.
	 * @throws IllegalStateException
	 *             if server is not bind.
	 */
	public ServerMetadata getRouteMetadata();

	/**
	 * Retrieve the named routes in this RestExpress server, creating a Map of
	 * them by name, with the value portion being populated with the URL
	 * pattern. Any '.{format}' portion of the URL pattern is omitted.
	 * <p/>
	 * If the Base URL is set, it is included in the URL pattern.
	 * <p/>
	 * Only named routes are included in the output.
	 *
	 * @return a Map of Route Name/URL pairs.
	 */
	public Map<String, String> getRouteUrlsByName();

	/**
	 * Register a controller instance by following JAX RS annotation definition,
	 * and create necessary route declaration.
	 * 
	 * @param controller
	 *            the controller to register.
	 * @throws ConfigurationException
	 *             if this controller cannot be mapped
	 * @return this {@link RestExpress} instance.
	 */
	public RestExpress route(Object controller) throws ConfigurationException;

	/**
	 * @return {@link RouteMapping} instance used to manage toute.
	 */
	public RouteMapping routeMapping();

	/**
	 * @return {@link ParamConverterProvider} used with JaxRs.
	 */
	public ParamConverterProvider paramConverterProvider();

}
