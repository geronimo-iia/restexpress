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
package org.restexpress.domain.metadata;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author toddf
 * @since Jan 31, 2011
 */
public class ServerMetadata {
	private String name = null;
	private int port;
	private List<String> supportedFormats = null;
	private String defaultFormat = null;
	private final List<RouteMetadata> routes = new ArrayList<RouteMetadata>();

	public ServerMetadata copyRootData() {
		final ServerMetadata copy = new ServerMetadata();
		copy.setName(getName());
		copy.setPort(getPort());
		copy.setDefaultFormat(getDefaultFormat());
		copy.addAllSupportedFormats(getSupportedFormats());
		return copy;
	}

	public String getName() {
		return name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public int getPort() {
		return port;
	}

	public void setPort(final int port) {
		this.port = port;
	}

	public List<String> getSupportedFormats() {
		return supportedFormats;
	}

	public void addSupportedFormat(final String format) {
		if (supportedFormats == null) {
			supportedFormats = new ArrayList<String>();
		}

		if (!supportedFormats.contains(format)) {
			supportedFormats.add(format);
		}
	}

	public void addAllSupportedFormats(final Collection<String> formats) {
		for (final String format : formats) {
			addSupportedFormat(format);
		}
	}

	public String getDefaultFormat() {
		return defaultFormat;
	}

	public void setDefaultFormat(final String defaultFormat) {
		this.defaultFormat = defaultFormat;
	}

	public List<RouteMetadata> getRoutes() {
		return routes;
	}

	public void addRoute(final RouteMetadata route) {
		routes.add(route);
	}

	public void addAllRoutes(final Collection<RouteMetadata> routes) {
		for (final RouteMetadata route : routes) {
			addRoute(route);
		}
	}
}
