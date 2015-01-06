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
/*
 Copyright 2010, Strategic Gains, Inc.

 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at

 http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
 */
package org.restexpress.route;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.intelligentsia.commons.http.HttpHeader;
import org.intelligentsia.commons.http.exception.MethodNotAllowedException;
import org.intelligentsia.commons.http.exception.NotFoundException;
import org.jboss.netty.handler.codec.http.HttpMethod;
import org.restexpress.Request;
import org.restexpress.Response;
import org.restexpress.domain.metadata.RouteMetadata;
import org.restexpress.pipeline.MessageContext;
import org.restexpress.url.UrlMatch;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

/**
 * Contains the routes for a given service implementation.
 * 
 * @author <a href="mailto:jguibert@intelligents-ia.com" >Jerome Guibert</a>
 * @author toddf
 * @since May 21, 2010
 */
public final class RouteMapping implements RouteResolver {

	private final String baseUrl;
	private final Map<HttpMethod, List<Route>> routes;
	private final List<Route> deleteRoutes = new ArrayList<Route>();
	private final List<Route> getRoutes = new ArrayList<Route>();
	private final List<Route> postRoutes = new ArrayList<Route>();
	private final List<Route> putRoutes = new ArrayList<Route>();
	private final List<Route> optionRoutes = new ArrayList<Route>();
	private final List<Route> headRoutes = new ArrayList<Route>();

	private final Map<String, Map<HttpMethod, Route>> routesByName = new HashMap<String, Map<HttpMethod, Route>>();
	private final Map<String, List<Route>> routesByPattern = new LinkedHashMap<String, List<Route>>();

	private final List<RouteMetadata> routeMetadata = Lists.newArrayList();

	/**
	 * Build a new instance of {@link RouteMapping} with specified base url.
	 * 
	 * @param baseUrl
	 *            base URL
	 */
	public RouteMapping(final String baseUrl) {
		super();
		this.baseUrl = baseUrl;
		routes = new HashMap<HttpMethod, List<Route>>();
		routes.put(HttpMethod.DELETE, deleteRoutes);
		routes.put(HttpMethod.GET, getRoutes);
		routes.put(HttpMethod.POST, postRoutes);
		routes.put(HttpMethod.PUT, putRoutes);
		routes.put(HttpMethod.HEAD, headRoutes);
		routes.put(HttpMethod.OPTIONS, optionRoutes);
	}

	/**
	 * @return server base URL.
	 */
	public String getBaseUrl() {
		return baseUrl;
	}

	/**
	 * Return a list of Route instances for the given HTTP method. The returned
	 * list is immutable.
	 * 
	 * @param method
	 *            the HTTP method (GET, PUT, POST, DELETE) for which to retrieve
	 *            the routes.
	 */
	public List<Route> getRoutesFor(final HttpMethod method) {
		final List<Route> routesFor = routes.get(method);

		if (routesFor == null)
			return Collections.emptyList();

		return Collections.unmodifiableList(routesFor);
	}

	/**
	 * Attempts to match the path and method to an appropriate Route, returning
	 * an Action instance if a match is found. Returns null if no match is
	 * found.
	 * 
	 * @param method
	 *            the HTTP method (GET, PUT, POST, DELETE) for which to retrieve
	 *            the routes.
	 * @param path
	 *            the path portion of the url to match.
	 * @return a new Action or null, if the path/method combination don't match.
	 */
	public Action getActionFor(final HttpMethod method, final String path) {
		for (final Route route : routes.get(method)) {
			final UrlMatch match = route.match(path);

			if (match != null)
				return new Action(route, match);
		}

		return null;
	}

	/**
	 * Returns a list of Route instances that the given path resolves to.
	 * 
	 * @param path
	 *            the path portion of the URL (e.g. after the domain and port).
	 * @return A list of Route instances matching the given path. Never null.
	 */
	public List<Route> getMatchingRoutes(final String path) {
		for (final List<Route> patternRoutes : routesByPattern.values())
			if (patternRoutes.get(0).match(path) != null)
				return Collections.unmodifiableList(patternRoutes);

		return Collections.emptyList();
	}

