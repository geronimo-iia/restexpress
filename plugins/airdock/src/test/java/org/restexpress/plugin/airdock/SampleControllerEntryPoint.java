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

import org.restexpress.plugin.airdock.Context;
import org.restexpress.plugin.airdock.Controller;
import org.restexpress.plugin.airdock.ControllerServiceEntryPoint;

/**
 * SampleControllerEntryPoint extends {@link ControllerServiceEntryPoint} and
 * just add all controller instance to manage.
 * 
 * This entry point is declared inside file
 * META-INF/services/org.restexpress.plugin.airdock.ServiceEntryPoint.
 * 
 * @author <a href="mailto:jguibert@intelligents-ia.com" >Jerome Guibert</a>
 */
public class SampleControllerEntryPoint extends ControllerServiceEntryPoint {

	/**
	 * Build a new instance of {@link SampleControllerEntryPoint}.
	 */
	public SampleControllerEntryPoint() {
		super();
	}

	@Override
	protected List<Controller> instanciateControllers(Context context) {
		List<Controller> controllers = super.instanciateControllers(context);
		controllers.add(new SampleController());
		return controllers;
	}

}
