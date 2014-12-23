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

import java.util.Map;

import org.intelligentsia.commons.http.exception.NotFoundException;
import org.restexpress.Request;
import org.restexpress.Response;
import org.restexpress.plugin.swagger.model.ApiDescription;
import org.restexpress.plugin.swagger.model.ResourceListing;

/**
 * {@link SwaggerController} implement a controller accoring swagger
 * specification.
 *
 * @author <a href="mailto:jguibert@intelligents-ia.com" >Jerome Guibert</a>
 */
public class SwaggerController {

	protected ResourceListing resourceListing;

	protected Map<String, ApiDescription> apisByPath;

	public SwaggerController() {
		super();
	}

	// on call /api-doc
	public ResourceListing read(Request request, Response response) {
		return resourceListing;
	}

	// on call /api-doc/{path}
	public ApiDescription readPath(Request request, Response response) {
		String path = request.getHeader("path");
		ApiDescription api = apisByPath.get("/" + path);
		if (api == null)
			throw new NotFoundException();
		return api;
	}
}
