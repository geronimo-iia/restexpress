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
package org.restexpress.plugin.airdock;

import java.util.Map;

import org.restexpress.RestExpress;
import org.restexpress.serialization.SerializationProvider;

import com.google.common.base.Preconditions;

/**
 * {@link Context} represent context server.
 * 
 * @author <a href="mailto:jguibert@intelligents-ia.com" >Jerome Guibert</a>
 * 
 */
public final class Context {

	private final transient RestExpress server;

	private final transient Map<String, Object> properties;

	/**
	 * Build a new instance.
	 * 
	 * @param serializationProvider
	 *            {@link SerializationProvider} instance
	 * @param server
	 *            {@link RestExpress} instance
	 * @param properties
	 *            a {@link Map} of key, value
	 * @throws NullPointerException
	 *             if one parameter is null
	 */
	public Context(RestExpress server, Map<String, Object> properties) throws NullPointerException {
		super();
		this.server = Preconditions.checkNotNull(server);
		this.properties = Preconditions.checkNotNull(properties);
	}

	/**
	 * @return a {@link RestExpress} instance.
	 */
	public RestExpress server() {
		return server;
	}

	/**
	 * @return a Map of properties
	 */
	public Map<String, Object> properties() {
		return properties;
	}

	/**
	 * @param name
	 * @return associated value
	 */
	@SuppressWarnings("unchecked")
	public <T> T property(String name) {
		return (T) properties.get(name);
	}

	/**
	 * Add a property.
	 * 
	 * @param name
	 * @param value
	 * @return previous value or null if none was set.
	 */
	@SuppressWarnings("unchecked")
	public <T> T property(String name, T value) {
		return (T) properties.put(name, value);
	}
}
