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
package org.restexpress.processor;

import java.io.File;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.restexpress.HttpSpecification;
import org.restexpress.Request;
import org.restexpress.Response;
import org.restexpress.domain.Format;
import org.restexpress.exception.HttpRuntimeException;
import org.restexpress.http.HttpDateTimeFormat;
import org.restexpress.http.HttpHeader;
import org.restexpress.http.HttpStatus;
import org.restexpress.pipeline.MessageContext;
import org.restexpress.pipeline.Postprocessor;

/**
 * {@link FileHeaderPostProcessor} implements a {@link Postprocessor} which
 * manage several use case if content is allowed..
 * 
 * TODO doc
 * 
 * 
 * @author <a href="mailto:jguibert@intelligents-ia.com" >Jerome Guibert</a>
 */
public final class FileHeaderPostProcessor implements Postprocessor {

	public FileHeaderPostProcessor() {
		super();
	}

	@Override
	public void process(MessageContext context) {
		final Response response = context.getResponse();
		if (HttpSpecification.isContentTypeAllowed(response)) {
			final Object body = response.getEntity();
			if (body != null) {
				if (File.class.isAssignableFrom(body.getClass())) {
					final File resource = (File) response.getEntity();
					processFileResponseHeader(context.getRequest(), response, resource);
				}
			}
		}
	}

	protected static void processFileResponseHeader(final Request request, final Response response, final File resource) {
		// check for is Modified Since
		if (!isModifiedSince(request, resource)) {
			response.setStatusInfo(HttpStatus.NOT_MODIFIED);
			final Calendar time = new GregorianCalendar();
			response.addHeader(HttpHeader.DATE, HttpDateTimeFormat.RFC_1123.format(time.getTime()));
		} else {
			// we have little thing to do
			final Calendar time = new GregorianCalendar();
			final Date currentTime = time.getTime();
			// date header
			response.addHeader(HttpHeader.DATE, HttpDateTimeFormat.RFC_1123.format(currentTime));
			// last modified header
			final Date lastModified = new Date(resource.lastModified());
			if (lastModified.after(currentTime)) {
				response.addHeader(HttpHeader.LAST_MODIFIED, HttpDateTimeFormat.RFC_1123.format(currentTime));
			} else {
				response.addHeader(HttpHeader.LAST_MODIFIED, HttpDateTimeFormat.RFC_1123.format(lastModified));
			}
			// content type
			String mediaType = Format.BIN.getMediaType();
			final String resourceName = resource.getName();
			final int index = resourceName.indexOf(".");
			if (index >= 0) {
				final String extension = resourceName.substring(index + 1);
				mediaType = Format.asMap().get(extension);
			}
			response.addHeader(HttpHeader.CONTENT_TYPE, mediaType);

			// we can now add content length header
			response.addHeader(HttpHeader.CONTENT_LENGTH, Long.toString(resource.length()));
		}
	}

	/**
	 * @param request
	 * @param resource
	 * @return True if resource is modified since date value read from
	 *         {@link RequestHeader#IF_MODIFIED_SINCE}.
	 * @throws HttpRuntimeException
	 *             {@link HttpStatus#BAD_REQUEST} if header
	 *             {@link RequestHeader#IF_MODIFIED_SINCE} cannot be parsed.
	 */
	protected static boolean isModifiedSince(final Request request, final File resource) throws HttpRuntimeException {
		final String ifModifiedSince = request.getHeader(HttpHeader.IF_MODIFIED_SINCE);
		if ((ifModifiedSince != null) && !ifModifiedSince.isEmpty()) {
			try {
				final Date ifModifiedSinceDate = HttpDateTimeFormat.parseAny(ifModifiedSince);
				// just compare second
				final long ifModifiedSinceDateSeconds = ifModifiedSinceDate.getTime() / 1000;
				final long fileLastModifiedSeconds = resource.lastModified() / 1000;
				return ifModifiedSinceDateSeconds <= fileLastModifiedSeconds;
			} catch (final ParseException e) {
				throw new HttpRuntimeException(HttpStatus.BAD_REQUEST, e);
			}
		}
		return true;
	}
}
