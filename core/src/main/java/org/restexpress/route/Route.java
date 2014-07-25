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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.intelligentsia.commons.http.exception.HttpRuntimeException;
import org.jboss.netty.handler.codec.http.HttpMethod;
import org.restexpress.Flags;
import org.restexpress.Request;
import org.restexpress.Response;
import org.restexpress.url.UrlMatch;
import org.restexpress.url.UrlMatcher;

/**
 * A Route is an immutable relationship between a URL pattern and a REST
 * service.
 * 
 * @author toddf
 * @since May 4, 2010
 */
public abstract class Route {

	private final UrlMatcher urlMatcher;
	private final Object controller;
	private final Method action;
	private final HttpMethod method;
	private boolean shouldSerializeResponse = true;
	private final String name;
	// private final List<String> supportedFormats = new ArrayList<String>();
	private final Set<String> flags = new HashSet<>();
	private final Map<String, Object> parameters = new HashMap<String, Object>();

	public Route(final UrlMatcher urlMatcher, final Object controller, final Method action, final HttpMethod method, final boolean shouldSerializeResponse, final String name, final List<String> supportedFormats, final Set<String> flags,
			final Map<String, Object> parameters) {
		super();
		this.urlMatcher = urlMatcher;
		this.controller = controller;
		this.action = action;
		this.action.setAccessible(true);
		this.method = method;
		this.shouldSerializeResponse = shouldSerializeResponse;
		this.name = name;
		// this.supportedFormats.addAll(supportedFormats);
		this.flags.addAll(flags);
		this.parameters.putAll(parameters);
	}

	public boolean isFlagged(final String flag) {
		return flags.contains(flag);
	}

	public boolean isFlagged(final Flags flag) {
		return flags.contains(flag.toString());
	}

	public boolean hasParameter(final String name) {
		return (getParameter(name) != null);
	}

	public Object getParameter(final String name) {
		return parameters.get(name);
	}

	public Method getAction() {
		return action;
	}

	public Object getController() {
		return controller;
	}

	public HttpMethod getMethod() {
		return method;
	}

	public String getName() {
		return name;
	}

	public boolean hasName() {
		return ((getName() != null) && !getName().trim().isEmpty());
	}

	/**
	 * Returns the URL pattern without any '.{format}' at the end. In essence, a
	 * 'short' URL pattern.
	 * 
	 * @return a URL pattern
	 */
	public String getPattern() {
		return urlMatcher.getPattern();
	}

	public boolean shouldSerializeResponse() {
		return shouldSerializeResponse;
	}

	// public Collection<String> getSupportedFormats() {
	// return Collections.unmodifiableList(supportedFormats);
	// }
	//
	// public boolean hasSupportedFormats() {
	// return (!supportedFormats.isEmpty());
	// }
	//
	// public void addAllSupportedFormats(final List<String> formats) {
	// supportedFormats.addAll(formats);
	// }
	//
	// public void addSupportedFormat(final String format) {
	// if (!supportsFormat(format)) {
	// supportedFormats.add(format);
	// }
	// }
	//
	// public boolean supportsFormat(final String format) {
	// return supportedFormats.contains(format);
	// }

	public UrlMatch match(final String url) {
		return urlMatcher.match(url);
	}

	public List<String> getUrlParameters() {
		return urlMatcher.getParameterNames();
	}

	public Object invoke(final Request request, final Response response) {
		try {
			return action.invoke(controller, request, response);
		} catch (final InvocationTargetException e) {
			final Throwable cause = e.getCause();

			if (RuntimeException.class.isAssignableFrom(cause.getClass())) {
				throw (RuntimeException) e.getCause();
			} else {
				throw new RuntimeException(cause);
			}
		} catch (final Exception e) {
			throw new HttpRuntimeException(e);
		}
	}
}
