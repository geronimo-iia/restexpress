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
import java.util.Collections;
import java.util.List;

/**
 * @author toddf
 * @since Jan 31, 2011
 */
public class RouteMetadata {
	private String name;
	private UriMetadata uri;
	private List<String> aliases;
	private List<String> supportedFormats;
	private String defaultFormat;
	private final List<String> methods = new ArrayList<String>();
	private boolean isSerialized;
	private String baseUrl;

	public String getName() {
		return name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public UriMetadata getUri() {
		return uri;
	}

	public void setUri(final UriMetadata uri) {
		this.uri = uri;
	}

	@SuppressWarnings("unchecked")
	public List<String> getSupportedFormats() {
		return supportedFormats == null ? Collections.EMPTY_LIST : supportedFormats;
	}

	public void addSupportedFormat(final String format) {
		if (supportedFormats == null) {
			supportedFormats = new ArrayList<String>();
		}

		if (!getSupportedFormats().contains(format)) {
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

	@SuppressWarnings("unchecked")
	public List<String> getMethods() {
		return methods == null ? Collections.EMPTY_LIST : methods;
	}

	public void addMethod(final String method) {
		if (!methods.contains(method)) {
			methods.add(method);
		}
	}

	public void addAllMethods(final Collection<String> methods) {
		for (final String method : methods) {
			addMethod(method);
		}
	}

	public boolean isSerialized() {
		return isSerialized;
	}

	public void setSerialized(final boolean isSerialized) {
		this.isSerialized = isSerialized;
	}

	@SuppressWarnings("unchecked")
	public List<String> getAliases() {
		return aliases == null ? Collections.EMPTY_LIST : Collections.unmodifiableList(aliases);
	}

	public void addAlias(final String alias) {
		if (aliases == null) {
			aliases = new ArrayList<String>();
		}

		if (!aliases.contains(alias)) {
			aliases.add(alias);
		}
	}

	public void addAllAliases(final List<String> aliases) {
		for (final String alias : aliases) {
			addAlias(alias);
		}
	}

	public String getBaseUrl() {
		return baseUrl;
	}

	public void setBaseUrl(final String baseUrl) {
		this.baseUrl = baseUrl;
	}
}
