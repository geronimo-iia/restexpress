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
package org.intelligentsia.commons.http.status;

/**
 * HttpResponseStandardStatus enumerate standard {@link HttpResponseStatus}
 * which are define by a code and a reason phrase.
 * 
 * @see idea based on netty (netty.io, see notice fomr more information).
 * 
 * @author <a href="mailto:jguibert@intelligents-ia.com" >Jerome Guibert</a>
 */
public enum HttpResponseStandardStatus implements HttpResponseStatus {

	/**
	 * 100 Continue
	 */
	CONTINUE(HttpResponseStandardStatusCode.CONTINUE, "Continue"),

	/**
	 * 101 Switching Protocols
	 */
	SWITCHING_PROTOCOLS(HttpResponseStandardStatusCode.SWITCHING_PROTOCOLS, "Switching Protocols"),

	/**
	 * 102 Processing (WebDAV, RFC2518)
	 */
	PROCESSING(HttpResponseStandardStatusCode.PROCESSING, "Processing"),

	/**
	 * 200 OK
	 */
	OK(HttpResponseStandardStatusCode.OK, "OK"),

	/**
	 * 201 Created
	 */
	CREATED(HttpResponseStandardStatusCode.CREATED, "Created"),

	/**
	 * 202 Accepted
	 */
	ACCEPTED(HttpResponseStandardStatusCode.ACCEPTED, "Accepted"),

	/**
	 * 203 Non-Authoritative Information (since HTTP/1.1)
	 */
	NON_AUTHORITATIVE_INFORMATION(HttpResponseStandardStatusCode.NON_AUTHORITATIVE_INFORMATION, "Non-Authoritative Information"),

	/**
	 * 204 No Content
	 */
	NO_CONTENT(HttpResponseStandardStatusCode.NO_CONTENT, "No Content"),

	/**
	 * 205 Reset Content
	 */
	RESET_CONTENT(HttpResponseStandardStatusCode.RESET_CONTENT, "Reset Content"),

	/**
	 * 206 Partial Content
	 */
	PARTIAL_CONTENT(HttpResponseStandardStatusCode.PARTIAL_CONTENT, "Partial Content"),

	/**
	 * 207 Multi-Status (WebDAV, RFC2518)
	 */
	MULTI_STATUS(HttpResponseStandardStatusCode.MULTI_STATUS, "Multi-Status"),

	/**
	 * 300 Multiple Choices
	 */
	MULTIPLE_CHOICES(HttpResponseStandardStatusCode.MULTIPLE_CHOICES, "Multiple Choices"),

	/**
	 * 301 Moved Permanently
	 */
	MOVED_PERMANENTLY(HttpResponseStandardStatusCode.MOVED_PERMANENTLY, "Moved Permanently"),

	/**
	 * 302 Found
	 */
	FOUND(HttpResponseStandardStatusCode.FOUND, "Found"),

	/**
	 * 303 See Other (since HTTP/1.1)
	 */
	SEE_OTHER(HttpResponseStandardStatusCode.SEE_OTHER, "See Other"),

	/**
	 * 304 Not Modified
	 */
	NOT_MODIFIED(HttpResponseStandardStatusCode.NOT_MODIFIED, "Not Modified"),

	/**
	 * 305 Use Proxy (since HTTP/1.1)
	 */
	USE_PROXY(HttpResponseStandardStatusCode.USE_PROXY, "Use Proxy"),

	/**
	 * 307 Temporary Redirect (since HTTP/1.1)
	 */
	TEMPORARY_REDIRECT(HttpResponseStandardStatusCode.TEMPORARY_REDIRECT, "Temporary Redirect"),

	/**
	 * 400 Bad Request
	 */
	BAD_REQUEST(HttpResponseStandardStatusCode.BAD_REQUEST, "Bad Request"),

	/**
	 * 401 Unauthorized
	 */
	UNAUTHORIZED(HttpResponseStandardStatusCode.UNAUTHORIZED, "Unauthorized"),

	/**
	 * 402 Payment Required
	 */
	PAYMENT_REQUIRED(HttpResponseStandardStatusCode.PAYMENT_REQUIRED, "Payment Required"),

