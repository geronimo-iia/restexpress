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
package org.restexpress.route;

import static org.jboss.netty.handler.codec.http.HttpMethod.DELETE;
import static org.jboss.netty.handler.codec.http.HttpMethod.GET;
import static org.jboss.netty.handler.codec.http.HttpMethod.HEAD;
import static org.jboss.netty.handler.codec.http.HttpMethod.OPTIONS;
import static org.jboss.netty.handler.codec.http.HttpMethod.POST;
import static org.jboss.netty.handler.codec.http.HttpMethod.PUT;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jboss.netty.handler.codec.http.HttpMethod;
import org.restexpress.Request;
import org.restexpress.Response;
import org.restexpress.domain.metadata.RouteMetadata;
import org.restexpress.domain.metadata.UriMetadata;
import org.restexpress.exception.ConfigurationException;

/**
 * Builds a route for a single URI. If a URI is given with no methods or
 * actions, the builder creates routes for the GET, POST, PUT, and DELETE HTTP
 * methods for the given URI.
 * 
 * @author toddf
 * @since 2010
 */
public abstract class RouteBuilder {
	// SECTION: CONSTANTS

	static final String DELETE_ACTION_NAME = "delete";
	static final String GET_ACTION_NAME = "read";
	static final String POST_ACTION_NAME = "create";
	static final String PUT_ACTION_NAME = "update";
	static final String HEAD_ACTION_NAME = "headers";
	static final String OPTION_ACTION_NAME = "options";
	static final List<HttpMethod> DEFAULT_HTTP_METHODS = Arrays.asList(new HttpMethod[] { GET, POST, PUT, DELETE });
	static final Map<HttpMethod, String> ACTION_MAPPING = new HashMap<HttpMethod, String>();

	static {
		ACTION_MAPPING.put(DELETE, DELETE_ACTION_NAME);
		ACTION_MAPPING.put(GET, GET_ACTION_NAME);
		ACTION_MAPPING.put(POST, POST_ACTION_NAME);
		ACTION_MAPPING.put(PUT, PUT_ACTION_NAME);
		ACTION_MAPPING.put(HEAD, HEAD_ACTION_NAME);
		ACTION_MAPPING.put(OPTIONS, OPTION_ACTION_NAME);
	}

	private final String uri;
	private List<HttpMethod> methods = new ArrayList<HttpMethod>();
	private final Map<HttpMethod, String> actionNames = new HashMap<HttpMethod, String>();
	private final Object controller;
	private boolean shouldSerializeResponse = true;
	private String name;
	private final Set<String> flags = new HashSet<>();
	private final Map<String, Object> parameters = new HashMap<String, Object>();
	protected final List<String> aliases = new ArrayList<String>();

	/**
	 * Create a RouteBuilder instance for the given URI pattern. URIs that match
	 * the pattern will map to methods on the POJO controller.
	 * 
	 * @param uri
	 *            a URI pattern
	 * @param controller
	 *            the POJO service controller.
	 */
	public RouteBuilder(final String uri, final Object controller) {
		super();
		this.uri = uri;
		this.controller = controller;
	}

	/**
	 * Child builder must implements this method.
	 * 
	 * @param pattern
	 * @param controller
	 * @param action
	 * @param method
	 * @param shouldSerializeResponse
	 * @param name
	 * @param supportedFormats
	 * @param flags
	 * @param parameters
	 * @param baseUrl
	 * @return
	 */
	protected abstract Route newRoute(String pattern, Object controller, Method action, HttpMethod method, boolean shouldSerializeResponse, String name, Set<String> flags, Map<String, Object> parameters);

	/**
	 * Child builder must implements this method.
	 * 
	 * @param uri
	 * @return
	 */
	protected abstract String toRegexPattern(String uri);

	/**
	 * Map a service method name (action) to a particular HTTP method (e.g. GET,
	 * POST, PUT, DELETE, HEAD, OPTIONS)
	 * 
	 * @param action
	 *            the name of a method within the service POJO.
	 * @param method
	 *            the HTTP method that should invoke the service method.
	 * @return the RouteBuilder instance.
	 */
	public RouteBuilder action(final String action, final HttpMethod method) {
		if (!actionNames.containsKey(method)) {
			actionNames.put(method, action);
		}

		if (!methods.contains(method)) {
			methods.add(method);
		}

		return this;
	}

	/**
	 * Defines HTTP methods that the route will support (e.g. GET, PUT, POST,
	 * DELETE, OPTIONS, HEAD). This utilizes the default HTTP method to service
	 * action mapping (e.g. GET maps to read(), PUT to update(), etc.).
	 * 
	 * @param methods
	 *            the HTTP methods supported by the route.
	 * @return the RouteBuilder instance.
	 */
	public RouteBuilder method(final HttpMethod... methods) {
		for (final HttpMethod method : methods) {
			if (!this.methods.contains(method)) {
				this.methods.add(method);
			}
		}

		return this;
	}

