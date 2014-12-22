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
package org.restexpress.plugin.swagger;

import org.restexpress.RestExpress;
import org.restexpress.plugin.AbstractRoutePlugin;

/**
 * SwaggerPlugin.
 *
 * @author <a href="mailto:jguibert@intelligents-ia.com" >Jerome Guibert</a>
 *
 */
public class SwaggerPlugin extends AbstractRoutePlugin {

	public static final String SWAGGER_VERSION = "1.2";

	public static final String URL_PATH = "/api-docs";
	
	private String basePath = URL_PATH;

	private String apiVersion;

	private String swaggerVersion = SWAGGER_VERSION;
	
	
	
	public SwaggerPlugin() {

	}

	@Override
	public void initialize(RestExpress server) {
		// TODO
	}

	@Override
	public void bind(RestExpress server) {
		// TODO
	}

	@Override
	public void destroy(RestExpress server) {
		// TODO
	}
}
