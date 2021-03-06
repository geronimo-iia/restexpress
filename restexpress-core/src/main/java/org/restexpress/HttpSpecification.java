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

import javax.ws.rs.core.Response.StatusType;

import org.restexpress.http.HttpHeader;
import org.restexpress.http.HttpSpecificationException;
import org.restexpress.http.HttpStatus;

/**
 * Verifies the response contents prior to writing it to the output stream to
 * ensure that it conforms to the HTTP 1.1. specification.
 * 
 * @author toddf
 * @since Mar 3, 2011
 */
public enum HttpSpecification {
	;
	// SECTION: SPECIFICATION ENFORCEMENT

	public static void enforce(final Response response) {
		final int status = response.getStatus();

		if (is1xx(status)) {
			enforce1xx(response);
		} else {
			switch (status) {
			case 204:
				enforce204(response);
				break;
			case 304:
				enforce304(response);
				break;
			case 405:
				enforce405(response);
			default:
				break;
			}
		}
	}

	// SECTION: SPECIFICATION TESTING

	/**
	 * There must be a Content-Type, except when the Status is 1xx, 204 or 304,
	 * in which case there must be none given.
	 * 
	 * @param response
	 * @return
	 */
	public static boolean isContentTypeAllowed(final Response response) {
		return isContentAllowed(response);
	}

	/**
	 * There must not be a Content-Length header when the Status is 1xx, 204 or
	 * 304.
	 * 
	 * @param response
	 * @return
	 */
	public static boolean isContentLengthAllowed(final Response response) {
		return isContentAllowed(response);
	}

	public static boolean isContentAllowed(final Response response) {
		final StatusType status = response.getStatusInfo();
		return !(HttpStatus.NO_CONTENT.equals(status) || HttpStatus.NOT_MODIFIED.equals(status) || is1xx(status.getStatusCode()));
	}

	// SECTION: UTILITY - PRIVATE

	private static boolean is1xx(final int status) {
		return ((100 <= status) && (status <= 199));
	}

	/**
	 * Responses 1xx, 204 (No Content) and 304 (Not Modified) must not return a
	 * response body.
	 * 
	 * @param response
	 */
	private static void enforce1xx(final Response response) {
		ensureNoBody(response);
		ensureNoContentType(response);
		ensureNoContentLength(response);
	}

	/**
	 * Responses 1xx, 204 (No Content) and 304 (Not Modified) must not return a
	 * response body.
	 * 
	 * @param response
	 */
	private static void enforce204(final Response response) {
		ensureNoBody(response);
		ensureNoContentType(response);
		ensureNoContentLength(response);
	}

	/**
	 * Responses 1xx, 204 (No Content) and 304 (Not Modified) must not return a
	 * response body.
	 * 
	 * @param response
	 */
	private static void enforce304(final Response response) {
		ensureNoBody(response);
		ensureNoContentType(response);
		ensureNoContentLength(response);
	}

	private static void enforce405(final Response response) {
		ensureAllowHeader(response);
	}

	private static void ensureNoBody(final Response response) {
		if (response.hasEntity()) {
			throw new HttpSpecificationException("HTTP 1.1 specification: must not contain response body with status: " + response.getStatusInfo());
		}
	}

	/**
	 * @param response
	 */
	private static void ensureNoContentLength(final Response response) {
		if (response.getHeader(HttpHeader.CONTENT_LENGTH) != null) {
			throw new HttpSpecificationException("HTTP 1.1 specification: must not contain Content-Length header for status: " + response.getStatusInfo());
		}
	}

	/**
	 * @param response
	 */
	private static void ensureNoContentType(final Response response) {
		if (response.getHeader(HttpHeader.CONTENT_TYPE) != null) {
			throw new HttpSpecificationException("HTTP 1.1 specification: must not contain Content-Type header for status: " + response.getStatusInfo());
		}
	}

	private static void ensureAllowHeader(final Response response) {
		if (response.getHeader(HttpHeader.ALLOW) == null) {
			throw new HttpSpecificationException("HTTP 1.1 specification: must contain Allow header for status: " + response.getStatusInfo());
		}
	}
}
