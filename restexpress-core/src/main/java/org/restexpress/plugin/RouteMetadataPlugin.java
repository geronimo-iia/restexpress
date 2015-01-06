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
package org.restexpress.plugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.jboss.netty.handler.codec.http.HttpMethod;
import org.mvel2.templates.CompiledTemplate;
import org.mvel2.templates.TemplateCompiler;
import org.mvel2.templates.TemplateRuntime;
import org.restexpress.Request;
import org.restexpress.Response;
import org.restexpress.RestExpress;
import org.restexpress.domain.CharacterSet;
import org.restexpress.domain.MediaType;
import org.restexpress.domain.metadata.RouteMetadata;
import org.restexpress.domain.metadata.ServerMetadata;
import org.restexpress.exception.NotFoundException;
import org.restexpress.route.RouteBuilder;

import com.google.common.collect.Maps;

/**
 * {@link RouteMetadataPlugin}.
 * 
 * @author <a href="mailto:jguibert@intelligents-ia.com" >Jerome Guibert</a>
 * 
 */
public class RouteMetadataPlugin extends AbstractRoutePlugin {

	/**
	 * {@link RouteMetadataController} instance.
	 */
	private final RouteMetadataController routeMetadataController;

	/**
	 * {@link List} of {@link RouteBuilder}.
	 */
	private final List<RouteBuilder> routeBuilders;

	/**
	 * Build a new instance of {@link RouteMetadataPlugin}.
	 */
	public RouteMetadataPlugin() {
		super(Integer.MAX_VALUE); // initialized at the end
		routeMetadataController = new RouteMetadataController();
		routeBuilders = new ArrayList<RouteBuilder>();
	}

	@Override
	public void initialize(RestExpress server) {
		// declare new route
		routeBuilders.add(server.uri("/routes/metadata.{format}", routeMetadataController) //
				.action("getAllRoutes", HttpMethod.GET) //
				.name("all.routes.metadata"));

		routeBuilders.add(server.uri("/routes/{routeName}/metadata.{format}", routeMetadataController) //
				.action("getSingleRoute", HttpMethod.GET) //
				.name("single.route.metadata"));

		routeBuilders.add(server.uri("/routes", routeMetadataController) //
				.action("getConsole", HttpMethod.GET) //
				.noSerialization() //
				.name("routes.metadata.console"));

		// apply flag and parameter
		for (RouteBuilder routeBuilder : routeBuilders) {
			applyFlags(routeBuilder);
			applyParameters(routeBuilder);
		}
	}

	@Override
	public void bind(RestExpress server) {
		routeMetadataController.initialize(server.getRouteMetadata());
	}

	@Override
	public void destroy(RestExpress server) {
		routeBuilders.clear();
		routeMetadataController.destroy();
	}

	/**
	 * RouteMetadataController.
	 * 
	 * @author <a href="mailto:jguibert@intelligents-ia.com" >Jerome Guibert</a>
	 * 
	 */
	public static class RouteMetadataController {
		/**
		 * {@link ServerMetadata} instance.
		 */
		private ServerMetadata serverMetadata;
		/**
		 * {@link Map} of route name and {@link RouteMetadata}.
		 */
		private Map<String, RouteMetadata> routeMetadata;
		/**
		 * {@link CompiledTemplate} for console page.
		 */
		private CompiledTemplate console;

		/**
		 * Build a new instance of {@link RouteMetadataController}.
		 */
		public RouteMetadataController() {
			super();
		}

		/**
		 * Initialize controller and build cache of Route.
		 * 
		 * @param serverMetadata
		 *            {@link ServerMetadata} to use.
		 */
		public void initialize(ServerMetadata serverMetadata) {
			this.serverMetadata = serverMetadata;
			routeMetadata = Maps.newHashMap();
			for (RouteMetadata routeInfo : serverMetadata.getRoutes()) {
				// cache the named routes.
				if (routeInfo.getName() != null && !routeInfo.getName().trim().isEmpty()) {
					routeMetadata.put(routeInfo.getName().toLowerCase(Locale.US), routeInfo);
				}
			}

			// load template
			console = TemplateCompiler.compileTemplate(getClass().getClassLoader().getResourceAsStream("org.restexpress.plugin.console.html"));
			if (console == null)
				throw new IllegalStateException("no console template");
		}

		/**
		 * Destroy all Resources
		 */
		public void destroy() {
			serverMetadata = null;
			if (routeMetadata != null)
				routeMetadata.clear();
			routeMetadata = null;
			console = null;
		}

		/**
		 * Returns information on all routes. <code>/routes/metadata</code>
		 * 
		 * @param request
		 * @param response
		 * @return {@link ServerMetadata} instance.
		 */
		public ServerMetadata getAllRoutes(Request request, Response response) {
			return serverMetadata;
		}

		/**
		 * Returns information on a single route.
		 * <code>/routes/metadata/{routeName}</code>
		 * 
		 * @param request
		 * @param response
		 * @return {@link ServerMetadata}
		 * @throws NotFoundException
		 *             if route can not be found
		 */
		public ServerMetadata getSingleRoute(Request request, Response response) throws NotFoundException {
			String routeName = request.getHeader("routeName", "Route name must be provided");
			RouteMetadata routeInfo = routeMetadata.get(routeName.toLowerCase(Locale.US));
			if (routeInfo == null) {
				throw new NotFoundException("Route name not found: " + routeName);
			}
			return new ServerMetadata(serverMetadata, routeInfo);
		}

		/**
		 * Returns a console page. <code>/routes</code>
		 * 
		 * @param request
		 * @param response
		 * @return HTML content of console page.
		 */
		public String getConsole(Request request, Response response) {
			response.setContentType(MediaType.TEXT_HTML.withCharset(CharacterSet.UTF_8.getCharsetName()));
			Map<String, Object> vars = new HashMap<>();
			vars.put("server", serverMetadata);
			return TemplateRuntime.execute(console, vars).toString();
		}

	}
}