	/**
	 * Returns the supported HTTP methods for the given URL path.
	 * 
	 * @param path
	 *            the path portion of the URL (e.g. after the domain and port).
	 * @return A list of appropriate HTTP methods for the given path. Never
	 *         null.
	 */
	public List<HttpMethod> getAllowedMethods(final String path) {
		final List<Route> matchingRoutes = getMatchingRoutes(path);

		if (matchingRoutes.isEmpty())
			return Collections.emptyList();

		final List<HttpMethod> methods = new ArrayList<HttpMethod>();

		for (final Route route : matchingRoutes)
			methods.add(route.getMethod());

		return methods;
	}

	/**
	 * Return a Route by the name and HttpMethod provided in DSL. Returns null
	 * if no route found.
	 * 
	 * @param name
	 * @return
	 */
	@Override
	public Route getNamedRoute(final String name, final HttpMethod method) {
		final Map<HttpMethod, Route> routesByMethod = routesByName.get(name);
		if (routesByMethod == null)
			return null;
		return routesByMethod.get(method);
	}

	@Override
	public String getNamedUrl(final String name, final HttpMethod method) {
		final Route route = getNamedRoute(name, method);
		return route != null ? getBaseUrl() + route.getPattern() : null;
	}

	@Override
	public Action resolve(final MessageContext context) {
		final Request request = context.getRequest();
		final Action action = getActionFor(request.getEffectiveHttpMethod(), request.getPath());

		if (action != null)
			return action;

		final List<HttpMethod> allowedMethods = getAllowedMethods(request.getPath());
		if (allowedMethods != null && !allowedMethods.isEmpty()) {
			final Response response = context.getResponse();
			for (final HttpMethod httpMethod : allowedMethods)
				response.addHeader(HttpHeader.ALLOW, httpMethod.getName());
			throw new MethodNotAllowedException(request.getUrl());
		}

		throw new NotFoundException("Unresolvable URL: " + request.getUrl());
	}

	/**
	 * Add given route
	 * 
	 * @param route
	 *            {@link Route} to add
	 */
	public void addRoute(final Route route) {
		routes.get(route.getMethod()).add(route);
		addByPattern(route);
		if (route.hasName())
			addNamedRoute(route);
	}

	private void addNamedRoute(final Route route) {
		Map<HttpMethod, Route> routesByMethod = routesByName.get(route.getName());
		if (routesByMethod == null) {
			routesByMethod = new HashMap<HttpMethod, Route>();
			routesByName.put(route.getName(), routesByMethod);
		}
		routesByMethod.put(route.getMethod(), route);
	}

	private void addByPattern(final Route route) {
		List<Route> urlRoutes = routesByPattern.get(route.getPattern());
		if (urlRoutes == null) {
			urlRoutes = new ArrayList<Route>();
			routesByPattern.put(route.getPattern(), urlRoutes);
		}
		urlRoutes.add(route);
	}

	/**
	 * Add a {@link RouteMetadata}.
	 * 
	 * @param metadata
	 */
	public void add(RouteMetadata metadata) {
		routeMetadata.add(metadata);
	}

	/**
	 * @return a {@link List} of declared {@link RouteMetadata}.
	 */
	public List<RouteMetadata> routeMetadata() {
		return routeMetadata;
	}

	/**
	 * Retrieve the named routes in this RestExpress server, creating a Map of
	 * them by name, with the value portion being populated with the URL
	 * pattern. Any '.{format}' portion of the URL pattern is omitted. Only
	 * named routes are included in the output.
	 * 
	 * @param baseUrl
	 *            the Base URL is included in the URL pattern
	 * 
	 * @return a Map of Route Name/URL pairs.
	 */
	public Map<String, String> getRouteUrlsByName(final String baseUrl) {
		final Map<String, String> urlsByName = Maps.newHashMap();
		for (RouteMetadata route : routeMetadata()) {
			if (route.getName() != null) {
				urlsByName.put(route.getName(), baseUrl + route.getUri().getPattern().replace(".{format}", ""));
			}
		}
		return urlsByName;
	}

}
