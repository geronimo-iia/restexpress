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

import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.URLDecoder;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;

import org.intelligentsia.commons.http.exception.BadRequestException;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBufferInputStream;
import org.jboss.netty.handler.codec.http.HttpHeaders;
import org.jboss.netty.handler.codec.http.HttpMethod;
import org.jboss.netty.handler.codec.http.HttpRequest;
import org.jboss.netty.handler.codec.http.HttpVersion;
import org.jboss.netty.handler.codec.http.QueryStringDecoder;
import org.restexpress.domain.CharacterSet;
import org.restexpress.response.ResponseProcessorSettingResolver;
import org.restexpress.route.Route;
import org.restexpress.route.RouteResolver;
import org.restexpress.url.QueryStringParser;

/**
 * {@link Request}.
 * 
 * @author toddf
 * @since Nov 20, 2009
 */
public class Request {
	private static AtomicLong nextCorrelationId = new AtomicLong(0);

	private final HttpRequest httpRequest;
	private final HttpVersion httpVersion;
	private final RouteResolver routeResolver;
	private final ResponseProcessorSettingResolver responseProcessorSettingResolver;
	private final Map<String, String> queryStringMap;
	private final HttpMethod effectiveHttpMethod;
	private final String correlationId;
	private final InetSocketAddress remoteAddress;
	private Route resolvedRoute;
	private Map<String, Object> attachments;

	/**
	 * Build a new instance of {@link Request} for testing purpose.
	 * 
	 * @param request
	 * @param routeResolver
	 */
	public Request(final HttpRequest httpRequest, final RouteResolver routeResolver) {
		this(httpRequest, routeResolver, null, null);
	}

	public Request(final HttpRequest httpRequest, final RouteResolver routeResolver, final ResponseProcessorSettingResolver responseProcessorSettingResolver) {
		this(httpRequest, routeResolver, responseProcessorSettingResolver, null);
	}

	/**
	 * Build a new instance of {@link Request}.
	 * 
	 * @param httpRequest
	 *            {@link HttpRequest}.
	 * @param routeResolver
	 *            {@link RouteResolver}.
	 * @param responseProcessorSettingResolver
	 *            {@link ResponseProcessorSettingResolver}.
	 * @param remoteAddress
	 *            {@link InetSocketAddress}
	 */
	public Request(final HttpRequest httpRequest, final RouteResolver routeResolver, final ResponseProcessorSettingResolver responseProcessorSettingResolver, InetSocketAddress remoteAddress) {
		super();
		this.httpRequest = httpRequest;
		this.httpVersion = httpRequest.getProtocolVersion();
		this.routeResolver = routeResolver;
		this.responseProcessorSettingResolver = responseProcessorSettingResolver;
		this.queryStringMap = parseQueryString(httpRequest);
		this.effectiveHttpMethod = determineEffectiveHttpMethod(httpRequest);
		this.correlationId = createCorrelationId();
		this.remoteAddress = remoteAddress;
	}

	/**
	 * Return the Correlation ID for this request. The Correlation ID is unique
	 * for each request within this VM instance. Restarting the VM will reset
	 * the correlation ID to zero. It is not a GUID. It is useful, however, in
	 * correlating events in the pipeline (e.g. timing, etc.).
	 */
	public String getCorrelationId() {
		return correlationId;
	}

	/**
	 * Return the HTTP method of the request.
	 * 
	 * @return HttpMethod of the request.
	 */
	public HttpMethod getHttpMethod() {
		return httpRequest.getMethod();
	}

	/**
	 * Return the requested HTTP method of the request, whether via the
	 * request's HTTP method or via a query parameter (e.g. "_Method=").
	 * 
	 * @return the requested HttpMethod.
	 */
	public HttpMethod getEffectiveHttpMethod() {
		return effectiveHttpMethod;
	}

	/**
	 * @return {@link Boolean#TRUE} if method is {@link HttpMethod#GET}
	 */
	public boolean isMethodGet() {
		return getEffectiveHttpMethod().equals(HttpMethod.GET);
	}

	/**
	 * @return {@link Boolean#TRUE} if method is {@link HttpMethod#DELETE}
	 */
	public boolean isMethodDelete() {
		return getEffectiveHttpMethod().equals(HttpMethod.DELETE);
	}

