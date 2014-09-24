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
 Copyright 2014, Strategic Gains, Inc.

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
package org.restexpress.plugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.restexpress.Flags;
import org.restexpress.route.RouteBuilder;

/**
 * {@link AbstractRoutePlugin} adds some convenience methods to {@link AbstractPlugin}
 * for plugins that create internal routes. {@link AbstractRoutePlugin} enables the
 * concept of flags and parameters on those routes.
 * <p>
 * Essentially, you want to be sure to call applyFlags(RouteBuilder) and
 * applyParameters(RouteBuilder) for each RouteBuilder created internally.
 * </p>
 * 
 * @author toddf
 * @since Mar 27, 2014
 */
public abstract class AbstractRoutePlugin extends AbstractPlugin {
	private final List<String> flags = new ArrayList<>();
	private final Map<String, Object> parameters = new HashMap<String, Object>();

	/**
	 * Build a new instance of {@link AbstractRoutePlugin}.
	 */
	public AbstractRoutePlugin() {
		super();
	}

	/**
	 * Add the flag value if not ever present.
	 * 
	 * @param flagValue
	 * @return {@link AbstractRoutePlugin} instance.
	 */
	public AbstractRoutePlugin flag(final Flags flagValue) {
		return flag(flagValue.toString());
	}

	/**
	 * Add the flag value if not ever present.
	 * 
	 * @param flagValue
	 * @return {@link AbstractRoutePlugin} instance.
	 */
	public AbstractRoutePlugin flag(final String flagValue) {
		if (!flags.contains(flagValue)) {
			flags.add(flagValue);
		}
		return this;
	}

	/**
	 * Add a route parameter/value if parameter is not ever present.
	 * 
	 * @param name
	 *            parameter name
	 * @param value
	 *            associated value.
	 * @return {@link AbstractRoutePlugin} instance.
	 */
	public AbstractRoutePlugin parameter(final String name, final Object value) {
		if (!parameters.containsKey(name)) {
			parameters.put(name, value);
		}

		return this;
	}

	/**
	 * Apply all flag on specified route builder.
	 * 
	 * @param routeBuilder
	 * @return {@link AbstractRoutePlugin} instance.
	 */
	protected AbstractRoutePlugin applyFlags(final RouteBuilder routeBuilder) {
		for (final String flag : flags) {
			routeBuilder.flag(flag);
		}
		return this;
	}

	/**
	 * Apply all parameter on specified route builder.
	 * 
	 * @param routeBuilder
	 * @return {@link AbstractRoutePlugin} instance.
	 */
	protected AbstractRoutePlugin applyParameters(final RouteBuilder routeBuilder) {
		for (final Entry<String, Object> entry : parameters.entrySet()) {
			routeBuilder.parameter(entry.getKey(), entry.getValue());
		}
		return this;
	}
}
