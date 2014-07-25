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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * {@link ServerMetadata} expose server metadata information on name, port,
 * supported formats, and routes.
 * 
 * @author toddf
 * @since Jan 31, 2011
 */
public class ServerMetadata implements Serializable {
	private static final long serialVersionUID = -8033976905088770790L;
	private String name;
	private int port;
	private String baseUrl;
	private Set<String> supportedMediaTypes;
	private Set<String> supportedFormats;
	private String defaultFormat;
	private final List<RouteMetadata> routes;

	/**
	 * Build a new instance of {@link ServerMetadata}.
	 */
	protected ServerMetadata() {
		super();
		routes = new ArrayList<>();
		supportedFormats = new HashSet<>();
	}

	public ServerMetadata(String name, int port, String baseUrl, Set<String> supportedMediaTypes, Set<String> supportedFormats, String defaultFormat, List<RouteMetadata> routes) {
		super();
		this.name = name;
		this.port = port;
		this.baseUrl = baseUrl;
		this.supportedMediaTypes = supportedMediaTypes;
		this.supportedFormats = supportedFormats;
		this.defaultFormat = defaultFormat;
		this.routes = routes;
	}

	public ServerMetadata(ServerMetadata root, RouteMetadata routeInfo) {
		this.name = root.name;
		this.port = root.port;
		this.baseUrl = root.baseUrl;
		this.supportedFormats = new HashSet<String>();
		this.supportedFormats.addAll(root.supportedFormats);
		this.defaultFormat = root.defaultFormat;
		this.routes = new ArrayList<RouteMetadata>();
		this.routes.add(routeInfo);
	}

	public String getBaseUrl() {
		return baseUrl;
	}
	
	public String getName() {
		return name;
	}

	public int getPort() {
		return port;
	}

	public Set<String> getSupportedMediaTypes() {
		return supportedMediaTypes;
	}
	
	public Set<String> getSupportedFormats() {
		return supportedFormats;
	}

	public String getDefaultFormat() {
		return defaultFormat;
	}

	public List<RouteMetadata> getRoutes() {
		return routes;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ServerMetadata other = (ServerMetadata) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "ServerMetadata {name=\"" + name + "\", port=\"" + port + " \"}";
	}

}
