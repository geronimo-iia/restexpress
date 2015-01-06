package org.intelligentsia.commons.http;

/**
 * {@link HttpHeader}.
 * 
 * @see http://en.wikipedia.org/wiki/List_of_HTTP_header_fields
 * @author <a href="mailto:jguibert@intelligents-ia.com" >Jerome Guibert</a>
 * 
 */
public enum HttpHeader {
	/**
	 * Accept header: Content-Types that are acceptable for the response. See
	 * {@link <a href="http://www.w3.org/Protocols/rfc2616/rfc2616-sec14.html#sec14.1">HTTP/1.1 documentation</a>}
	 * .
	 **/
	ACCEPT("Accept"),
	/**
	 * Accept-Charset header: Character sets that are acceptable. See
	 * {@link <a href="http://www.w3.org/Protocols/rfc2616/rfc2616-sec14.html#sec14.2">HTTP/1.1 documentation</a>}
	 * .
	 **/
	ACCEPT_CHARSET("Accept-Charset"),
	/**
	 * Accept-Datetime header: Acceptable version in time
	 **/
	ACCEPT_DATETIME("Accept-Datetime"),
	/**
	 * Accept-Encoding header: List of acceptable encodings. See HTTP
	 * compression. See
	 * {@link <a href="http://www.w3.org/Protocols/rfc2616/rfc2616-sec14.html#sec14.3">HTTP/1.1 documentation</a>}
	 * .
	 **/
	ACCEPT_ENCODING("Accept-Encoding"),
	/**
	 * Accept-Language header: List of acceptable human languages for response.
	 * See
	 * {@link <a href="http://www.w3.org/Protocols/rfc2616/rfc2616-sec14.html#sec14.4">HTTP/1.1 documentation</a>}
	 * .
	 **/
	ACCEPT_LANGUAGE("Accept-Language"),

	/**
	 * Accept-Ranges header: What partial content range types this server
	 * supports
	 **/
	ACCEPT_RANGES("Accept-Ranges"),
	/**
	 * Access-Control-Allow-Origin header: Specifying which web sites can
	 * participate in cross-origin resource sharing
	 **/
	ACCESS_CONTROL_ALLOW_ORIGIN("Access-Control-Allow-Origin"),

	/**
	 * Age header: The age the object has been in a proxy cache in seconds
	 **/
	AGE("Age"),
	/**
	 * Allow header: Valid actions for a specified resource. To be used for a
	 * 405 Method not allowed. See
	 * {@link <a href="http://www.w3.org/Protocols/rfc2616/rfc2616-sec14.html#sec14.7">HTTP/1.1 documentation</a>}
	 * .
	 **/
	ALLOW("Allow"),
	/**
	 * Authorization header: Authentication credentials for HTTP authentication.
	 * See
	 * {@link <a href="http://www.w3.org/Protocols/rfc2616/rfc2616-sec14.html#sec14.8">HTTP/1.1 documentation</a>}
	 * .
	 **/
	AUTHORIZATION("Authorization"),
	/**
	 * Cache-Control header: Tells all caching mechanisms from server to client
	 * whether they may cache this object. It is measured in seconds. See
	 * {@link <a href="http://www.w3.org/Protocols/rfc2616/rfc2616-sec14.html#sec14.9">HTTP/1.1 documentation</a>}
	 * .
	 **/
	CACHE_CONTROL("Cache-Control"),

	/**
	 * Connection header: Options that are desired for the connection[22]
	 **/
	CONNECTION("Connection"),

