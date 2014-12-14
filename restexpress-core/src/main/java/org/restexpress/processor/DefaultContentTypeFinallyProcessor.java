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

import org.jboss.netty.handler.codec.http.HttpHeaders;
import org.restexpress.HttpSpecification;
import org.restexpress.Response;
import org.restexpress.domain.CharacterSet;
import org.restexpress.domain.MediaType;
import org.restexpress.pipeline.MessageContext;
import org.restexpress.pipeline.Postprocessor;

/**
 * {@link DefaultContentTypeFinallyProcessor} implement a {@link Postprocessor}
 * which add default content type if none was provided AND if content is
 * allowed.
 * 
 * Default Content Type is per default "text/plain; utf-8".
 * 
 * @author <a href="mailto:jguibert@intelligents-ia.com" >Jerome Guibert</a>
 * 
 */
public final class DefaultContentTypeFinallyProcessor implements Postprocessor {

	private final String defaultContentType;

	/**
	 * Build a new instance of {@link DefaultContentTypeFinallyProcessor} with
	 * specified default content type.
	 * 
	 * @param defaultContentType
	 *            default Content Type to use.
	 */
	public DefaultContentTypeFinallyProcessor(String defaultContentType) {
		super();
		this.defaultContentType = defaultContentType;
	}

	/**
	 * Build a new instance of {@link DefaultContentTypeFinallyProcessor} with
	 * default content type "text/plain; utf-8".
	 */
	public DefaultContentTypeFinallyProcessor() {
		this(MediaType.TEXT_PLAIN.withCharset(CharacterSet.UTF_8.getCharsetName()));
	}

	@Override
	public void process(MessageContext context) {
		final Response response = context.getResponse();
		if (HttpSpecification.isContentTypeAllowed(response)) {
			if (!response.hasHeader(HttpHeaders.Names.CONTENT_TYPE)) {
				response.setContentType(defaultContentType);
			}
		}
	}

	@Override
	public String toString() {
		return "DefaultContentTypeFinallyProcessor [defaultContentType=" + defaultContentType + "]";
	}

}
