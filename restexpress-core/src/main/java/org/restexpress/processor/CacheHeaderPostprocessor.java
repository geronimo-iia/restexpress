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
/*
 * Copyright 2011, Strategic Gains, Inc.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You
 * may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS"
 * BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing
 * permissions and limitations under the License.
 */
package org.restexpress.processor;

import java.util.Date;

import org.intelligentsia.commons.http.HttpHeaderDateTimeFormat;
import org.intelligentsia.commons.http.RequestHeader;
import org.intelligentsia.commons.http.ResponseHeader;
import org.jboss.netty.handler.codec.http.HttpMethod;
import org.restexpress.Flags;
import org.restexpress.Parameters;
import org.restexpress.Request;
import org.restexpress.Response;
import org.restexpress.pipeline.MessageContext;
import org.restexpress.pipeline.Postprocessor;

/**
 * For GET and HEAD requests, adds caching control headers. May be used in conjunction with DateHeaderPostprocessor to add Date header
 * for GET and HEAD requests.
 * <p/>
 * If the route has a Flags.Cache.NO_CACHE flag, then the following headers are set on the response: Cache-Control: no-cache<br/>
 * Pragma: no-cache
 * <p/>
 * If the route has a Parameters.Cache.MAX_AGE parameter, or request has a Parameters.Cache.MAX_AGE attachment, whose value is the
 * max-age in seconds then the following are added:
 * <ul>
 * <li>Cache-Control: max-age=<seconds></li>
 * <li>Expires: now + max-age</li>
 * </ul>
 * <p/>
 * The MAX_AGE parameter takes precedence, in that, if present, the NO_CACHE flag is ignored.
 * <p/>
 * The Parameters.Cache.MAX_AGE attachment takes precedence on parameter.
 * <p/>
 * To use: simply add server.addPostprocessor(new CacheHeaderPostprocessor()); in your main() method.
 * 
 * @author <a href="mailto:jguibert@intelligents-ia.com" >Jerome Guibert</a>
 * @author toddf
 * @since Oct 3, 2011
 */
public class CacheHeaderPostprocessor implements Postprocessor {
    private static final String NO_CACHE = "no-cache";

    @Override
    public void process(final MessageContext context) {
        final Request request = context.getRequest();
        if (!request.isMethodGet() && !HttpMethod.HEAD.equals(request.getHttpMethod()))
            return;

        // lookup max age from request Attachment first, or from route definition is no attachment was found.
        Object maxAge = request.getAttachment(Parameters.Cache.MAX_AGE);
        if (maxAge == null) {
            maxAge = request.getParameter(Parameters.Cache.MAX_AGE);
        }
        final Response response = context.getResponse();
        //
        if (maxAge != null) {
            response.addHeader(ResponseHeader.CACHE_CONTROL.getHeader(), String.format("max-age=%s", maxAge));
            response.addHeader(ResponseHeader.EXPIRES.getHeader(),
                    HttpHeaderDateTimeFormat.RFC_1123.format(computeExpiresDate((Integer) maxAge)));
        } else if (request.isFlagged(Flags.NO_CACHE)) {
            response.addHeader(RequestHeader.CACHE_CONTROL.getHeader(), NO_CACHE);
            response.addHeader(RequestHeader.PRAGMA.getHeader(), NO_CACHE);
        }
    }

    /**
     * Compute expiration date.
     * 
     * @param maxAge
     */
    private Date computeExpiresDate(final Integer maxAge) {
        final long millis = System.currentTimeMillis() + (long) maxAge * 1000l;
        return new Date(millis);
    }
}
