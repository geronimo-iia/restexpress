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

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

/**
 * ControllerServiceEntryPoint provide an implementation of
 * {@link ServiceEntryPoint} to manage {@link Controller}.
 * 
 * @author <a href="mailto:jguibert@intelligents-ia.com" >Jerome Guibert</a>
 * 
 */
public class ControllerServiceEntryPoint implements ServiceEntryPoint {

	private Logger logger = LoggerFactory.getLogger(ControllerServiceEntryPoint.class);

	private Map<String, Controller> controllers;

	/**
	 * Build a new instance.
	 */
	public ControllerServiceEntryPoint() {
		super();
	}

	@Override
	public final String name() {
		return getClass().getSimpleName();
	}

	@Override
	public final void initialize(Context context) {
		List<Controller> controllers = instanciateControllers(context);
		if (controllers.isEmpty()) {
			logger.warn("No controller was found for {}.", name());
			return;
		}
		// create map of controller
		Map<String, Controller> map = Maps.newHashMap();
		for (Controller controller : controllers) {
			map.put(controller.name(), controller);
		}
		// add list of controller into context
		context.property(name() + ".controller", map);
		this.controllers = map;

		// initialize each controller
		logger.info("{} initialization", name());
		for (Controller controller : controllers) {
			controller.initialize(context);
			logger.info(controller.name() + " initialized");
		}
	}

	/**
	 * This method should be override by child entry point.
	 * 
	 * @return a {@link List} of {@link Controller}.
	 */
	protected List<Controller> instanciateControllers(Context context) {
		return Lists.newArrayList();
	}

	/**
	 * Destroy all controller.
	 * 
	 * @see io.airdock.ServiceEntryPoint.api.AirDockServiceEntryPoint#destroy()
	 */
	@Override
	public final void destroy() {
		for (Controller controller : controllers.values()) {
			controller.destroy();
		}
		controllers = null;
	}

	/**
	 * @param name
	 * @return associated {@link Controller} instance or null if none exists.
	 */
	@SuppressWarnings("unchecked")
	public <T extends Controller> T find(String name) {
		return (T) controllers.get(name);
	}
}
