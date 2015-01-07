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
package com.echo;

import org.jboss.netty.buffer.ChannelBuffer;
import org.restexpress.Request;
import org.restexpress.Response;

public class EchoController {
	private static final String ECHO_PARAMETER_NOT_FOUND = "'echo' header or query-string parameter not found. Please set query-string parameter 'echo' (e.g. ?echo=value).";
	private static final String ECHO_HEADER = "echo";

	public ChannelBuffer create(Request request, Response response) {
		response.setResponseCreated();
		return request.getEntity();
	}

	public String delete(Request request, Response response) {
		return request.getHeader(ECHO_HEADER, ECHO_PARAMETER_NOT_FOUND);
	}

	public String read(Request request, Response response) {
		String echo = request.getHeader(ECHO_HEADER);
		if (echo == null) {
			return "Please set query-string parameter 'echo' (e.g. ?echo=value)";
		}
		return echo;
	}

	public ChannelBuffer update(Request request, Response response) {
		return request.getEntity();
	}
}
