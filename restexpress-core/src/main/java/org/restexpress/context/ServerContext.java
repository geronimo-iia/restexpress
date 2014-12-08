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
package org.restexpress.context;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * The {@link ServerContext} class maintains a Map of name/value pairs.
 * 
 * <p>
 * It's a way for passing data from different sources to lower levels in the
 * framework.
 * </p>
 * 
 * 
 * @author <a href="mailto:jguibert@intelligents-ia.com" >Jerome Guibert</a>
 * 
 */
public class ServerContext {

	private final transient Map<String, Object> environment;

	/**
	 * Build a new instance of {@link ServerContext}.
	 */
	public ServerContext() {
		environment = new ConcurrentHashMap<>();
	}

	public boolean containsKey(String key) {
		return environment.containsKey(key);
	}

	public Object get(String name) {
		return environment.get(name);
	}

	public Object put(String name, Object value) {
		return environment.put(name, value);
	}

	public Object remove(String name) {
		return environment.remove(name);
	}

	public void clear() {
		environment.clear();
	}

}
