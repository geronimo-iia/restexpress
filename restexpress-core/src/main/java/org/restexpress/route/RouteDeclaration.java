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
import java.util.Map;

import org.restexpress.domain.metadata.RouteMetadata;
import org.restexpress.route.parameterized.ParameterizedRouteBuilder;
import org.restexpress.route.regex.RegexRouteBuilder;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

/**
 * 
 * @author <a href="mailto:jguibert@intelligents-ia.com" >Jerome Guibert</a>
 * @author toddf
 * @since Jan 13, 2011
 */
public class RouteDeclaration {

	private final List<RouteBuilder> routeBuilders = Lists.newArrayList();
	private final List<RouteMetadata> routeMetadata = Lists.newArrayList();

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
	public RouteMapping createRouteMapping(String baseUrl) {
		final RouteMapping results = new RouteMapping(baseUrl);
		apply(new Function<RouteBuilder, Void>() {
			@Override
			public Void apply(final RouteBuilder builder) {
				// generate meta data
				routeMetadata.add(builder.asMetadata());
				// add all route on result
				for (final Route route : builder.build()) {
					results.addRoute(route);
				}
				return null;
			}
		});

		return results;
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
		
		apply(new Function<RouteBuilder, Void>() {
			@Override
			public Void apply(final RouteBuilder routeBuilder) {
				final RouteMetadata route = routeBuilder.asMetadata();

				if (route.getName() != null) {
					urlsByName.put(route.getName(), baseUrl + route.getUri().getPattern().replace(".{format}", ""));
				}
				return null;
			}
		});

		return urlsByName;
	}

	/**
	 * Apply a function Function<RouteBuilder, Void> on each
	 * {@link RouteBuilder}.
	 * 
	 * @param function
	 *            function to apply against collection
	 */
	public void apply(Function<RouteBuilder, Void> function) {
		for (final RouteBuilder builder : routeBuilders) {
			function.apply(builder);
		}
	}

	/**
	 * @return
	 */
	public List<RouteMetadata> getMetadata() {
		return routeMetadata;
	}
}
