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
/**
 * 
 */
package org.restexpress.server;

import org.restexpress.RestExpress;
import org.restexpress.RestExpressEntryPoint;
import org.restexpress.plugin.common.cache.CacheControlPlugin;
import org.restexpress.plugin.route.RouteMetadataPlugin;

/**
 * {@link ServerRestExpressEntryPoint} add several plugin.
 * 
 * @author <a href="mailto:jguibert@intelligents-ia.com" >Jerome Guibert</a>
 * 
 */
public class ServerRestExpressEntryPoint implements RestExpressEntryPoint {

	/**
	 * Build a new instance.
	 */
	public ServerRestExpressEntryPoint() {
	}

	/**
	 * @see org.restexpress.RestExpressEntryPoint#onLoad(org.restexpress.RestExpress)
	 */
	@Override
	public void onLoad(RestExpress restExpress) throws RuntimeException {
		// add Route Metadata plugin
		restExpress.registerPlugin(new RouteMetadataPlugin());
		// add cache plugin
		restExpress.registerPlugin(new CacheControlPlugin());
	}

}