	/**
	 * 403 Forbidden
	 */
	FORBIDDEN(HttpResponseStandardStatusCode.FORBIDDEN, "Forbidden"),

	/**
	 * 404 Not Found
	 */
	NOT_FOUND(HttpResponseStandardStatusCode.NOT_FOUND, "Not Found"),

	/**
	 * 405 Method Not Allowed
	 */
	METHOD_NOT_ALLOWED(HttpResponseStandardStatusCode.METHOD_NOT_ALLOWED, "Method Not Allowed"),

	/**
	 * 406 Not Acceptable
	 */
	NOT_ACCEPTABLE(HttpResponseStandardStatusCode.NOT_ACCEPTABLE, "Not Acceptable"),

	/**
	 * 407 Proxy Authentication Required
	 */
	PROXY_AUTHENTICATION_REQUIRED(HttpResponseStandardStatusCode.PROXY_AUTHENTICATION_REQUIRED, "Proxy Authentication Required"),

	/**
	 * 408 Request Timeout
	 */
	REQUEST_TIMEOUT(HttpResponseStandardStatusCode.REQUEST_TIMEOUT, "Request Timeout"),

	/**
	 * 409 Conflict
	 */
	CONFLICT(HttpResponseStandardStatusCode.CONFLICT, "Conflict"),

	/**
	 * 410 Gone
	 */
	GONE(HttpResponseStandardStatusCode.GONE, "Gone"),

	/**
	 * 411 Length Required
	 */
	LENGTH_REQUIRED(HttpResponseStandardStatusCode.LENGTH_REQUIRED, "Length Required"),

	/**
	 * 412 Precondition Failed
	 */
	PRECONDITION_FAILED(HttpResponseStandardStatusCode.PRECONDITION_FAILED, "Precondition Failed"),

	/**
	 * 413 Request Entity Too Large
	 */
	REQUEST_ENTITY_TOO_LARGE(HttpResponseStandardStatusCode.REQUEST_ENTITY_TOO_LARGE, "Request Entity Too Large"),

	/**
	 * 414 Request-URI Too Long
	 */
	REQUEST_URI_TOO_LONG(HttpResponseStandardStatusCode.REQUEST_URI_TOO_LONG, "Request-URI Too Long"),

	/**
	 * 415 Unsupported Media Type
	 */
	UNSUPPORTED_MEDIA_TYPE(HttpResponseStandardStatusCode.UNSUPPORTED_MEDIA_TYPE, "Unsupported Media Type"),

	/**
	 * 416 Requested Range Not Satisfiable
	 */
	REQUESTED_RANGE_NOT_SATISFIABLE(HttpResponseStandardStatusCode.REQUESTED_RANGE_NOT_SATISFIABLE, "Requested Range Not Satisfiable"),

	/**
	 * 417 Expectation Failed
	 */
	EXPECTATION_FAILED(HttpResponseStandardStatusCode.EXPECTATION_FAILED, "Expectation Failed"),

	/**
	 * 422 Unprocessable Entity (WebDAV, RFC4918)
	 */
	UNPROCESSABLE_ENTITY(HttpResponseStandardStatusCode.UNPROCESSABLE_ENTITY, "Unprocessable Entity"),

	/**
	 * 423 Locked (WebDAV, RFC4918)
	 */
	LOCKED(HttpResponseStandardStatusCode.LOCKED, "Locked"),

	/**
	 * 424 Failed Dependency (WebDAV, RFC4918)
	 */
	FAILED_DEPENDENCY(HttpResponseStandardStatusCode.FAILED_DEPENDENCY, "Failed Dependency"),

	/**
	 * 425 Unordered Collection (WebDAV, RFC3648)
	 */
	UNORDERED_COLLECTION(HttpResponseStandardStatusCode.UNORDERED_COLLECTION, "Unordered Collection"),

	/**
	 * 426 Upgrade Required (RFC2817)
	 */
	UPGRADE_REQUIRED(HttpResponseStandardStatusCode.UPGRADE_REQUIRED, "Upgrade Required"),

