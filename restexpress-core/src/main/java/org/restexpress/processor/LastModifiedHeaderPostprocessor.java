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

import org.intelligentsia.commons.http.HttpHeader;
import org.intelligentsia.commons.http.HttpHeaderDateTimeFormat;
import org.intelligentsia.commons.http.HttpMethods;
import org.restexpress.Response;
import org.restexpress.domain.TimeStamped;
import org.restexpress.pipeline.MessageContext;
import org.restexpress.pipeline.Postprocessor;

/**
 * {@link LastModifiedHeaderPostprocessor} add header {@link ResponseHeader#LAST_MODIFIED} for {@link HttpMethods#GET} if not present.
 * Time come from {@link Response#getBody()} if the {@link Object} implement {@link TimeStamped}.
 * 
 * @author <a href="mailto:jguibert@intelligents-ia.com" >Jerome Guibert</a>
 */
public class LastModifiedHeaderPostprocessor implements Postprocessor {

    @Override
    public void process(MessageContext context) {
        if (!context.getRequest().isMethodGet())
            return;
        Response response = context.getResponse();
        if (!response.hasBody())
            return;

        if (!response.hasHeader(HttpHeader.LAST_MODIFIED)) {
            Object body = response.getBody();
            if (TimeStamped.class.isAssignableFrom(body.getClass())) {
                response.addHeader(HttpHeader.LAST_MODIFIED,
                        HttpHeaderDateTimeFormat.RFC_1123.format(((TimeStamped) body).updateAt()));
            }
        }

    }
}
