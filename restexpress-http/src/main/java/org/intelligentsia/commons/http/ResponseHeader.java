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
 * ResponseHeader.
 * 
 * @author <a href="mailto:jguibert@intelligents-ia.com" >Jerome Guibert</a>
 * 
 */
public enum ResponseHeader {
	/**
	 * Access-Control-Allow-Origin header: Specifying which web sites can
	 * participate in cross-origin resource sharing
	 **/
	ACCESS_CONTROL_ALLOW_ORIGIN("Access-Control-Allow-Origin"),
	/**
	 * Accept-Ranges header: What partial content range types this server
	 * supports
	 **/
	ACCEPT_RANGES("Accept-Ranges"),
	/**
	 * Age header: The age the object has been in a proxy cache in seconds
	 **/
	AGE("Age"),
	/**
	 * Allow header: Valid actions for a specified resource. To be used for a
	 * 405 Method not allowed
	 **/
	ALLOW("Allow"),
	/**
	 * Cache-Control header: Tells all caching mechanisms from server to client
	 * whether they may cache this object. It is measured in seconds
	 **/
	CACHE_CONTROL("Cache-Control"),
	/**
	 * Connection header: Options that are desired for the connection[22]
	 **/
	CONNECTION("Connection"),
	/**
	 * Content-Encoding header: The type of encoding used on the data. See HTTP
	 * compression.
	 **/
	CONTENT_ENCODING("Content-Encoding"),
	/**
	 * Content-Language header: The language the content is in
	 **/
	CONTENT_LANGUAGE("Content-Language"),
	/**
	 * Content-Length header: The length of the response body in octets (8-bit
	 * bytes)
	 **/
	CONTENT_LENGTH("Content-Length"),
	/**
	 * Content-Location header: An alternate location for the returned data
	 **/
	CONTENT_LOCATION("Content-Location"),
	/**
	 * Content-MD5 header: A Base64-encoded binary MD5 sum of the content of the
	 * response
	 **/
	CONTENT_MD5("Content-MD5"),
	/**
	 * Content-Disposition header: An opportunity to raise aFile Download"
	 * dialogue box for a known MIME type with binary format or suggest a
	 * filename for dynamic content. Quotes are necessary with special
	 * characters.
	 **/
	CONTENT_DISPOSITION("Content-Disposition"),
	/**
	 * Content-Range header: Where in a full body message this partial message
	 * belongs
	 **/
	CONTENT_RANGE("Content-Range"),
	/**
	 * Content-Type header: The MIME type of this content
	 **/
	CONTENT_TYPE("Content-Type"),
	/**
	 * Date header: The date and time that the message was sent (inHTTP-date"
	 * format as defined by RFC 2616)
	 **/
	DATE("Date"),
	/**
	 * ETag header: An identifier for a specific version of a resource, often a
	 * message digest
	 **/
	ETAG("ETag"),
	/**
	 * Expires header: Gives the date/time after which the response is
	 * considered stale
	 **/
	EXPIRES("Expires"),
	/**
	 * Last-Modified header: The last modified date for the requested object
	 * (inHTTP-date" format as defined by RFC 2616)
	 **/
	LAST_MODIFIED("Last-Modified"),
	/**
	 * Link header: Used to express a typed relationship with another resource,
	 * where the relation type is defined by RFC 5988
	 **/
	LINK("Link"),
	/**
	 * Location header: Used in redirection, or when a new resource has been
	 * created.
	 **/
	LOCATION("Location"),
	/**
	 * P3P header: This header is supposed to set P3P policy, in the form
	 * ofP3P:CP="your_compact_policy". However, P3P did not take off,[27] most
	 * browsers have never fully implemented it, a lot of websites set this
	 * header with fake policy text, that was enough to fool browsers the
	 * existence of P3P policy and grant permissions for third party cookies.
	 **/
	P3P("P3P"),
	/**
	 * Pragma header: Implementation-specific headers that may have various
	 * effects anywhere along the request-response chain.
	 **/
	PRAGMA("Pragma"),
	/**
	 * Proxy-Authenticate header: Request authentication to access the proxy.
	 **/
	PROXY_AUTHENTICATE("Proxy-Authenticate"),
	/**
	 * Refresh header: Used in redirection, or when a new resource has been
	 * created. This refresh redirects after 5 seconds.
	 **/
	REFRESH("Refresh"),
	/**
	 * Retry-After header: If an entity is temporarily unavailable, this
	 * instructs the client to try again later. Value could be a specified
	 * period of time (in seconds) or a HTTP-date.[28]
	 **/
	RETRY_AFTER("Retry-After"),
	/**
	 * Server header: A name for the server
	 **/
	SERVER("Server"),
	/**
	 * Set-Cookie header: An HTTP cookie
	 **/
	SET_COOKIE("Set-Cookie"),
	/**
	 * Status header: The HTTP status of the response
	 **/
	STATUS("Status"),
	/**
	 * Strict-Transport-Security header: A HSTS Policy informing the HTTP client
	 * how long to cache the HTTPS only policy and whether this applies to
	 * subdomains.
	 **/
	STRICT_TRANSPORT_SECURITY("Strict-Transport-Security"),
	/**
	 * Trailer header: The Trailer general field value indicates that the given
	 * set of header fields is present in the trailer of a message encoded with
	 * chunked transfer-coding.
	 **/
	TRAILER("Trailer"),
	/**
	 * Transfer-Encoding header: The form of encoding used to safely transfer
	 * the entity to the user. Currently defined methods are: chunked, compress,
	 * deflate, gzip, identity.
	 **/
	TRANSFER_ENCODING("Transfer-Encoding"),
	/**
	 * Upgrade header: Ask the client to upgrade to another protocol.
	 **/
	UPGRADE("Upgrade"),
	/**
	 * Vary header: Tells downstream proxies how to match future request headers
	 * to decide whether the cached response can be used rather than requesting
	 * a fresh one from the origin server.
	 **/
	VARY("Vary"),
	/**
	 * Via header: Informs the client of proxies through which the response was
	 * sent.
	 **/
	VIA("Via"),
	/**
	 * Warning header: A general warning about possible problems with the entity
	 * body.
	 **/
	WARNING("Warning"),
	/**
	 * WWW-Authenticate header: Indicates the authentication scheme that should
	 * be used to access the requested entity.
	 */
	WWW_AUTHENTICATE("WWW-Authenticate");

	private String header;

	private ResponseHeader(final String header) {
		this.header = header;
	}

	public String getHeader() {
		return header;
	}
}
