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
package org.restexpress.response;

import static org.jboss.netty.handler.codec.http.HttpHeaders.Names.CONNECTION;
import static org.jboss.netty.handler.codec.http.HttpHeaders.Names.CONTENT_LENGTH;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.ChannelFutureListener;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.http.DefaultHttpResponse;
import org.jboss.netty.handler.codec.http.HttpResponse;
import org.restexpress.ContentType;
import org.restexpress.Request;
import org.restexpress.Response;
import org.restexpress.util.HttpSpecification;

/**
 * @author toddf
 * @since Aug 26, 2010
 */
public class DefaultHttpResponseWriter implements HttpResponseWriter {
	@Override
	public void write(final ChannelHandlerContext ctx, final Request request, final Response response) {
		final HttpResponse httpResponse = new DefaultHttpResponse(request.getHttpVersion(), response.getResponseStatus());
		addHeaders(response, httpResponse);

		if (response.hasBody() && HttpSpecification.isContentAllowed(response)) {
			// If the response body already contains a ChannelBuffer, use it.
			if (ChannelBuffer.class.isAssignableFrom(response.getBody().getClass())) {
				httpResponse.setContent(ChannelBuffers.wrappedBuffer((ChannelBuffer) response.getBody()));
			} else // response body is assumed to be a string (e.g. JSON or
					// XML).
			{
				httpResponse.setContent(ChannelBuffers.copiedBuffer(response.getBody().toString(), ContentType.CHARSET));
			}
		}

		if (request.isKeepAlive()) {
			// Add 'Content-Length' header only for a keep-alive connection.
			if (HttpSpecification.isContentLengthAllowed(response)) {
				httpResponse.headers().set(CONTENT_LENGTH, String.valueOf(httpResponse.getContent().readableBytes()));
			}

			// Support "Connection: Keep-Alive" for HTTP 1.0 requests.
			if (request.isHttpVersion1_0()) {
				httpResponse.headers().add(CONNECTION, "Keep-Alive");
			}

			ctx.getChannel().write(httpResponse).addListener(ChannelFutureListener.CLOSE_ON_FAILURE);
		} else {
			httpResponse.headers().set(CONNECTION, "close");

			// Close the connection as soon as the message is sent.
			ctx.getChannel().write(httpResponse).addListener(ChannelFutureListener.CLOSE);
		}
	}

	/**
	 * @param response
	 * @param httpResponse
	 */
	private void addHeaders(final Response response, final HttpResponse httpResponse) {
		for (final String name : response.getHeaderNames()) {
			for (final String value : response.getHeaders(name)) {
				httpResponse.headers().add(name, value);
			}
		}
	}
}