	/**
	 * @return {@link Boolean#TRUE} if method is {@link HttpMethod#POST}
	 */
	public boolean isMethodPost() {
		return getEffectiveHttpMethod().equals(HttpMethod.POST);
	}

	/**
	 * @return {@link Boolean#TRUE} if method is {@link HttpMethod#PUT}
	 */
	public boolean isMethodPut() {
		return getEffectiveHttpMethod().equals(HttpMethod.PUT);
	}

	/**
	 * @return {@link ChannelBuffer} of request body.
	 */
	public ChannelBuffer getBody() {
		return httpRequest.getContent();
	}

	/**
	 * Attempts to deserialize the request body into an instance of the given
	 * type.
	 * 
	 * @param type
	 *            the resulting type
	 * @return an instance of the requested type.
	 * @throws BadRequestException
	 *             if the deserialization fails.
	 */
	public <T> T getBodyAs(final Class<T> type) throws BadRequestException {
		return responseProcessorSettingResolver.resolve(this).deserialize(this, type);
	}

	/**
	 * Attempts to deserialize the request body into an instance of the given
	 * type. If the serialization process returns null, throws
	 * BadRequestException using the message.
	 * 
	 * @param type
	 *            the resulting type.
	 * @param message
	 *            the message for the BadRequestException if serialization
	 *            returns null.
	 * @return an instance of the requested type.
	 * @throws BadRequestException
	 *             if serialization fails.
	 */
	public <T> T getBodyAs(final Class<T> type, final String message) throws BadRequestException {
		final T instance = getBodyAs(type);
		if (instance == null) {
			throw new BadRequestException(message);
		}
		return instance;
	}

	/**
	 * Returns the request body as an InputStream.
	 * 
	 * @return an InputStream
	 */
	public InputStream getBodyAsStream() {
		return new ChannelBufferInputStream(getBody());
	}

	/**
	 * Accesses the request body as a ByteBuffer. It is equivalent to calling
	 * getBody().toByteBuffer().
	 * 
	 * @return a ByteBuffer.
	 */
	public ByteBuffer getBodyAsByteBuffer() {
		return getBody().toByteBuffer();
	}

	/**
	 * Returns the byte array underlying the Netty ChannelBuffer for this
	 * request. However, if the ChannelBuffer returns false to hasArray(), this
	 * method returns null.
	 * 
	 * @return an array of byte, or null, if the ChannelBuffer is not backed by
	 *         a byte array.
	 */
	public byte[] getBodyAsBytes() {
		return (getBody().hasArray() ? getBody().array() : null);
	}

	/**
	 * Returns the body as a Map of name/value pairs from a url-form-encoded
	 * form submission. The values returned are URL-decoded.
	 * 
	 * @return
	 */
	public Map<String, List<String>> getBodyFromUrlFormEncoded() {
		return getBodyFromUrlFormEncoded(true);
	}

	/**
	 * Returns the body as a Map of name/value pairs from a url-form-encoded
	 * form submission. The values returned are URL-decoded depending on the
	 * value of shouldDecode.
	 * 
	 * @param shouldDecode
	 *            true if the returned values should be URL-decoded
	 * @return
	 */
	public Map<String, List<String>> getBodyFromUrlFormEncoded(final boolean shouldDecode) {
		Charset charset = CharacterSet.UTF_8.getCharset();
		if (shouldDecode) {
			final QueryStringDecoder qsd = new QueryStringDecoder(getBody().toString(charset), charset, false);
			return qsd.getParameters();
		}
		final QueryStringParser qsp = new QueryStringParser(getBody().toString(charset), false);
		return qsp.getParameters();
	}

	/**
	 * Set body content.
	 * 
	 * @param body
	 *            {@link ChannelBuffer}.
	 */
	public void setBody(final ChannelBuffer body) {
		httpRequest.setContent(body);
	}

	/**
	 * Clear all headers.
	 */
	public void clearHeaders() {
		httpRequest.headers().clear();
	}

