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

import java.util.List;

import org.restexpress.RestExpress;

/**
 * {@link PluginService} define methods to expose {@link Plugin} used with
 * {@link RestExpress}.
 * 
 * @author <a href="mailto:jguibert@intelligents-ia.com" >Jerome Guibert</a>
 */
public interface PluginService extends Iterable<Plugin> {

	/**
	 * @return a sorted unmodifiable {@link List} of registered {@link Plugin}.
	 */
	public List<Plugin> plugins();

	/**
	 * Find the first {@link Plugin} which implements the specified interface.
	 * 
	 * @param interfaceName
	 *            class of wished interface.
	 * @return an instance of {@link Plugin} which implement the specified
	 *         interface or null if none is found.
	 */
	public <T> T find(final Class<T> interfaceName);

	/**
	 * @param simplePluginClassName
	 *            simple Plugin Class Name
	 * @return an instance of {@link Plugin} named simplePluginClassName or null
	 *         if none is found.
	 */
	public <T extends Plugin> T find(final String simplePluginClassName);

}