	/**
	 * Content-Disposition header: An opportunity to raise aFile Download"
	 * dialogue box for a known MIME type with binary format or suggest a
	 * filename for dynamic content. Quotes are necessary with special
	 * characters. See
	 * {@link <a href="http://tools.ietf.org/html/rfc2183">IETF RFC-2183</a>}.
	 **/
	CONTENT_DISPOSITION("Content-Disposition"),
	/**
	 * Content-Encoding header: The type of encoding used on the data. See HTTP
	 * compression. See
	 * {@link <a href="http://www.w3.org/Protocols/rfc2616/rfc2616-sec14.html#sec14.11">HTTP/1.1 documentation</a>}
	 * .
	 **/
	CONTENT_ENCODING("Content-Encoding"),
	/**
	 * See {@link <a href="http://tools.ietf.org/html/rfc2392">IETF RFC-2392</a>}
	 * .
	 */
	CONTENT_ID("Content-ID"),
	/**
	 * Content-Language header. See
	 * {@link <a href="http://www.w3.org/Protocols/rfc2616/rfc2616-sec14.html#sec14.12">HTTP/1.1 documentation</a>}
	 * .
	 **/
	CONTENT_LANGUAGE("Content-Language"),
	/**
	 * Content-Length header: The length of the response body in octets (8-bit
	 * bytes). See
	 * {@link <a href="http://www.w3.org/Protocols/rfc2616/rfc2616-sec14.html#sec14.13">HTTP/1.1 documentation</a>}
	 * .
	 **/
	CONTENT_LENGTH("Content-Length"),
	/**
	 * Content-Location header: An alternate location for the returned data. See
	 * {@link <a href="http://www.w3.org/Protocols/rfc2616/rfc2616-sec14.html#sec14.14">HTTP/1.1 documentation</a>}
	 * .
	 **/
	CONTENT_LOCATION("Content-Location"),
	/**
	 * Content-MD5 header: A Base64-encoded binary MD5 sum of the content of the
	 * request body
	 **/
	CONTENT_MD5("Content-MD5"),
	/**
	 * Content-Range header: Where in a full body message this partial message
	 * belongs
	 **/
	CONTENT_RANGE("Content-Range"),
	/**
	 * Content-Type header: The MIME type of this content. See
	 * {@link <a href="http://www.w3.org/Protocols/rfc2616/rfc2616-sec14.html#sec14.17">HTTP/1.1 documentation</a>}
	 * .
	 **/
	CONTENT_TYPE("Content-Type"),
	/**
	 * Cookie header: an HTTP cookie previously sent by the server with
	 * Set-Cookie (below). See
	 * {@link <a href="http://www.ietf.org/rfc/rfc2109.txt">IETF RFC 2109</a>}.
	 **/
	COOKIE("Cookie"),
	/**
	 * Date header: The date and time that the message was sent (inHTTP-date"
	 * format as defined by RFC 2616). See
	 * {@link <a href="http://www.w3.org/Protocols/rfc2616/rfc2616-sec14.html#sec14.18">HTTP/1.1 documentation</a>}
	 * .
	 **/
	DATE("Date"),
	/**
	 * ETag header: An identifier for a specific version of a resource, often a
	 * message digest. See
	 * {@link <a href="http://www.w3.org/Protocols/rfc2616/rfc2616-sec14.html#sec14.19">HTTP/1.1 documentation</a>}
	 * .
	 **/
	ETAG("ETag"),
	/**
	 * Expect header: Indicates that particular server behaviors are required by
	 * the client
	 **/
	EXPECT("Expect"),
	/**
	 * Expires header: Gives the date/time after which the response is
	 * considered stale. See
	 * {@link <a href="http://www.w3.org/Protocols/rfc2616/rfc2616-sec14.html#sec14.21">HTTP/1.1 documentation</a>}
	 * .
	 **/
	EXPIRES("Expires"),
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
	 * See
	 * {@link <a href="http://www.w3.org/Protocols/rfc2616/rfc2616-sec14.html#sec14.23">HTTP/1.1 documentation</a>}
	 * .
	 **/
	HOST("Host"),
	/**
	 * If-Match header: Only perform the action if the client supplied entity
	 * matches the same entity on the server. This is mainly for methods like
	 * PUT to only update a resource if it has not been modified since the user
	 * last updated it. See
	 * {@link <a href="http://www.w3.org/Protocols/rfc2616/rfc2616-sec14.html#sec14.24">HTTP/1.1 documentation</a>}
	 * .
	 **/
	IF_MATCH("If-Match"),
	/**
	 * If-Modified-Since header: Allows a 304 Not Modified to be returned if
	 * content is unchanged. See
	 * {@link <a href="http://www.w3.org/Protocols/rfc2616/rfc2616-sec14.html#sec14.25">HTTP/1.1 documentation</a>}
	 * .
	 **/
	IF_MODIFIED_SINCE("If-Modified-Since"),
	/**
	 * If-None-Match header: Allows a 304 Not Modified to be returned if content
	 * is unchanged, see HTTP ETag. See
	 * {@link <a href="http://www.w3.org/Protocols/rfc2616/rfc2616-sec14.html#sec14.26">HTTP/1.1 documentation</a>}
	 * .
	 **/
	IF_NONE_MATCH("If-None-Match"),
	/**
	 * If-Range header: If the entity is unchanged, send me the part(s) that I
	 * am missing; otherwise, send me the entire new entity
	 **/
	IF_RANGE("If-Range"),

