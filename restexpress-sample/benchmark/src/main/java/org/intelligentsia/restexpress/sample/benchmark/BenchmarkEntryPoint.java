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
package org.intelligentsia.restexpress.sample.benchmark;

import org.jboss.netty.handler.codec.http.HttpMethod;
import org.restexpress.Request;
import org.restexpress.Response;
import org.restexpress.RestExpress;
import org.restexpress.RestExpressEntryPoint;

/**
 * {@link BenchmarkEntryPoint}.
 * 
 * @author <a href="mailto:jguibert@intelligents-ia.com" >Jerome Guibert</a>
 * 
 */
public class BenchmarkEntryPoint implements RestExpressEntryPoint {

	/**
	 * Build a new instance.
	 */
	public BenchmarkEntryPoint() {
	}

	/**
	 * @see org.restexpress.RestExpressEntryPoint#onLoad(org.restexpress.RestExpress)
	 */
	@Override
	public void onLoad(RestExpress restExpress) throws RuntimeException {
		// set name
		restExpress.settings().serverSettings().setName("Echo");
		// add echo route
		restExpress.uri("/echo", new Object() {

			@SuppressWarnings("unused")
			public String read(Request request, Response response) {
				String value = request.getHeader("echo");
				response.setContentType("text/xml");
				if (value == null) {
					return "<http_test><error>no value specified</error></http_test>";
				} else {
					return String.format("<http_test><value>%s</value></http_test>", value);
				}
			}
		}).method(HttpMethod.GET).noSerialization();
	}

}
