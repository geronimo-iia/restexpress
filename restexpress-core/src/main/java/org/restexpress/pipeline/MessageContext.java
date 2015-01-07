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
package org.restexpress.pipeline;

import java.net.URLDecoder;
import java.util.Collection;
import java.util.Map.Entry;

import javax.ws.rs.core.Response.StatusType;

import org.restexpress.Request;
import org.restexpress.Response;
import org.restexpress.domain.CharacterSet;
import org.restexpress.http.HttpHeader;
import org.restexpress.response.ResponseProcessorSetting;
import org.restexpress.route.Action;

/**
 * {@link MessageContext}.
 * 
 * @author toddf
 * @since Feb 2, 2011
 */
public final class MessageContext {
	/**
	 * {@link Request}
	 */
	private final Request request;
	/**
	 * {@link Response}
	 */
	private final Response response;
	/**
	 * {@link Action} instance (to be resolved).
	 */
	private Action action;
	/**
	 * {@link ResponseProcessorSetting} instance (to be resolved)
	 */
	private ResponseProcessorSetting responseProcessorSetting;

	/**
	 * Build a new instance of {@link MessageContext}.
	 * 
	 * @param request
	 * @param response
	 */
	public MessageContext(final Request request, final Response response) {
		super();
		this.request = request;
		this.response = response;
		action = null;
		responseProcessorSetting = null;
	}

	public Request getRequest() {
		return request;
	}

	public Response getResponse() {
		return response;
	}

	public Action getAction() {
		return action;
	}

	public boolean hasAction() {
		return (getAction() != null);
	}

	public void setAction(final Action action) {
		this.action = action;
		addUrlParametersAsHeaders(getRequest(), action.parameters());
		getRequest().setResolvedRoute(action.resolvedRoute());
		getResponse().setIsSerialized(action.shouldSerializeResponse());
	}

	public Throwable getException() {
		return getResponse().getException();
	}

	/**
	 * Set Exception on this {@link MessageContext}. This response header will
	 * be removed from {@link Response}:
	 * <ul>
	 * <li>{@link ResponseHeader#CONTENT_TYPE}</li>
	 * <li>{@link ResponseHeader#CONTENT_LENGTH}</li>
	 * </ul>
	 * 
	 * @param throwable
	 *            root cause
	 */
	public void setException(final Throwable throwable) {
		response.setException(throwable);
		response.headers().remove(HttpHeader.CONTENT_TYPE.toString());
		response.headers().remove(HttpHeader.CONTENT_LENGTH.toString());
	}

	/**
	 * Set the HTTP response status.
	 * @see Response#setStatusInfo(StatusType)
	 * 
	 * @param statusInfo
	 */
	public void setStatusInfo(final StatusType statusInfo) {
		getResponse().setStatusInfo(statusInfo);
	}

	public ResponseProcessorSetting getResponseProcessorSetting() {
		return responseProcessorSetting;
	}

	public void setResponseProcessorSetting(ResponseProcessorSetting responseProcessorSetting) {
		this.responseProcessorSetting = responseProcessorSetting;
	}

	/**
	 * Copy all URL parameter in request header.
	 * 
	 * @param request
	 * @param parameters
	 *            parameters find from action.
	 */
	private static void addUrlParametersAsHeaders(final Request request, final Collection<Entry<String, String>> parameters) {
		for (final Entry<String, String> entry : parameters) {
			try {
				request.addHeader(entry.getKey(), URLDecoder.decode(entry.getValue(), CharacterSet.UTF_8.getCharsetName()));
			} catch (final Exception e) {
				request.addHeader(entry.getKey(), entry.getValue());
			}
		}
	}
}