	/**
	 * Gets the named header from the request. Returns null if the header is not
	 * present. Both HTTP headers and query-string parameters are set as headers
	 * on the Request, with query-string parameters overriding headers if there
	 * is a name clash.
	 * <p/>
	 * NOTE: because HTTP headers are handled by Netty, which processes them
	 * with QueryStringDecoder, HTTP headers are URL decoded. Also query-string
	 * parameters that get processed by RestExpress are URL decoded before being
	 * set as headers on the request.
	 * 
	 * @param name
	 * @return the requested header, or null if 'name' doesn't exist as a
	 *         header.
	 */
	public String getHeader(final String name) {
		return httpRequest.headers().get(name);
	}

	/**
	 * Gets the list of named headers from the request. Returns null if the
	 * header name is not present.
	 * <p/>
	 * NOTE: because HTTP headers are handled by Netty, which processes them
	 * with QueryStringDecoder, HTTP headers are URL decoded. Also query-string
	 * parameters that get processed by RestExpress are URL decoded before being
	 * set as headers on the request.
	 * 
	 * @param name
	 * @return the requested list of headers, or null if 'name' doesn't exist as
	 *         a header.
	 */
	public List<String> getHeaders(final String name) {
		return httpRequest.headers().getAll(name);
	}

	/**
	 * Gets the named header from the request. Throws
	 * BadRequestException(message) if the header is not present. Both HTTP
	 * headers and query-string parameters are set as headers on the Request,
	 * with query-string parameters overriding headers if there is a name clash.
	 * <p/>
	 * NOTE: because HTTP headers are handled by Netty, which processes them
	 * with QueryStringDecoder, HTTP headers are URL decoded. Also query-string
	 * parameters that get processed by RestExpress are URL decoded before being
	 * set as headers on the request.
	 * 
	 * @param name
	 * @return the requested header
	 * @throws BadRequestException
	 *             (message) if 'name' doesn't exist as a header.
	 */
	public String getHeader(final String name, final String message) {
		final String value = getHeader(name);
		if (value == null) {
			throw new BadRequestException(message);
		}
		return value;
	}

	/**
	 * Returns all header names in the request
	 * 
	 * @return Set of all header names
	 */
	public Set<String> getHeaderNames() {
		return httpRequest.headers().names();
	}

	/**
	 * Adds a header to the request
	 * 
	 * Beware that headers set when parsing the HTTP request are
	 * indistinguishable from those set programmatically via this method.
	 * Consider using {@link #putAttachment} to persist data that should not be
	 * directly accessible by the request.
	 */
	public void addHeader(final String name, final String value) {
		httpRequest.headers().add(name, value);
	}

	/**
	 * Adds a collection of headers to the request
	 * 
	 * Beware that headers set when parsing the HTTP request are
	 * indistinguishable from those set programmatically via this method.
	 * Consider using {@link #putAttachment} to persist data that should not be
	 * directly accessible by the request.
	 */
	public void addAllHeaders(final Collection<Entry<String, String>> headers) {
		for (final Entry<String, String> entry : headers) {
			addHeader(entry.getKey(), entry.getValue());
		}
	}

	/**
	 * @return resolved {@link Route}.
	 */
	public Route getResolvedRoute() {
		return resolvedRoute;
	}

	/**
	 * Set resolved {@link Route}.
	 * 
	 * @param route
	 */
	public void setResolvedRoute(final Route route) {
		this.resolvedRoute = route;
	}

	/**
	 * Gets the path for this request.
	 * 
	 * @return
	 */
	public String getPath() {
		return httpRequest.getUri();
	}

	/**
	 * Returns the protocol and host (without the path) portion of the URL.
	 * 
	 * @return
	 */
	public String getBaseUrl() {
		return getProtocol() + "://" + getHost();
	}

	/**
	 * Returns the full URL for the request, containing protocol, host and path.
	 * Note that this call also returns the query string as part of the path.
	 * 
	 * @return the full URL for the request.
	 */
	public String getUrl() {
		return getBaseUrl() + getPath();
	}

	/**
	 * Get the named URL for the current effective HTTP method.
	 * 
	 * @param resourceName
	 *            the name of the route
	 * @return the URL pattern, or null if the name/method does not exist.
	 */
	public String getNamedUrl(final String resourceName) {
		return getNamedUrl(getEffectiveHttpMethod(), resourceName);
	}

	/**
	 * Get the named URL for the given HTTP method
	 * 
	 * @see RouteResolver#getNamedUrl(String, HttpMethod)
	 * 
	 * @param method
	 *            the HTTP method
	 * @param resourceName
	 *            the name of the route
	 * @return the URL pattern, or null if the name/method does not exist.
	 */
	public String getNamedUrl(final HttpMethod method, final String resourceName) {
		return routeResolver.getNamedUrl(resourceName, method);
	}

