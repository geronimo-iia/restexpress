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
package org.restexpress.route.regex;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.Set;

import org.jboss.netty.handler.codec.http.HttpMethod;
import org.restexpress.route.Route;
import org.restexpress.url.UrlRegex;

/**
 * @author toddf
 * @since Jan 7, 2011
 */
public class RegexRoute extends Route {

	public RegexRoute(final UrlRegex urlMatcher, final Object controller, final Method action, final HttpMethod method, final boolean shouldSerializeResponse, final String name,  final Set<String> flags,
			final Map<String, Object> parameters) {
		super(urlMatcher, controller, action, method, shouldSerializeResponse, name, flags, parameters);
	}

	public RegexRoute(final String urlPattern, final Object controller, final Method action, final HttpMethod method, final boolean shouldSerializeResponse, final String name,  final Set<String> flags,
			final Map<String, Object> parameters) {
		this(new UrlRegex(urlPattern), controller, action, method, shouldSerializeResponse, name,  flags, parameters);
	}
}
