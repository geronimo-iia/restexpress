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
package org.restexpress.plugin.content;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.regex.Pattern;

import org.intelligentsia.commons.http.HttpHeaderDateTimeFormat;
import org.intelligentsia.commons.http.RequestHeader;
import org.intelligentsia.commons.http.ResponseHeader;
import org.jboss.netty.handler.codec.http.HttpResponseStatus;
import org.restexpress.Request;
import org.restexpress.Response;

import com.google.common.base.Preconditions;

/**
 * {@link ContentController} declare controller for serve all static content.
 * 
 * URI management are minded from {@link https
 * ://github.com/netty/netty/blob/master/example/src/main/java/io/netty/example/http/file/HttpStaticFileServerHandler.java}
 * 
 * 
 * @author <a href="mailto:jguibert@intelligents-ia.com" >Jerome Guibert</a>
 */
public class ContentController {

    private static final Pattern INSECURE_URI = Pattern.compile(".*[<>&\"].*");

    /**
     * Context adapter to retrieve resource.
     */
    protected final ContextAdapter contextAdapter;

    /**
     * Build a new instance of ContentController.
     * 
     * @param contextAdapter instance of {@link ContextAdapter}.
     * @throws NullPointerException if {@link ContextAdapter} is null
     */
    public ContentController(final ContextAdapter contextAdapter) throws NullPointerException {
        super();
        this.contextAdapter = Preconditions.checkNotNull(contextAdapter);
    }

    /**
     * Main method.
     * 
     * @param request
     * @param response
     * @return a {@link File} instance if something match.
     * @throws IOException
     * @throws ParseException
     */
    public File read(final Request request, final Response response) throws IOException, ParseException {
        final String uri = request.getPath();
        final String path = sanitizeUri(uri);
        File resource = null;
        if (path == null) {
            response.setResponseStatus(HttpResponseStatus.FORBIDDEN);
            return resource;
        }
        if (!contextAdapter.match(path)) {
            response.setResponseStatus(HttpResponseStatus.NOT_FOUND);
            return resource;
        }
        resource = contextAdapter.retrieve(path);
        if (resource == null) {
            response.setResponseStatus(HttpResponseStatus.NOT_FOUND);
            return resource;
        }
        if (isModifiedSince(request, resource)) {
            response.setResponseStatus(HttpResponseStatus.NOT_MODIFIED);
            final Calendar time = new GregorianCalendar();
            response.addHeader(ResponseHeader.DATE.getHeader(), HttpHeaderDateTimeFormat.RFC_1123.format(time.getTime()));
            resource = null;
        }
        return resource;
    }

    public void delete(final Request request, final Response response) {
        response.setResponseStatus(HttpResponseStatus.FORBIDDEN);
    }

    public void create(final Request request, final Response response) {
        response.setResponseStatus(HttpResponseStatus.FORBIDDEN);
    }

    public void update(final Request request, final Response response) {
        response.setResponseStatus(HttpResponseStatus.FORBIDDEN);
    }

    public void headers(final Request request, final Response response) {
        response.setResponseStatus(HttpResponseStatus.FORBIDDEN);
    }

    public void options(final Request request, final Response response) {
        response.setResponseStatus(HttpResponseStatus.FORBIDDEN);
    }

    /**
     * @param request
     * @param resource
     * @return True if ressource is modified since date value read from {@link RequestHeader#IF_MODIFIED_SINCE}.
     * @throws ParseException
     */
    private static boolean isModifiedSince(final Request request, final File resource) throws ParseException {
        final String ifModifiedSince = request.getHeader(RequestHeader.IF_MODIFIED_SINCE.getHeader());
        if (ifModifiedSince != null && !ifModifiedSince.isEmpty()) {
            final Date ifModifiedSinceDate = HttpHeaderDateTimeFormat.parseAny(ifModifiedSince);
            // just compare second
            final long ifModifiedSinceDateSeconds = ifModifiedSinceDate.getTime() / 1000;
            final long fileLastModifiedSeconds = resource.lastModified() / 1000;
            return ifModifiedSinceDateSeconds <= fileLastModifiedSeconds;
        }
        return false;
    }

    /**
     * Sanitize URI.
     * 
     * @param uri
     * @return
     */
    private static String sanitizeUri(String uri) {
        try {
            uri = URLDecoder.decode(uri, "UTF-8");
        } catch (final UnsupportedEncodingException e) {
            throw new Error(e);
        }
        if (uri.isEmpty() || uri.charAt(0) != '/')
            return null;
        if (uri.contains("/.") || uri.contains("./") || uri.charAt(0) == '.' || uri.charAt(uri.length() - 1) == '.'
                || INSECURE_URI.matcher(uri).matches())
            return null;
        return uri;
    }

}
