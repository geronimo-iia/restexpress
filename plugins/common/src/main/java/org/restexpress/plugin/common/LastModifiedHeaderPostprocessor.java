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
package org.restexpress.plugin.common;

import org.intelligentsia.commons.http.HttpMethods;
import org.intelligentsia.commons.http.ResponseHeader;
import org.restexpress.Response;
import org.restexpress.pipeline.MessageContext;
import org.restexpress.pipeline.Postprocessor;

import com.strategicgains.util.date.DateAdapter;
import com.strategicgains.util.date.HttpHeaderTimestampAdapter;

/**
 * {@link LastModifiedHeaderPostprocessor} add header
 * {@link ResponseHeader#LAST_MODIFIED} for {@link HttpMethods#GET} if not
 * present. Time come from {@link Response#getBody()} if the {@link Object}
 * implement Timestamped.
 * 
 * TODO define Timestamped.
 * 
 * @author <a href="mailto:jguibert@intelligents-ia.com" >Jerome Guibert</a>
 */
public class LastModifiedHeaderPostprocessor implements Postprocessor {
	DateAdapter fmt = new HttpHeaderTimestampAdapter();

	@Override
	public void process(MessageContext context) {
		if (!context.getRequest().isMethodGet())
			return;
		Response response = context.getResponse();
		if (!context.getResponse().hasBody())
			return;
		Object body = response.getBody();

		// if (!response.hasHeader(ResponseHeader.LAST_MODIFIED.getHeader()) &&
		// body instanceof Timestamped) {
		// response.addHeader(ResponseHeader.LAST_MODIFIED.getHeader(),
		// fmt.format(((Timestamped) body).getUpdatedAt()));
		// }
	}
}
