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
package org.restexpress.pipeline.handler;

import java.io.File;
import java.net.InetSocketAddress;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.intelligentsia.commons.http.HttpHeaderDateTimeFormat;
import org.intelligentsia.commons.http.RequestHeader;
import org.intelligentsia.commons.http.ResponseHeader;
import org.intelligentsia.commons.http.exception.HttpRuntimeException;
import org.intelligentsia.commons.http.status.HttpResponseStandardStatus;
import org.jboss.netty.channel.ChannelHandler.Sharable;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.handler.codec.http.HttpHeaders;
import org.jboss.netty.handler.codec.http.HttpRequest;
import org.jboss.netty.handler.codec.http.HttpResponseStatus;
import org.restexpress.Request;
import org.restexpress.Response;
import org.restexpress.SerializationProvider;
import org.restexpress.domain.CharacterSet;
import org.restexpress.domain.Format;
import org.restexpress.domain.MediaType;
import org.restexpress.pipeline.HttpResponseWriter;
import org.restexpress.pipeline.MessageContext;
import org.restexpress.response.ResponseProcessorManager;
import org.restexpress.response.ResponseProcessorSetting;
import org.restexpress.response.ResponseProcessorSettingResolver;
import org.restexpress.route.Action;
import org.restexpress.route.RouteResolver;
import org.restexpress.util.HttpSpecification;

/**
 * {@link DefaultRequestHandler} implements an {@link AbstractRequestHandler}
 * for our {@link ResponseProcessorSetting} and {@link RouteResolver}.
 * 
 * @author <a href="mailto:jguibert@intelligents-ia.com" >Jerome Guibert</a>
 * @author toddf
 * @since Nov 13, 2009
 */
@Sharable
public final class DefaultRequestHandler extends AbstractRequestHandler {

	private final RouteResolver routeResolver;
	private final ResponseProcessorManager responseProcessorManager;
	// default content type (may be a must to manage this in another place)
	private final String defaultContentType;

	public DefaultRequestHandler(final RouteResolver routeResolver, final ResponseProcessorManager responseProcessorManager, //
			final HttpResponseWriter responseWriter, final boolean shouldEnforceHttpSpec,//
			final String defaultContentType) {
		super(responseWriter, shouldEnforceHttpSpec);
		this.routeResolver = routeResolver;
		this.responseProcessorManager = responseProcessorManager;
		this.defaultContentType = defaultContentType;
	}

	/**
	 * Build a new instance of {@link DefaultRequestHandler}.
	 * 
	 * @param routeResolver
	 *            {@link RouteResolver}.
	 * @param responseProcessorManager
	 *            {@link ResponseProcessorManager}
	 * @param responseWriter
	 *            {@link HttpResponseWriter}
	 * @param enforceHttpSpec
	 */
	public DefaultRequestHandler(final RouteResolver routeResolver, //
			final ResponseProcessorManager responseProcessorManager,//
			final HttpResponseWriter responseWriter, final boolean enforceHttpSpec) {
		this(routeResolver, responseProcessorManager, responseWriter, enforceHttpSpec, MediaType.TEXT_PLAIN.withCharset(CharacterSet.UTF_8.getCharsetName()));
	}

	/**
	 * Build a new instance of {@link DefaultRequestHandler}.
	 * 
	 * @param routeResolver
	 *            {@link RouteResolver}.
	 * @param responseProcessorSettingResolver
	 *            {@link ResponseProcessorSettingResolver}
	 * @param responseWriter
	 *            {@link HttpResponseWriter}
	 * @param enforceHttpSpec
	 */
	public DefaultRequestHandler(final RouteResolver routeResolver, //
			final HttpResponseWriter responseWriter, final boolean enforceHttpSpec) {
		this(routeResolver, new ResponseProcessorManager(), responseWriter, enforceHttpSpec);
	}

	/**
	 * Testing purpose.
	 * 
	 * @return {@link SerializationProvider}.
	 */
	public SerializationProvider serializationProvider() {
		return responseProcessorManager;
	}

	@Override
	protected void resolveResponseProcessor(final MessageContext context) {
		final ResponseProcessorSetting setting = responseProcessorManager.resolve(context.getRequest(), context.getResponse(), false);
		context.setResponseProcessorSetting(setting);
	}

	@Override
	protected void resolveRoute(final MessageContext context) {
		final Action action = routeResolver.resolve(context);
		context.setAction(action);
	}