	/**
	 * Turns off serialization of the response--returns the response body as
	 * pain text.
	 * 
	 * @return the RouteBuilder instance.
	 */
	public RouteBuilder noSerialization() {
		this.shouldSerializeResponse = false;
		return this;
	}

	/**
	 * Turns on response serialization (the default) so the response body will
	 * be serialized (e.g. into JSON or XML).
	 * 
	 * @return the RouteBuilder instance.
	 */
	public RouteBuilder performSerialization() {
		this.shouldSerializeResponse = true;
		return this;
	}

	/**
	 * Give the route a known name to facilitate retrieving the route by name.
	 * This facilitates using the route URI pattern to create Link instances via
	 * LinkUtils.asLinks().
	 * 
	 * The name must be unique for each URI pattern.
	 * 
	 * @param name
	 *            the given name of the route for later retrieval.
	 * @return the RouteBuilder instance.
	 */
	public RouteBuilder name(final String name) {
		this.name = name;
		return this;
	}

	/**
	 * Flags are boolean settings that are created at route definition time.
	 * These flags can be used to pass booleans to preprocessors, controllers,
	 * or postprocessors. An example might be: flag(NO_AUTHORIZATION), which
	 * might inform an authorization preprocessor to skip authorization for this
	 * route.
	 * 
	 * @param flagValue
	 *            the name of the flag.
	 * @return this RouteBuilder to facilitate method chaining.
	 */
	public RouteBuilder flag(final String flagValue) {
		flags.add(flagValue);
		return this;
	}

	/**
	 * Parameters are named settings that are created at route definition time.
	 * These parameters can be used to pass data to subsequent preprocessors,
	 * controllers, or postprocessors. This is a way to pass data from a route
	 * definition down to subsequent controllers, etc. An example might be:
	 * setParameter("route", "read_foo") setParameter("permission",
	 * "view_private_data"), which might inform an authorization preprocessor of
	 * what permission is being requested on a given resource.
	 * 
	 * @param name
	 *            the name of the parameter.
	 * @param value
	 *            an object that is the parameter value.
	 * @return this RouteBuilder to facilitate method chaining.
	 */
	public RouteBuilder parameter(final String name, final Object value) {
		parameters.put(name, value);
		return this;
	}

	public RouteBuilder useStreamingMultipartUpload() {
		// TODO: complete supportMultipart()
		return this;
	}

	public RouteBuilder useStreamingDownload() {
		// TODO: complete useStreamingdownload()
		return this;
	}

	/**
	 * Build the Route instances. The last step in the Builder process.
	 * 
	 * @return a List of {@link Route} instances.
	 */
	public List<Route> build() {
		if (methods.isEmpty()) {
			methods = DEFAULT_HTTP_METHODS;
		}

		final List<Route> routes = new ArrayList<Route>();
		final String pattern = toRegexPattern(uri);

		for (final HttpMethod method : methods) {
			String actionName = actionNames.get(method);

			if (actionName == null) {
				actionName = ACTION_MAPPING.get(method);
			}

			final Method action = determineActionMethod(controller, actionName);
			routes.add(newRoute(pattern, controller, action, method, shouldSerializeResponse, name, flags, parameters));
		}

		return routes;
	}

	/**
	 * @return {@link RouteMetadata}
	 */
	public RouteMetadata asMetadata() {
		final List<String> methods = new ArrayList<String>();
		final UriMetadata uriMeta = new UriMetadata(uri);
		final List<Route> routes = build();

		for (final Route route : routes) {
			uriMeta.addAllParameters(route.getUrlParameters());
			methods.add(route.getMethod().getName());
		}
		final RouteMetadata metadata = new RouteMetadata(name, uriMeta,//
				aliases, methods, shouldSerializeResponse);

		return metadata;
	}

	// SECTION: UTILITY - PRIVATE

	/**
	 * Attempts to find the actionName on the controller, assuming a signature
	 * of actionName(Request, Response), and returns the action as a Method to
	 * be used later when the route is invoked.
	 * 
	 * @param controller
	 *            a pojo that implements a method named by the action, with
	 *            Request and Response as parameters.
	 * @param actionName
	 *            the name of a method on the given controller pojo.
	 * @return a Method instance referring to the action on the controller.
	 * @throws ConfigurationException
	 *             if an error occurs.
	 */
	private Method determineActionMethod(final Object controller, final String actionName) {
		try {
			return controller.getClass().getMethod(actionName, Request.class, Response.class);
		} catch (final Exception e) {
			throw new ConfigurationException(e);
		}
	}

}
