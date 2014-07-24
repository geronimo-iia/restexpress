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
/*
 Copyright 2011, Strategic Gains, Inc.

 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at

 http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
 */
package org.restexpress.test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.restexpress.Request;
import org.restexpress.Response;
import org.restexpress.domain.CharacterSet;
import org.restexpress.pipeline.HttpResponseWriter;
import org.restexpress.util.HttpSpecification;

/**
 * @author toddf
 * @since Feb 10, 2011
 */
public class StringBufferHttpResponseWriter implements HttpResponseWriter {
	private Map<String, List<String>> headers;
	private StringBuffer body;

	public StringBufferHttpResponseWriter(StringBuffer buffer) {
		this(null, buffer);
	}

	public StringBufferHttpResponseWriter(Map<String, List<String>> headers, StringBuffer buffer) {
		super();
		this.body = buffer;
		this.headers = headers;
	}

	@Override
	public void write(ChannelHandlerContext ctx, Request request, Response response) {
		if (headers != null) {
			for (String headerName : response.getHeaderNames()) {
				List<String> headerValues = new ArrayList<String>(response.getHeaders(headerName));
				headers.put(headerName, headerValues);
			}
		}
		if (response.hasBody() && HttpSpecification.isContentAllowed(response)) {
			if (ChannelBuffer.class.isAssignableFrom(response.getBody().getClass())) {
				ChannelBuffer channelBuffer = (ChannelBuffer) response.getBody();
				body.append(channelBuffer.toString(CharacterSet.UTF_8.getCharset()));
			} else {
				body.append(response.getBody().toString());
			}
		} else {
			// no content is null
			body.append((String) null);
		}
	}
}
