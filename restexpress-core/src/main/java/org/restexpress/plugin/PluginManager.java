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

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.restexpress.RestExpress;

import com.google.common.collect.Lists;

/**
 * {@link PluginManager} implements {@link PluginService}.
 * 
 * @author <a href="mailto:jguibert@intelligents-ia.com" >Jerome Guibert</a>
 */
public final class PluginManager implements PluginService {

	/**
	 * {@link List} of {@link Plugin}.
	 */
	private final List<Plugin> plugins = Lists.newArrayList();

	/**
	 * Build a new instance of {@link PluginManager}.
	 */
	public PluginManager() {
		super();
	}

	/**
	 * Register specified {@link Plugin}. If the specified {@link Plugin} is
	 * ever registered, {@link #register(Plugin)} will do nothing.
	 * 
	 * 
	 * @param plugin
	 *            {@link Plugin} instance to register.
	 * @return true if the specified {@link Plugin} has been registered.
	 */
	public boolean register(Plugin plugin) {
		if (!plugins.contains(plugin)) {
			return plugins.add(plugin);
		}
		return false;
	}

	/**
	 * Initialize all {@link Plugin}.
	 * 
	 * @param restExpress
	 *            {@link RestExpress} instance
	 */
	public void initialize(RestExpress restExpress) {
		Collections.sort(plugins);
		for (Plugin plugin : plugins) {
			plugin.initialize(restExpress);
		}
	}

	/**
	 * Bind all {@link Plugin}.
	 * 
	 * @param restExpress
	 *            {@link RestExpress} instance
	 */
	public void bind(RestExpress restExpress) {
		Collections.sort(plugins);
		for (Plugin plugin : plugins) {
			plugin.bind(restExpress);
		}
	}

	/**
	 * Destroy all {@link Plugin}.
	 * 
	 * @param restExpress
	 *            {@link RestExpress} instance
	 */
	public void destroy(RestExpress restExpress) {
		Collections.sort(plugins);
		Collections.reverse(plugins);
		for (Plugin plugin : plugins) {
			plugin.destroy(restExpress);
		}
	}

	/**
	 * @see org.restexpress.plugin.PluginService#plugins()
	 */
	@Override
	public List<Plugin> plugins() {
		return Collections.unmodifiableList(plugins);
	}

	/**
	 * @see org.restexpress.plugin.PluginService#find(java.lang.Class)
	 */
	@Override
	@SuppressWarnings("unchecked")
	public <T> T find(final Class<T> interfaceName) {
		for (Plugin plugin : plugins) {
			if (interfaceName.isAssignableFrom(plugin.getClass())) {
				return (T) plugin;
			}
		}
		return null;
	}

	/**
	 * @see org.restexpress.plugin.PluginService#find(java.lang.String)
	 */
	@Override
	@SuppressWarnings("unchecked")
	public <T extends Plugin> T find(final String simplePluginClassName) {
		for (Plugin plugin : plugins) {
			if (plugin.getClass().getSimpleName().equals(simplePluginClassName)) {
				return (T) plugin;
			}
		}
		return null;
	}

	/**
	 * @see java.lang.Iterable#iterator()
	 */
	@Override
	public Iterator<Plugin> iterator() {
		return plugins().iterator();
	}
}
