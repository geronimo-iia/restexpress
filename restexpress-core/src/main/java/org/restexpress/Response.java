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
package org.restexpress;

import static org.jboss.netty.handler.codec.http.HttpResponseStatus.OK;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.intelligentsia.commons.http.ResponseHeader;
import org.jboss.netty.handler.codec.http.HttpHeaders;
import org.jboss.netty.handler.codec.http.HttpResponseStatus;
import org.restexpress.query.QueryRange;

import com.google.common.collect.Maps;

/**
 * {@link Response} definition for RestExpress.
 * 
 * @author toddf
 * @since Nov 20, 2009
 */
public class Response {

	private HttpResponseStatus responseCode = OK;
	private Object body;
	private final Map<String, List<String>> headers = Maps.newHashMap();
	private boolean isSerialized = true;
	private Throwable exception = null;

	public Response() {
		super();
	}

	/**
	 * @return response body as {@link Object}
	 */
	public Object getBody() {
		return body;
	}

	/**
	 * @return True if body member is not null
	 */
	public boolean hasBody() {
		return (getBody() != null);
	}

	/**
	 * Set response body.
	 * 
	 * @param body
	 *            response body
	 */
	public void setBody(final Object body) {
		this.body = body;
	}

	public void clearHeaders() {
		headers.clear();
	}

	public Map<String, List<String>> headers() {
		return headers;
	}

	public String getHeader(final String name) {
		final List<String> list = headers.get(name);

		if ((list != null) && !list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	public List<String> getHeaders(final String name) {
		return headers.get(name);
	}

	public boolean hasHeader(final String name) {
		return (getHeader(name) != null);
	}

	public boolean hasHeaders() {
		return !headers.isEmpty();
	}

	public Set<String> getHeaderNames() {
		return headers.keySet();
	}

	/**
	 * Add a header value to the response.
	 * 
	 * @param name
	 * @param value
	 */
	public void addHeader(final String name, final String value) {
		List<String> list = headers.get(name);

		if (list == null) {
			list = new ArrayList<String>();
			headers.put(name, list);
		}

		list.add(value);
	}

	/**
	 * Add a "Content-Range" header to the response, setting it to the range and
	 * count. This enables datagrid-style pagination support.
	 * 
	 * @param response
	 * @param range
	 * @param count
	 * @param size
	 */
	public void addRangeHeader(final QueryRange range, final long count) {
		addHeader(ResponseHeader.CONTENT_RANGE.getHeader(), range.asContentRange(count));
	}

	/**
	 * Add a "Location" header to the response.
	 * 
	 * @param url
	 *            URL location
	 */
	public void addLocationHeader(final String url) {
		addHeader(ResponseHeader.LOCATION.getHeader(), url);
	}

	/**
	 * Sets HTTP response code and Content-Range header appropriately for the
	 * requested QueryRange, returned collection size and maximum data set size.
	 * 
	 * @param queryRange
	 * @param results
	 * @param count
	 */
	public void setCollectionResponse(final QueryRange queryRange, final int size, final long count) {
		final QueryRange range = queryRange.clone();

		if (range.isOutside(size, count)) {
			setResponseCode(416);
			range.setOffset(0);
			range.setLimitViaEnd(Math.min(count - 1, range.getLimit()));
		} else if (range.extendsBeyond(size, count)) {
			range.setLimitViaEnd((count > 1 ? count - 1 : 0));

			if ((count > 0) && !range.spans(size, count)) {
				setResponseStatus(HttpResponseStatus.PARTIAL_CONTENT);
			}
		} else if (range.isInside(size, count)) {
			setResponseStatus(HttpResponseStatus.PARTIAL_CONTENT);
		}

		addRangeHeader(range, count);
	}

	/**
	 * Set the HTTP response status code.
	 * 
	 * @param value
	 */
	public void setResponseCode(final int value) {
		setResponseStatus(HttpResponseStatus.valueOf(value));
	}

	/**
	 * Set the HTTP response status.
	 * 
	 * @param status
	 */
	public void setResponseStatus(final HttpResponseStatus status) {
		this.responseCode = status;
	}

	/**
	 * Sets the HTTP response status code to 201 - created.
	 */
	public void setResponseCreated() {
		setResponseStatus(HttpResponseStatus.CREATED);
	}

	/**
	 * Sets the HTTP response status code to 204 - no content. Note, however, if
	 * a wrapped response is requested, then this method has no effect (as the
	 * body will contain content).
	 */
	public void setResponseNoContent() {
		setResponseStatus(HttpResponseStatus.NO_CONTENT);
	}

	/**
	 * Get the HTTP Response Status.
	 * 
	 * @return
	 */
	public HttpResponseStatus getResponseStatus() {
		return responseCode;
	}

	public String getContentType() {
		return getHeader(HttpHeaders.Names.CONTENT_TYPE);
	}

	public void setContentType(final String contentType) {
		final List<String> list = headers.get(HttpHeaders.Names.CONTENT_TYPE);

		if ((list != null) && !list.isEmpty()) {
			list.clear();
			list.add(contentType);
		} else if (list == null) {
			addHeader(HttpHeaders.Names.CONTENT_TYPE, contentType);
		}
	}

	/**
	 * Send a redirect response.
	 * 
	 * TODO With HTTP 1.1 we should use 303/307 (
	 * {@link HttpResponseStatus#SEE_OTHER} and
	 * {@link HttpResponseStatus#TEMPORARY_REDIRECT} )
	 * 
	 * @param location
	 *            URL of redirect
	 * @param permanent
	 *            if true, this will be a permanent redirect (301) else just a
	 *            redirect 302 (Found).
	 */
	public void redirect(String location, boolean permanent) {
		// set response code
		if (permanent) {
			responseCode = HttpResponseStatus.MOVED_PERMANENTLY;
		} else {
			responseCode = HttpResponseStatus.FOUND;
		}
		// add location header
		addHeader(ResponseHeader.LOCATION.getHeader(), location);
	}

	public boolean isSerialized() {
		return isSerialized;
	}

	public void setIsSerialized(final boolean value) {
		this.isSerialized = value;
	}

	public void noSerialization() {
		setIsSerialized(false);
	}

	public void useSerialization() {
		setIsSerialized(true);
	}

	public Throwable getException() {
		return exception;
	}

	public boolean hasException() {
		return (getException() != null);
	}

	public void setException(final Throwable exception) {
		this.exception = exception;
	}

}
