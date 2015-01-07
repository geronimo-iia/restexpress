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

import java.util.List;

import org.restexpress.RestExpress;
import org.restexpress.route.parameterized.ParameterizedRouteBuilder;
import org.restexpress.route.regex.RegexRouteBuilder;

import com.google.common.collect.Lists;

/**
 * {@link RouteDeclaration} provide methods to declare {@link Route} and build a
 * {@link RouteMapping}.
 * 
 * @author <a href="mailto:jguibert@intelligents-ia.com" >Jerome Guibert</a>
 * @author toddf
 * @since Jan 13, 2011
 */
public final class RouteDeclaration {

	private final List<RouteBuilder> routeBuilders = Lists.newArrayList();

	/**
	 * Build a new instance of {@link RouteDeclaration}.
	 */
	public RouteDeclaration() {
		super();
	}

	/**
	 * Map a parameterized URL pattern to a controller.
	 * 
	 * @param urlPattern
	 *            a string specifying a URL pattern to match.
	 * @param controller
	 *            a POJO which contains implementations of the service methods
	 *            (e.g. create(), read(), update(), delete()).
	 */
	public ParameterizedRouteBuilder uri(final String uri, final Object controller) {
		final ParameterizedRouteBuilder builder = new ParameterizedRouteBuilder(uri, controller);
		routeBuilders.add(builder);
		return builder;
	}

	/**
	 * Map a Regex pattern to a controller.
	 * 
	 * @param regex
	 *            a string specifying a regex pattern to match.
	 * @param controller
	 *            a pojo which contains implementations of service methods (e.g.
	 *            create(), read(), update(), delete()).
	 */
	public RegexRouteBuilder regex(final String regex, final Object controller) {
		final RegexRouteBuilder builder = new RegexRouteBuilder(regex, controller);
		routeBuilders.add(builder);
		return builder;
	}

	/**
	 * Generate or update a {@link RouteMapping} (utilized by RouteResolver) from the declared
	 * routes. This method should be called only once.
	 * 
	 * After this call all previous route declaration will be cleaned on this
	 * instance.
	 * 
	 * @param restExpress
	 *            {@link RestExpress} instance
	 */
	public RouteMapping createRouteMapping(final RestExpress restExpress) {
		final RouteMapping results = restExpress.routeMapping();
		for (RouteBuilder builder : routeBuilders) {
			RouteDefinition routeDefinition = builder.build(restExpress);
			// generate meta data
			results.add(routeDefinition.metadata());
			// add all route on result
			for (final Route route : routeDefinition.routes()) {
				results.addRoute(route);
			}

		}
		clear();
		return results;
	}

	/**
	 * Clear all previous route declaration.
	 */
	public void clear() {
		routeBuilders.clear();
	}

}
