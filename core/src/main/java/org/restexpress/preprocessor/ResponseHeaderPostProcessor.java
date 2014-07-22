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
package org.restexpress.preprocessor;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.restexpress.Response;
import org.restexpress.pipeline.MessageContext;
import org.restexpress.pipeline.Postprocessor;

/**
 * {@link ResponseHeaderPostProcessor} add response header if they're not
 * present.
 * 
 * @author <a href="mailto:jguibert@intelligents-ia.com" >Jerome Guibert</a>
 * 
 */
public class ResponseHeaderPostProcessor implements Postprocessor {

	private final Map<String, String> headers;

	/**
	 * Build a new instance of {@link ResponseHeaderPostProcessor}.
	 * 
	 * @param name
	 *            header name
	 * @param value
	 *            header value
	 * @throws IllegalArgumentException
	 *             if name or value is null
	 */
	public ResponseHeaderPostProcessor(String name, String value) throws IllegalArgumentException {
		super();
		if (name == null)
			throw new IllegalArgumentException("name can't be null");
		if (value == null)
			throw new IllegalArgumentException("value can't be null");
		this.headers = new HashMap<String, String>();
		headers.put(name, value);
	}

	/**
	 * Build a new instance of {@link ResponseHeaderPostProcessor}.
	 * 
	 * @param headers
	 *            a {@link Map} of { header name, header value}
	 * @throws NullPointerException
	 *             if headers is null
	 */
	public ResponseHeaderPostProcessor(Map<String, String> headers) throws NullPointerException {
		super();
		if (headers == null)
			throw new NullPointerException("header can not be null");
		this.headers = new HashMap<String, String>(headers);
	}

	@Override
	public void process(MessageContext context) {
		Response response = context.getResponse();
		for (Entry<String, String> entry : headers.entrySet()) {
			if (!response.hasHeader(entry.getKey())) {
				response.addHeader(entry.getKey(), entry.getValue());
			}
		}
	}

}
