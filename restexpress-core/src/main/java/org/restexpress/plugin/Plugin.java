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
 * {@link Plugin} is a way to extends functionality of {@link RestExpress} by
 * adding preprocessor, post org.restexpress.serialization, declaring routes,
 * etc..
 * 
 * @author <a href="mailto:jguibert@intelligents-ia.com" >Jerome Guibert</a>
 * @author toddf
 * @since Jul 20, 2011
 */
public interface Plugin extends Comparable<Plugin> {

	/**
	 * Called to initialize this {@link Plugin}. Within this method,
	 * pre/post-processors can be created, as well as routes injected.
	 * 
	 * Take care if you need other {@link Plugin} that they can be not yet
	 * initialized. In this last case, do your initialization inside
	 * {@link #bind(RestExpress)}.
	 * 
	 * @param server
	 *            {@link RestExpress} instance
	 */
	public void initialize(RestExpress server);

	/**
	 * 
	 * Called during bind of {@link RestExpress}, after all resources have been
	 * allocated and/or initialized.
	 * 
	 * Within this method, this {@link Plugin} can access such details as route
	 * metadata, etc.
	 * 
	 * 
	 * @param server
	 *            the fully-bound RestExpress server.
	 */
	public void bind(RestExpress server);

	/**
	 * Called during RestExpress.shutdown() to release resources held by this
	 * {@link Plugin}.
	 */
	public void destroy(RestExpress server);

	/**
	 * High priority means that {@link Plugin} will be initialized later
	 * 
	 * @return priority level for loading order.
	 */
	public int priority();
}