	/**
	 * Get the path pattern (without protocol://host:port) for a named Route and
	 * given HTTP method
	 * 
	 * @param method
	 *            the HTTP method
	 * @param resourceName
	 *            the name of the route
	 * @return the URL Path pattern, or null if the name/method does not exist.
	 */
	public String getNamedPath(final HttpMethod method, final String resourceName) {
		final Route route = routeResolver.getNamedRoute(resourceName, method);
		return route != null ? route.getPattern() : null;
	}

	/**
	 * @return query string as {@link Map}.
	 */
	public Map<String, String> getQueryStringMap() {
		return queryStringMap;
	}

	/**
	 * @return {@link Boolean#TRUE} if and only if the connection can remain
	 *         open and thus 'kept alive'.
	 */
	public boolean isKeepAlive() {
		return HttpHeaders.isKeepAlive(httpRequest);
	}

	/**
	 * @return {@link Boolean#TRUE} if and only if this message does not have
	 *         any content
	 */
	public boolean isChunked() {
		return httpRequest.isChunked();
	}

	/**
	 * Get the value of the {format} header in the request.
	 * 
	 * @return format or null if none was found.
	 */
	public String getFormat() {
		return getHeader(Parameters.Query.FORMAT);
	}

	/**
	 * Get the host (and port) from the request.
	 * 
	 * @return the value of the "Host" header.
	 */
	public String getHost() {
		return HttpHeaders.getHost(httpRequest);
	}

	/**
	 * Get the protocol of the request.
	 * 
	 * @return "http" or "https," etc. lower-case.
	 */
	public String getProtocol() {
		return httpRequest.getProtocolVersion().getProtocolName().toLowerCase();
	}

	/**
	 * Checks the format request parameter against the given format value.
	 * Ignores case.
	 * 
	 * @param format
	 * @return true if the given format matches (case insensitive) the request
	 *         format parameter. Otherwise false.
	 */
	public boolean isFormatEqual(final String format) {
		return isHeaderEqual(Parameters.Query.FORMAT, format);
	}

	/**
	 * Checks the value of the given header against the given value. Ignores
	 * case. If the header value or given value is null or has a trimmed length
	 * of zero, returns false.
	 * 
	 * @param name
	 *            the name of a header to check.
	 * @param value
	 *            the expected value.
	 * @return true if the header equals (ignoring case) to the given value.
	 */
	public boolean isHeaderEqual(final String name, final String value) {
		final String header = getHeader(name);
		if ((header == null) || (header.trim().length() == 0) || (value == null) || (value.trim().length() == 0)) {
			return false;
		}
		return header.trim().equalsIgnoreCase(value.trim());
	}

	/**
	 * Ask if the request contains the named flag. Flags are boolean settings
	 * that are created at route definition time. These flags can be used to
	 * pass booleans to preprocessors, controllers, or postprocessors. An
	 * example might be: flag(NO_AUTHORIZATION), which might inform an
	 * authorization preprocessor to skip authorization for this route.
	 * 
	 * @param flag
	 *            the name of a flag.
	 * @return true if the request contains the named flag, otherwise false.
	 */
	public boolean isFlagged(final String flag) {
		return resolvedRoute.isFlagged(flag);
	}

	/**
	 * Get a named parameter. Parameters are named settings that are created at
	 * route definition time. These parameters can be used to pass data to
	 * subsequent preprocessors, controllers, or postprocessors. This is a way
	 * to pass data from a route definition down to subsequent controllers, etc.
	 * An example might be: setParameter("route", "read_foo")
	 * setParameter("permission", "view_private_data"), which might inform an
	 * authorization preprocessor of what permission is being requested on a
	 * given resource.
	 * 
	 * @param name
	 *            the name of a parameter to retrieve.
	 * @return the named parameter or null, if not present.
	 */
	public Object getParameter(final String name) {
		return resolvedRoute.getParameter(name);
	}