	/**
	 * If-Unmodified-Since header: Only send the response if the entity has not
	 * been modified since a specific time. See
	 * {@link <a href="http://www.w3.org/Protocols/rfc2616/rfc2616-sec14.html#sec14.28">HTTP/1.1 documentation</a>}
	 * .
	 **/
	IF_UNMODIFIED_SINCE("If-Unmodified-Since"),

	/**
	 * Last-Modified header: The last modified date for the requested object
	 * (inHTTP-date" format as defined by RFC 2616). See
	 * {@link <a href="http://www.w3.org/Protocols/rfc2616/rfc2616-sec14.html#sec14.29">HTTP/1.1 documentation</a>}
	 * .
	 **/
	LAST_MODIFIED("Last-Modified"),
	/**
	 * Link header: Used to express a typed relationship with another resource,
	 * where the relation type is defined by RFC 5988. See
	 * {@link <a href="http://tools.ietf.org/html/rfc5988#page-6">Web Linking (IETF RFC-5988) documentation</a>}
	 * .
	 **/
	LINK("Link"),
	/**
	 * Location header: Used in redirection, or when a new resource has been
	 * created. See
	 * {@link <a href="http://www.w3.org/Protocols/rfc2616/rfc2616-sec14.html#sec14.30">HTTP/1.1 documentation</a>}
	 * .
	 **/
	LOCATION("Location"),
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
	 * Refresh header: Used in redirection, or when a new resource has been
	 * created. This refresh redirects after 5 seconds.
	 **/
	REFRESH("Refresh"),
	/**
	 * Retry-After header: If an entity is temporarily unavailable, this
	 * instructs the client to try again later. Value could be a specified
	 * period of time (in seconds) or a HTTP-date.[28] See
	 * {@link <a href="http://www.w3.org/Protocols/rfc2616/rfc2616-sec14.html#sec14.37">HTTP/1.1 documentation</a>}
	 * .
	 **/
	RETRY_AFTER("Retry-After"),
	/**
	 * Server header: A name for the server
	 **/
	SERVER("Server"),
	/**
	 * Set-Cookie header: An HTTP cookie. See
	 * {@link <a href="http://www.ietf.org/rfc/rfc2109.txt">IETF RFC 2109</a>}.
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
	 * TE header: The transfer encodings the user agent is willing to accept:
	 * the same values as for the response header Transfer-Encoding can be used,
	 * plus the trailers" value (related to thechunked" transfer method) to
	 * notify the server it expects to receive additional headers (the trailers)
	 * after the last, zero-sized, chunk.
	 **/
	TE("TE"),

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
	 * Upgrade header: Ask the server to upgrade to another protocol.
	 **/
	UPGRADE("Upgrade"),
	/**
	 * User-Agent header: The user agent string of the user agent. See
	 * {@link <a href="http://www.w3.org/Protocols/rfc2616/rfc2616-sec14.html#sec14.43">HTTP/1.1 documentation</a>}
	 * .
	 **/
	USER_AGENT("User-Agent"),
	/**
	 * Vary header: Tells downstream proxies how to match future request headers
	 * to decide whether the cached response can be used rather than requesting
	 * a fresh one from the origin server. See
	 * {@link <a href="http://www.w3.org/Protocols/rfc2616/rfc2616-sec14.html#sec14.44">HTTP/1.1 documentation</a>}
	 * .
	 **/
	VARY("Vary"),
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
	 * WWW-Authenticate header: Indicates the authentication scheme that should
	 * be used to access the requested entity. See
	 * {@link <a href="http://www.w3.org/Protocols/rfc2616/rfc2616-sec14.html#sec14.47">HTTP/1.1 documentation</a>}
	 * .
	 */
	WWW_AUTHENTICATE("WWW-Authenticate"),
	/**
	 *  
	 */
	X_AUTHENTICATED_PASSWORD("X-AuthenticatedPassword"),
	/**
	 * 
	 */
	X_AUTHENTICATED_USER("X-AuthenticatedUser");

	private String header;

	private HttpHeader(final String header) {
		this.header = header;
	}

	@Override
	public String toString() {
		return header;
	}
}
