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
import java.util.regex.Pattern;

import org.jboss.netty.handler.codec.http.HttpResponseStatus;
import org.restexpress.Parameters;
import org.restexpress.Request;
import org.restexpress.Response;
import org.restexpress.processor.CacheHeaderPostprocessor;

import com.google.common.base.Preconditions;

/**
 * {@link ContentController} declare controller for serve all static content.
 * 
 * Recommended usage with {@link CacheHeaderPostprocessor}.
 * 
 * URI management are minded from {@link https ://github.com/netty/netty/blob/master /example/src/main/java/io/netty/example/http
 * /file/HttpStaticFileServerHandler.java}
 * 
 * 
 * @author <a href="mailto:jguibert@intelligents-ia.com" >Jerome Guibert</a>
 */
public class ContentController {

    private static final Pattern INSECURE_URI = Pattern.compile(".*[<>&\"].*");

    /**
     * Context service to retrieve resource.
     */
    protected final ContentService contentService;

    /**
     * Build a new instance of ContentController.
     * 
     * @param contentService instance of {@link ContentService}.
     * @throws NullPointerException if {@link ContextAdapter} is null
     */
    public ContentController(final ContentService contentService) throws NullPointerException {
        super();
        this.contentService = Preconditions.checkNotNull(contentService);
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
            response.setResponseStatus(HttpResponseStatus.BAD_REQUEST);
            return resource;
        }
        if (!contentService.match(path)) {
            response.setResponseStatus(HttpResponseStatus.NOT_FOUND);
            return resource;
        }
        resource = contentService.retrieve(path);
        if (resource == null) {
            response.setResponseStatus(HttpResponseStatus.NOT_FOUND);
            return resource;
        }
        response.setResponseStatus(HttpResponseStatus.OK);
        // add cache information that could be used by CacheHeaderPostprocessor.
        if (contentService.isCacheEnabled()) {
            request.putAttachment(Parameters.Cache.MAX_AGE, contentService.expireAfterWrite() * 60);
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
