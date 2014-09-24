/**
 * Licensed to the Apache Software Foundation (ASF) under one or more contributor license agreements. See the NOTICE file distributed
 * with this work for additional information regarding copyright ownership. The ASF licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License
 * at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS"
 * BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing
 * permissions and limitations under the License.
 * 
 */
package org.restexpress.processor;

import java.util.Date;

import org.intelligentsia.commons.http.HttpHeaderDateTimeFormat;
import org.intelligentsia.commons.http.ResponseHeader;
import org.jboss.netty.handler.codec.http.HttpMethod;
import org.restexpress.Request;
import org.restexpress.pipeline.MessageContext;
import org.restexpress.pipeline.Postprocessor;

/**
 * For GET and HEAD requests, adds a Date: <timestamp> header, if not already present. This enables clients to determine age of a
 * representation for caching purposes. <timestamp> is in RFC1123 full date format.
 * <p/>
 * To use: simply add server.addPostprocessor(new DateHeaderPostprocessor()); in your main() method.
 * <p/>
 * Note that HEAD requests are not provided with a Date header via this postprocessor. This is due to the fact that most external
 * caches forward HEAD requests to the origin server as a GET request and cache the result.
 * 
 * @author <a href="mailto:jguibert@intelligents-ia.com" >Jerome Guibert</a>
 * @author toddf
 * @since Oct 3, 2011
 */
public class DateHeaderPostprocessor implements Postprocessor {

    @Override
    public void process(final MessageContext context) {
        final Request request = context.getRequest();
        if ((request.isMethodGet() || HttpMethod.HEAD.equals(request.getHttpMethod()))
                && !context.getResponse().hasHeader(ResponseHeader.DATE.getHeader())) {
            final Date date = new Date(System.currentTimeMillis());
            context.getResponse().addHeader(ResponseHeader.DATE.getHeader(), HttpHeaderDateTimeFormat.RFC_1123.format(date));
        }
    }
}