	/**
	 * 431 Request Header Fields Too Large (RFC6585)
	 */
	REQUEST_HEADER_FIELDS_TOO_LARGE(HttpResponseStandardStatusCode.REQUEST_HEADER_FIELDS_TOO_LARGE, "Request Header Fields Too Large"),

	/**
	 * 500 Internal Server Error
	 */
	INTERNAL_SERVER_ERROR(HttpResponseStandardStatusCode.INTERNAL_SERVER_ERROR, "Internal Server Error"),

	/**
	 * 501 Not Implemented
	 */
	NOT_IMPLEMENTED(HttpResponseStandardStatusCode.NOT_IMPLEMENTED, "Not Implemented"),

	/**
	 * 502 Bad Gateway
	 */
	BAD_GATEWAY(HttpResponseStandardStatusCode.BAD_GATEWAY, "Bad Gateway"),

	/**
	 * 503 Service Unavailable
	 */
	SERVICE_UNAVAILABLE(HttpResponseStandardStatusCode.SERVICE_UNAVAILABLE, "Service Unavailable"),

	/**
	 * 504 Gateway Timeout
	 */
	GATEWAY_TIMEOUT(HttpResponseStandardStatusCode.GATEWAY_TIMEOUT, "Gateway Timeout"),

	/**
	 * 505 HTTP Version Not Supported
	 */
	HTTP_VERSION_NOT_SUPPORTED(HttpResponseStandardStatusCode.HTTP_VERSION_NOT_SUPPORTED, "HTTP Version Not Supported"),

	/**
	 * 506 Variant Also Negotiates (RFC2295)
	 */
	VARIANT_ALSO_NEGOTIATES(HttpResponseStandardStatusCode.VARIANT_ALSO_NEGOTIATES, "Variant Also Negotiates"),

	/**
	 * 507 Insufficient Storage (WebDAV, RFC4918)
	 */
	INSUFFICIENT_STORAGE(HttpResponseStandardStatusCode.INSUFFICIENT_STORAGE, "Insufficient Storage"),

	/**
	 * 510 Not Extended (RFC2774)
	 */
	NOT_EXTENDED(HttpResponseStandardStatusCode.NOT_EXTENDED, "Not Extended");

	/**
	 * Standard {@link HttpResponseStatus}.
	 */
	private HttpResponseStatus status;

	/**
	 * Creates a new instance with the specified {@code code} and its
	 * {@code reasonPhrase}.
	 */
	private HttpResponseStandardStatus(final HttpResponseStandardStatusCode code, final String reasonPhrase) {
		status = new DefaultHttpResponseStatus(code, reasonPhrase);
	}

	/**
	 * @param code
	 * @return an {@link HttpResponseStatus} associated with HTTP error code.
	 */
	public static HttpResponseStatus valueOf(final HttpResponseStandardStatusCode code) {
		return valueOf(code.getCode());
	}

	/**
	 * @param code
	 * @return an {@link HttpResponseStatus} associated with HTTP error code
	 */
	public static HttpResponseStatus valueOf(final int code) {
		for (final HttpResponseStandardStatus httpResponseStandardStatus : HttpResponseStandardStatus.values()) {
			if (code == httpResponseStandardStatus.status.getCode()) {
				return httpResponseStandardStatus.status;
			}
		}
		String reasonPhrase;
		if (code < 100) {
			reasonPhrase = "Unknown Status (" + code + ")";
		} else if (code < 200) {
			reasonPhrase = "Informational (" + code + ")";
		} else if (code < 300) {
			reasonPhrase = "Successful (" + code + ")";
		} else if (code < 400) {
			reasonPhrase = "Redirection (" + code + ")";
		} else if (code < 500) {
			reasonPhrase = "Client Error (" + code + ")";
		} else if (code < 600) {
			reasonPhrase = "Server Error (" + code + ")";
		} else {
			reasonPhrase = "Unknown Status (" + code + ")";
		}
		return new DefaultHttpResponseStatus(code, reasonPhrase);
	}

	/**
	 * @return underlying {@link HttpResponseStatus}.
	 */
	public HttpResponseStatus getStatus() {
		return status;
	}

	@Override
	public int getCode() {
		return status.getCode();
	}

	@Override
	public String getReasonPhrase() {
		return status.getReasonPhrase();
	}
}
