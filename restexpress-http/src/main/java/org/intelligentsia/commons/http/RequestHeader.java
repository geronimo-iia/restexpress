/**
 * ====
 *            Licensed to the Apache Software Foundation (ASF) under one
 *            or more contributor license agreements.  See the NOTICE file
 *            distributed with this work for additional information
 *            regarding copyright ownership.  The ASF licenses this file
 *            to you under the Apache License, Version 2.0 (the
 *            "License"); you may not use this file except in compliance
 *            with the License.  You may obtain a copy of the License at
 *
 *              http://www.apache.org/licenses/LICENSE-2.0
 *
 *            Unless required by applicable law or agreed to in writing,
 *            software distributed under the License is distributed on an
 *            "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *            KIND, either express or implied.  See the License for the
 *            specific language governing permissions and limitations
 *            under the License.
 *
 * ====
 *
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
package org.intelligentsia.commons.http;

/**
 * RequestHeader.
 * 
 * @see http://en.wikipedia.org/wiki/List_of_HTTP_header_fields
 * @author <a href="mailto:jguibert@intelligents-ia.com" >Jerome Guibert</a>
 * 
 */
public enum RequestHeader {

	/**
	 * Accept header: Content-Types that are acceptable for the response
	 **/
	ACCEPT("Accept"),
	/**
	 * Accept-Charset header: Character sets that are acceptable
	 **/
	ACCEPT_CHARSET("Accept-Charset"),
	/**
	 * Accept-Encoding header: List of acceptable encodings. See HTTP
	 * compression.
	 **/
	ACCEPT_ENCODING("Accept-Encoding"),
	/**
	 * Accept-Language header: List of acceptable human languages for response
	 **/
	ACCEPT_LANGUAGE("Accept-Language"),
	/**
	 * Accept-Datetime header: Acceptable version in time
	 **/
	ACCEPT_DATETIME("Accept-Datetime"),
	/**
	 * Authorization header: Authentication credentials for HTTP authentication
	 **/
	AUTHORIZATION("Authorization"),
	/**
	 * Cache-Control header: Used to specify directives that MUST be obeyed by
	 * all caching mechanisms along the request/response chain
	 **/
	CACHE_CONTROL("Cache-Control"),
	/**
	 * Connection header: What type of connection the user-agent would prefer
	 **/
	CONNECTION("Connection"),
	/**
	 * Cookie header: an HTTP cookie previously sent by the server with
	 * Set-Cookie (below)
	 **/
	COOKIE("Cookie"),
	/**
	 * Content-Length header: The length of the request body in octets (8-bit
	 * bytes)
	 **/
	CONTENT_LENGTH("Content-Length"),
	/**
	 * Content-MD5 header: A Base64-encoded binary MD5 sum of the content of the
	 * request body
	 **/
	CONTENT_MD5("Content-MD5"),
	/**
	 * Content-Type header: The MIME type of the body of the request (used with
	 * POST and PUT requests)
	 **/
	CONTENT_TYPE("Content-Type"),
	/**
	 * Date header: The date and time that the message was sent (inHTTP-date"
	 * format as defined by RFC 2616)
	 **/
	DATE("Date"),
	/**
	 * Expect header: Indicates that particular server behaviors are required by
	 * the client
	 **/
	EXPECT("Expect"),
	/**
	 * From header: The email address of the user making the request
	 **/
	FROM("From"),
	/**
	 * Host header: The domain name of the server (for virtual hosting), and the
	 * TCP port number on which the server is listening. The port number may be
	 * omitted if the port is the standard port for the service
	 * requested.[8]Mandatory since HTTP/1.1. Although domain name are specified
	 * as case-insensitive,[9][10] it is not specified whether the contents of
	 * the Host field should be interpreted in a case-insensitive manner[11] and
	 * in practice some implementations of virtual hosting interpret the
	 * contents of the Host field in a case-sensitive manner.[citation needed]
	 **/
	HOST("Host"),
	/**
	 * If-Match header: Only perform the action if the client supplied entity
	 * matches the same entity on the server. This is mainly for methods like
	 * PUT to only update a resource if it has not been modified since the user
	 * last updated it.
	 **/
	IF_MATCH("If-Match"),
	/**
	 * If-Modified-Since header: Allows a 304 Not Modified to be returned if
	 * content is unchanged
	 **/
	IF_MODIFIED_SINCE("If-Modified-Since"),
	/**
	 * If-None-Match header: Allows a 304 Not Modified to be returned if content
	 * is unchanged, see HTTP ETag
	 **/
	IF_NONE_MATCH("If-None-Match"),
	/**
	 * If-Range header: If the entity is unchanged, send me the part(s) that I
	 * am missing; otherwise, send me the entire new entity
	 **/
	IF_RANGE("If-Range"),
	/**
	 * If-Unmodified-Since header: Only send the response if the entity has not
	 * been modified since a specific time.
	 **/
	IF_UNMODIFIED_SINCE("If-Unmodified-Since"),
	/**
	 * Max-Forwards header: Limit the number of times the message can be
	 * forwarded through proxies or gateways.
	 **/
	MAX_FORWARDS("Max-Forwards"),
	/**
	 * Origin header: Initiates a request for cross-origin resource sharing
	 * (asks server for an 'Access-Control-Allow-Origin' response header) .
	 **/
	ORIGIN("Origin"),
	/**
	 * Pragma header: Implementation-specific headers that may have various
	 * effects anywhere along the request-response chain.
	 **/
	PRAGMA("Pragma"),
	/**
	 * Proxy-Authorization header: Authorization credentials for connecting to a
	 * proxy.
	 **/
	PROXY_AUTHORIZATION("Proxy-Authorization"),
	/**
	 * Range header: Request only part of an entity. Bytes are numbered from 0.
	 **/
	RANGE("Range"),
	/**
	 * Referer header: This is the address of the previous web page from which a
	 * link to the currently requested page was followed. (The word “referrer”
	 * has been misspelled in the RFC as well as in most implementations to the
	 * point that it has become standard usage and is considered correct
	 * terminology)
	 **/
	REFERER("Referer"),
	/**
	 * TE header: The transfer encodings the user agent is willing to accept:
	 * the same values as for the response header Transfer-Encoding can be used,
	 * plus the trailers" value (related to thechunked" transfer method) to
	 * notify the server it expects to receive additional headers (the trailers)
	 * after the last, zero-sized, chunk.
	 **/
	TE("TE"),
	/**
	 * User-Agent header: The user agent string of the user agent
	 **/
	USER_AGENT("User-Agent"),
	/**
	 * Upgrade header: Ask the server to upgrade to another protocol.
	 **/
	UPGRADE("Upgrade"),
	/**
	 * Via header: Informs the server of proxies through which the request was
	 * sent.
	 **/
	VIA("Via"),
	/**
	 * Warning header: A general warning about possible problems with the entity
	 * body.
	 **/
	WARNING("Warning"),
	/**
	 * 
	 */
	X_AUTHENTICATED_USER("X-AuthenticatedUser"),
	/**
	 *  
	 */
	X_AUTHENTICATED_PASSWORD("X-AuthenticatedPassword");

	private String header;

	private RequestHeader(final String header) {
		this.header = header;
	}

	public String getHeader() {
		return header;
	}
}
