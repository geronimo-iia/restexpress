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
 * HttpResponseStandardStatusCode enumerate standard HTTP error code.
 * 
 * @author <a href="mailto:jguibert@intelligents-ia.com" >Jerome Guibert</a>
 * 
 */
public enum HttpResponseStandardStatusCode {

	/**
	 * 100 Continue
	 */
	CONTINUE(100),

	/**
	 * 101 Switching Protocols
	 */
	SWITCHING_PROTOCOLS(101),

	/**
	 * 102 Processing (WebDAV, RFC2518)
	 */
	PROCESSING(102),

	/**
	 * 200 OK
	 */
	OK(200),

	/**
	 * 201 Created
	 */
	CREATED(201),

	/**
	 * 202 Accepted
	 */
	ACCEPTED(202),

	/**
	 * 203 Non-Authoritative Information (since HTTP/1.1)
	 */
	NON_AUTHORITATIVE_INFORMATION(203),

	/**
	 * 204 No Content
	 */
	NO_CONTENT(204),

	/**
	 * 205 Reset Content
	 */
	RESET_CONTENT(205),

	/**
	 * 206 Partial Content
	 */
	PARTIAL_CONTENT(206),

	/**
	 * 207 Multi-Status (WebDAV, RFC2518)
	 */
	MULTI_STATUS(207),

	/**
	 * 300 Multiple Choices
	 */
	MULTIPLE_CHOICES(300),

	/**
	 * 301 Moved Permanently
	 */
	MOVED_PERMANENTLY(301),

	/**
	 * 302 Found
	 */
	FOUND(302),

	/**
	 * 303 See Other (since HTTP/1.1)
	 */
	SEE_OTHER(303),

	/**
	 * 304 Not Modified
	 */
	NOT_MODIFIED(304),

	/**
	 * 305 Use Proxy (since HTTP/1.1)
	 */
	USE_PROXY(305),

	/**
	 * 307 Temporary Redirect (since HTTP/1.1)
	 */
	TEMPORARY_REDIRECT(307),

	/**
	 * 400 Bad Request
	 */
	BAD_REQUEST(400),

	/**
	 * 401 Unauthorized
	 */
	UNAUTHORIZED(401),

	/**
	 * 402 Payment Required
	 */
	PAYMENT_REQUIRED(402),

	/**
	 * 403 Forbidden
	 */
	FORBIDDEN(403),

	/**
	 * 404 Not Found
	 */
	NOT_FOUND(404),

	/**
	 * 405 Method Not Allowed
	 */
	METHOD_NOT_ALLOWED(405),

	/**
	 * 406 Not Acceptable
	 */
	NOT_ACCEPTABLE(406),

	/**
	 * 407 Proxy Authentication Required
	 */
	PROXY_AUTHENTICATION_REQUIRED(407),

	/**
	 * 408 Request Timeout
	 */
	REQUEST_TIMEOUT(408),

	/**
	 * 409 Conflict
	 */
	CONFLICT(409),

	/**
	 * 410 Gone
	 */
	GONE(410),

	/**
	 * 411 Length Required
	 */
	LENGTH_REQUIRED(411),

	/**
	 * 412 Precondition Failed
	 */
	PRECONDITION_FAILED(412),

	/**
	 * 413 Request Entity Too Large
	 */
	REQUEST_ENTITY_TOO_LARGE(413),

	/**
	 * 414 Request-URI Too Long
	 */
	REQUEST_URI_TOO_LONG(414),

	/**
	 * 415 Unsupported Media Type
	 */
	UNSUPPORTED_MEDIA_TYPE(415),

	/**
	 * 416 Requested Range Not Satisfiable
	 */
	REQUESTED_RANGE_NOT_SATISFIABLE(416),

	/**
	 * 417 Expectation Failed
	 */
	EXPECTATION_FAILED(417),

	/**
	 * 422 Unprocessable Entity (WebDAV, RFC4918)
	 */
	UNPROCESSABLE_ENTITY(422),

	/**
	 * 423 Locked (WebDAV, RFC4918)
	 */
	LOCKED(423),

	/**
	 * 424 Failed Dependency (WebDAV, RFC4918)
	 */
	FAILED_DEPENDENCY(424),

	/**
	 * 425 Unordered Collection (WebDAV, RFC3648)
	 */
	UNORDERED_COLLECTION(425),

	/**
	 * 426 Upgrade Required (RFC2817)
	 */
	UPGRADE_REQUIRED(426),

	/**
	 * 431 Request Header Fields Too Large (RFC6585)
	 */
	REQUEST_HEADER_FIELDS_TOO_LARGE(431),

	/**
	 * 500 Internal Server Error
	 */
	INTERNAL_SERVER_ERROR(500),

	/**
	 * 501 Not Implemented
	 */
	NOT_IMPLEMENTED(501),

	/**
	 * 502 Bad Gateway
	 */
	BAD_GATEWAY(502),

	/**
	 * 503 Service Unavailable
	 */
	SERVICE_UNAVAILABLE(503),

	/**
	 * 504 Gateway Timeout
	 */
	GATEWAY_TIMEOUT(504),

	/**
	 * 505 HTTP Version Not Supported
	 */
	HTTP_VERSION_NOT_SUPPORTED(505),

	/**
	 * 506 Variant Also Negotiates (RFC2295)
	 */
	VARIANT_ALSO_NEGOTIATES(506),

	/**
	 * 507 Insufficient Storage (WebDAV, RFC4918)
	 */
	INSUFFICIENT_STORAGE(507),

	/**
	 * 510 Not Extended (RFC2774)
	 */
	NOT_EXTENDED(510)

	;

	private final int code;

	/**
	 * Build a new instance.
	 * 
	 * @param code
	 */
	private HttpResponseStandardStatusCode(final int code) {
		this.code = code;
	}

	/**
	 * @return http code
	 */
	public int getCode() {
		return code;
	}

	/**
	 * @param code
	 * @return associated {@link HttpResponseStandardStatusCode} with specified
	 *         code
	 * @throws IllegalArgumentException
	 *             if no HttpResponseStandardStatusCode match with specified
	 *             code.
	 */
	public static HttpResponseStandardStatusCode valueOf(final int code) throws IllegalArgumentException {
		for (final HttpResponseStandardStatusCode statusCode : HttpResponseStandardStatusCode.values()) {
			if (code == statusCode.getCode()) {
				return statusCode;
			}
		}
		throw new IllegalArgumentException("No HttpResponseStandardStatusCode match code " + code);
	}
}
