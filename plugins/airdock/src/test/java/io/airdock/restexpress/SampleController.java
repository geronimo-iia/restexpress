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
package io.airdock.restexpress;

import java.util.Collections;
import java.util.List;

import org.jboss.netty.handler.codec.http.HttpMethod;
import org.restexpress.Request;
import org.restexpress.Response;

/**
 * SampleController extends {@link Controller}.
 * 
 * @author <a href="mailto:jguibert@intelligents-ia.com" >Jerome Guibert</a>
 * 
 */
public class SampleController extends Controller {
	public SampleController() {
		super();
	}

	@Override
	public void initialize(Context context) {
		super.initialize(context);

		context.server().uri("/your/route/here/{sampleId}.{format}", this) //
				.method(HttpMethod.GET, HttpMethod.PUT, HttpMethod.DELETE)//
				.name("sample.single.route");

		context.server().uri("/your/route/here.{format}", this)//
				.action("readAll", HttpMethod.GET).method(HttpMethod.POST)//
				.name("sample.collection.route");
	}

	public Object create(Request request, Response response) {
		// TODO: Your 'POST' logic here...
		return null;
	}

	public Object read(Request request, Response response) {
		// TODO: Your 'GET' logic here...
		return null;
	}

	public List<Object> readAll(Request request, Response response) {
		// TODO: Your 'GET collection' logic here...
		return Collections.emptyList();
	}

	public void update(Request request, Response response) {
		// TODO: Your 'PUT' logic here...
		response.setResponseNoContent();
	}

	public void delete(Request request, Response response) {
		// TODO: Your 'DELETE' logic here...
		response.setResponseNoContent();
	}
}
