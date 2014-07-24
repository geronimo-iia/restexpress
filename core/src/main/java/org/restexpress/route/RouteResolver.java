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

import java.util.List;

import org.intelligentsia.commons.http.ResponseHeader;
import org.intelligentsia.commons.http.exception.MethodNotAllowedException;
import org.intelligentsia.commons.http.exception.NotFoundException;
import org.jboss.netty.handler.codec.http.HttpMethod;
import org.restexpress.Request;
import org.restexpress.Response;
import org.restexpress.pipeline.MessageContext;
import org.restexpress.util.Resolver;

/**
 * @author toddf
 * @since May 4, 2010
 */
public class RouteResolver implements Resolver<Action> {

	/**
	 * {@link RouteMapping} instance.
	 */
	private final RouteMapping routeMapping;

	/**
	 * Build a new instance of {@link RouteResolver}.
	 * @param routes
	 */
	public RouteResolver(final RouteMapping routes) {
		super();
		this.routeMapping = routes;
	}

	/**
	 * 
	 * @param name
	 * @param method
	 * @return {@link Route} for specified name and {@link HttpMethod} or null
	 *         if none as found
	 */
	public Route getNamedRoute(final String name, final HttpMethod method) {
		return routeMapping.getNamedRoute(name, method);
	}

	@Override
	public Action resolve(final MessageContext context) {
		final Request request = context.getRequest();
		final Action action = routeMapping.getActionFor(request.getEffectiveHttpMethod(), request.getPath());

		if (action != null) {
			return action;
		}

		final List<HttpMethod> allowedMethods = routeMapping.getAllowedMethods(request.getPath());
		if ((allowedMethods != null) && !allowedMethods.isEmpty()) {
			final Response response = context.getResponse();
			for (final HttpMethod httpMethod : allowedMethods) {
				response.addHeader(ResponseHeader.ALLOW.getHeader(), httpMethod.getName());
			}
			throw new MethodNotAllowedException(request.getUrl());
		}

		throw new NotFoundException("Unresolvable URL: " + request.getUrl());
	}
}
