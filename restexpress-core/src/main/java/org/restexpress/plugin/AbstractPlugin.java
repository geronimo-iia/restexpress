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

import org.restexpress.RestExpress;

/**
 * {@link AbstractPlugin} implements basic functionality of {@link Plugin} and
 * provide a priority level to determine in which order server will load them.
 * Basically, on bind, all plugins are sorted and initialized into ascending
 * order. So high priority means that plugins will be initialized later. See
 * {@link PluginService}.
 * 
 * @author <a href="mailto:jguibert@intelligents-ia.com" >Jerome Guibert</a>
 * @author toddf
 * @since Jul 20, 2011
 */
public abstract class AbstractPlugin implements Plugin, Comparable<Plugin> {

	/**
	 * Priority level (default is 0).
	 */
	private final int priority;

	/**
	 * Build a new instance with default priority.
	 */
	public AbstractPlugin() {
		this(0);
	}

	/**
	 * Build a new instance with specified priority.
	 * 
	 * @param priority
	 */
	public AbstractPlugin(int priority) {
		super();
		this.priority = priority;
	}

	@Override
	public void initialize(RestExpress server) {
		// do nothing
	}

	@Override
	public void bind(final RestExpress server) {
		// do nothing
	}

	@Override
	public void destroy(final RestExpress server) {
		// do nothing
	}

	@Override
	public final int priority() {
		return priority;
	}

	/**
	 * This AbstractPlugin is assumed to be equal to other when:
	 * 
	 * <ol>
	 * <li>This class is assignable from other and</li>
	 * <li>other has the same simple class name.</li>
	 * </ol>
	 */
	@Override
	public final boolean equals(final Object other) {
		if (AbstractPlugin.class.isAssignableFrom(other.getClass()))
			return equals((AbstractPlugin) other);
		return false;
	}

	public final boolean equals(final AbstractPlugin plugin) {
		return this.getClass().getSimpleName().equals(plugin.getClass().getSimpleName());
	}

	/**
	 * Generates a hash code based on the class of this instance. All instances
	 * of the same class will have the same hash code.
	 */
	@Override
	public final int hashCode() {
		return this.getClass().hashCode() ^ 17;
	}

	/**
	 * We compare {@link Plugin} according their priority.
	 * 
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public final int compareTo(final Plugin o) {
		return priority - o.priority();
	}

}