	@Override
	protected Request createRequest(final MessageEvent event, final ChannelHandlerContext context) {
		return new Request((HttpRequest) event.getMessage(), routeResolver, responseProcessorManager, (InetSocketAddress) event.getRemoteAddress());
	}

	@Override
	protected Response createResponse() {
		return new Response();
	}

	@Override
	protected void handleResponseContent(final MessageContext context, final boolean force) {
		final Response response = context.getResponse();
		if (HttpSpecification.isContentTypeAllowed(response)) {
			/*
			 * Mind ResponseProcessorSetting is something that can handle
			 * exception and Serialization. We assume that for a File, we have
			 * no serialization on response.
			 */
			ResponseProcessorSetting settings = context.getResponseProcessorSetting();
			if ((settings == null) && force) {
				settings = responseProcessorManager.resolve(context.getRequest(), response, force);
			}
			// process serialization if one was found
			if (settings != null) {
				settings.serialize(response);
			}

			// TODO manage this in another place
			// manage File case
			final Object body = response.getBody();
			if (body != null) {
				if (File.class.isAssignableFrom(body.getClass())) {
					final File resource = (File) response.getBody();
					processFileResponseHeader(context.getRequest(), response, resource);
				}
			}

			// add default content type if none was provided only if content
			// type is allowed
			if (!response.hasHeader(HttpHeaders.Names.CONTENT_TYPE)) {
				response.setContentType(defaultContentType);
			}
		}
	}

	private static void processFileResponseHeader(final Request request, final Response response, final File resource) {
		// check for is Modified Since
		if (!isModifiedSince(request, resource)) {
			response.setResponseStatus(HttpResponseStatus.NOT_MODIFIED);
			final Calendar time = new GregorianCalendar();
			response.addHeader(ResponseHeader.DATE.getHeader(), HttpHeaderDateTimeFormat.RFC_1123.format(time.getTime()));
		} else {
			// we have little thing to do
			final Calendar time = new GregorianCalendar();
			final Date currentTime = time.getTime();
			// date header
			response.addHeader(ResponseHeader.DATE.getHeader(), HttpHeaderDateTimeFormat.RFC_1123.format(currentTime));
			// last modified header
			final Date lastModified = new Date(resource.lastModified());
			if (lastModified.after(currentTime)) {
				response.addHeader(ResponseHeader.LAST_MODIFIED.getHeader(), HttpHeaderDateTimeFormat.RFC_1123.format(currentTime));
			} else {
				response.addHeader(ResponseHeader.LAST_MODIFIED.getHeader(), HttpHeaderDateTimeFormat.RFC_1123.format(lastModified));
			}
			// content type
			String mediaType = Format.BIN.getMediaType();
			final String resourceName =resource.getName(); 
			final int index = resourceName.indexOf(".");
			if (index>=0) {
				final String extension = resourceName.substring(index);
				mediaType = Format.asMap().get(extension);
			}
			response.addHeader(ResponseHeader.CONTENT_TYPE.getHeader(), mediaType);

			// we can now add content length header
			response.addHeader(ResponseHeader.CONTENT_LENGTH.getHeader(), Long.toString(resource.length()));
		}
	}

	/**
	 * @param request
	 * @param resource
	 * @return True if resource is modified since date value read from
	 *         {@link RequestHeader#IF_MODIFIED_SINCE}.
	 * @throws HttpRuntimeException
	 *             {@link HttpResponseStandardStatus#BAD_REQUEST} if header
	 *             {@link RequestHeader#IF_MODIFIED_SINCE} cannot be parsed.
	 */
	private static boolean isModifiedSince(final Request request, final File resource) throws HttpRuntimeException {
		final String ifModifiedSince = request.getHeader(RequestHeader.IF_MODIFIED_SINCE.getHeader());
		if ((ifModifiedSince != null) && !ifModifiedSince.isEmpty()) {
			try {
				final Date ifModifiedSinceDate = HttpHeaderDateTimeFormat.parseAny(ifModifiedSince);
				// just compare second
				final long ifModifiedSinceDateSeconds = ifModifiedSinceDate.getTime() / 1000;
				final long fileLastModifiedSeconds = resource.lastModified() / 1000;
				return ifModifiedSinceDateSeconds <= fileLastModifiedSeconds;
			} catch (final ParseException e) {
				throw new HttpRuntimeException(HttpResponseStandardStatus.BAD_REQUEST, e);
			}
		}
		return true;
	}
}
