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
    Copyright 2011, Strategic Gains, Inc.

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
import java.util.List;

import org.restexpress.domain.metadata.RouteMetadata;
import org.restexpress.route.parameterized.ParameterizedRouteBuilder;
import org.restexpress.route.regex.RegexRouteBuilder;
import org.restexpress.util.Callback;

/**
 * @author toddf
 * @since Jan 13, 2011
 */
public class RouteDeclaration {
	// SECTION: INSTANCE VARIABLES

	private final List<RouteBuilder> routeBuilders;
	List<RouteMetadata> routeMetadata = new ArrayList<RouteMetadata>();

	public RouteDeclaration() {
		super();
		this.routeBuilders = new ArrayList<RouteBuilder>();
	}

	// SECTION: URL MAPPING

	/**
	 * Map a parameterized URL pattern to a controller.
	 * 
	 * @param urlPattern
	 *            a string specifying a URL pattern to match.
	 * @param controller
	 *            a pojo which contains implementations of the service methods
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
	 * Generate a RouteMapping (utilized by RouteResolver) from the declared
	 * routes.
	 */
	public RouteMapping createRouteMapping() {
		final RouteMapping results = new RouteMapping();

		iterateRouteBuilders(new Callback<RouteBuilder>() {
			@Override
			public void process(final RouteBuilder builder) {
				routeMetadata.add(builder.asMetadata());

				for (final Route route : builder.build()) {
					results.addRoute(route);
				}
			}
		});

		return results;
	}

	// SECTION: CONSOLE

	public void iterateRouteBuilders(final Callback<RouteBuilder> callback) {
		for (final RouteBuilder builder : routeBuilders) {
			callback.process(builder);
		}
	}

	/**
	 * @return
	 */
	public List<RouteMetadata> getMetadata() {
		return routeMetadata;
	}
}
