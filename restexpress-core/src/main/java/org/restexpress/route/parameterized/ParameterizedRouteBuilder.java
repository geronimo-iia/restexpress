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
package org.restexpress.route.parameterized;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.Set;

import org.jboss.netty.handler.codec.http.HttpMethod;
import org.restexpress.route.Route;
import org.restexpress.route.RouteBuilder;

/**
 * @author toddf
 * @since Jan 13, 2011
 */
public class ParameterizedRouteBuilder extends RouteBuilder {

	/**
	 * @param uri
	 * @param controller
	 * @param routeType
	 */
	public ParameterizedRouteBuilder(final String uri, final Object controller) {
		super(uri, controller);
	}

	@Override
	protected Route newRoute(final String pattern, final Object controller, final Method action, final HttpMethod method, final boolean shouldSerializeResponse, final String name, final Set<String> flags, final Map<String, Object> parameters) {
		final ParameterizedRoute parameterizedRoute = new ParameterizedRoute(pattern, controller, action, method, shouldSerializeResponse, name, flags, parameters);
		parameterizedRoute.addAliases(aliases);
		return parameterizedRoute;
	}

	/**
	 * Associate another URI pattern to this route, essentially making an alias
	 * for the route. There may be multiple alias URIs for a given route. Note
	 * that new parameter nodes (e.g. {id}) in the URI will be available within
	 * the method. Parameter nodes that are missing from the alias will not be
	 * available in the action method.
	 * 
	 * @param uri
	 *            the alias URI.
	 * @return the ParameterizedRouteBuilder instance (this).
	 */
	public ParameterizedRouteBuilder alias(final String uri) {
		if (!aliases.contains(uri)) {
			aliases.add(uri);
		}
		return this;
	}

	@Override
	protected String toRegexPattern(final String uri) {
		String pattern = uri;
		if ((pattern != null) && !pattern.startsWith("/")) {
			pattern = "/" + pattern;
		}
		return pattern;
	}

}