	/**
	 * Each request can have many user-defined attachments, perhaps placed via
	 * preprocessors, etc. These attachments are named and are carried along
	 * with the request to subsequent preprocessors, controllers, and
	 * postprocessors. Attachments are different than parameters in that, they
	 * are set on a per request basis, instead of at the route level. They can
	 * be set via preprocessors, controllers, postprocessor, as opposed to
	 * parameters which are set on the route definition.
	 * 
	 * @param name
	 *            the name of an attachment.
	 * @return the named attachment, or null if it is not present.
	 */
	public Object getAttachment(final String name) {
		if (attachments != null) {
			return attachments.get(name);
		}
		return null;
	}

	/**
	 * Determine whether a named attachment is present.
	 * 
	 * @param name
	 *            the name of a parameter.
	 * @return true if the parameter is present, otherwise false.
	 */
	public boolean hasAttachment(final String name) {
		return (getAttachment(name) != null);
	}

	/**
	 * Set an attachment on this request. These attachments are named and are
	 * carried along with the request to subsequent preprocessors, controllers,
	 * and postprocessors. Attachments are different than parameters in that,
	 * they are set on a per request basis, instead of at the route level. They
	 * can be set via preprocessors, controllers, postprocessor, as opposed to
	 * parameters which are set on the route definition.
	 * 
	 * @param name
	 *            the name of the attachment.
	 * @param attachment
	 *            the attachment to associate with this request.
	 */
	public void putAttachment(final String name, final Object attachment) {
		if (attachments == null) {
			attachments = new HashMap<String, Object>();
		}
		attachments.put(name, attachment);
	}

	/**
	 * @return {@link HttpVersion}/
	 */
	public HttpVersion getHttpVersion() {
		return httpVersion;
	}

	/**
	 * @return {@link Boolean#TRUE} if HTTP version is 1.0
	 */
	public boolean isHttpVersion1_0() {
		return ((httpVersion.getMajorVersion() == 1) && (httpVersion.getMinorVersion() == 0));
	}

	/**
	 * @return remote {@link InetSocketAddress}.
	 */
	public InetSocketAddress getRemoteAddress() {
		return remoteAddress;
	}

	/**
	 * Add the query string parameters to the request as headers. Also parses
	 * the query string into the queryStringMap, if applicable. Note, if the
	 * query string contains multiple of the same parameter name, the headers
	 * will contain them all, but the queryStringMap will only contain the first
	 * one. This will be fixed in a future release.
	 */
	private static Map<String, String> parseQueryString(final HttpRequest request) {
		if (!request.getUri().contains("?")) {
			return new HashMap<String, String>();
		}
		final Map<String, List<String>> parameters = new QueryStringParser(request.getUri(), true).getParameters();
		if ((parameters == null) || parameters.isEmpty()) {
			return new HashMap<String, String>();
		}
		Map<String, String> queryStringMap = new HashMap<String, String>(parameters.size());
		String charset = CharacterSet.UTF_8.getCharsetName();
		for (final Entry<String, List<String>> entry : parameters.entrySet()) {
			queryStringMap.put(entry.getKey(), entry.getValue().get(0));

			for (final String value : entry.getValue()) {
				try {
					request.headers().add(entry.getKey(), URLDecoder.decode(value, charset));
				} catch (final Exception e) {
					request.headers().add(entry.getKey(), value);
				}
			}
		}
		return queryStringMap;
	}

	/**
	 * If the request HTTP method is post, allow a query string parameter to
	 * determine the request HTTP method of the post (e.g. _method=DELETE or
	 * _method=PUT). This supports DELETE and PUT from the browser.
	 * 
	 * @param parameters
	 * @return effective {@link HttpMethod}.
	 */
	private HttpMethod determineEffectiveHttpMethod(final HttpRequest request) {
		HttpMethod httpMethod = request.getMethod();
		if (!HttpMethod.POST.equals(httpMethod)) {
			return httpMethod;
		}
		final String methodString = this.getHeader(Parameters.Query.METHOD_TUNNEL);
		if ("PUT".equalsIgnoreCase(methodString) || "DELETE".equalsIgnoreCase(methodString)) {
			return HttpMethod.valueOf(methodString.toUpperCase());
		}
		return httpMethod;
	}

	/**
	 * Create correlation Id.
	 * 
	 * @return correlation Id.
	 */
	protected static String createCorrelationId() {
		return String.valueOf(nextCorrelationId.incrementAndGet());
	}
}
