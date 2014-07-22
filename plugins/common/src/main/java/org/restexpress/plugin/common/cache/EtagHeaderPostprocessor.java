package org.restexpress.plugin.common.cache;

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

import org.intelligentsia.commons.http.ResponseHeader;
import org.jboss.netty.handler.codec.http.HttpMethod;
import org.restexpress.Request;
import org.restexpress.Response;
import org.restexpress.pipeline.MessageContext;
import org.restexpress.pipeline.Postprocessor;

/**
 * If the response body is non-null, adds an ETag header. ETag is computed from
 * the body object's hash code .
 * 
 * @author toddf
 * @since Oct 5, 2011
 */
public class EtagHeaderPostprocessor implements Postprocessor {
	@Override
	public void process(MessageContext context) {
		Request request = context.getRequest();
		Response response = context.getResponse();
		if (!request.isMethodGet() && !HttpMethod.HEAD.equals(request.getHttpMethod()))
			return;
		if (!response.hasBody())
			return;

		Object body = response.getBody();

		if (!response.hasHeader(ResponseHeader.ETAG.getHeader())) {
			response.addHeader(ResponseHeader.ETAG.getHeader(), String.format("\"%d\"", body.hashCode()));
		}
	}
}