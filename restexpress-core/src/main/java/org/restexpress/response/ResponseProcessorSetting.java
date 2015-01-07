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
 * Copyright 2013, Strategic Gains, Inc.
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
package org.restexpress.response;

import org.restexpress.Request;
import org.restexpress.Response;
import org.restexpress.exception.DeserializationException;
import org.restexpress.exception.SerializationException;
import org.restexpress.http.HttpHeader;

/**
 * {@link ResponseProcessorSetting} represent a selection of a specific media
 * Type with his {@link ResponseProcessor} in order to deal with serialization
 * for a {@link Request} or a {@link Response}.
 * 
 * 
 * 
 * @author <a href="mailto:jguibert@intelligents-ia.com" >Jerome Guibert</a>
 */
public final class ResponseProcessorSetting implements Serializer {
	/**
	 * media type.
	 */
	private final String mediaType;
	/**
	 * {@link ResponseProcessor} instance.
	 */
	private final ResponseProcessor responseProcessor;

	/**
	 * Build a new instance of {@link ResponseProcessorSetting}.
	 * 
	 * @param mediaType
	 * @param responseProcessor
	 */
	public ResponseProcessorSetting(final String mediaType, final ResponseProcessor responseProcessor) {
		super();
		this.mediaType = mediaType;
		this.responseProcessor = responseProcessor;
	}

	/**
	 * @return media type.
	 */
	public String mediaType() {
		return mediaType;
	}

	/**
	 * @return {@link ResponseProcessor} instance
	 */
	public ResponseProcessor responseProcessor() {
		return responseProcessor;
	}

	@Override
	public <T> T deserialize(Request request, Class<T> type) throws DeserializationException {
		return responseProcessor.deserialize(request, type);
	}

	/**
	 * If {@link Response#isSerialized()} then
	 * <ul>
	 * <li>add {@link ResponseHeader#CONTENT_TYPE} if not defined</li>
	 * <li>Use selected {@link ResponseWrapper}</li>
	 * <li>Serialize the response</li>
	 * </ul>
	 * If {@link Response#hasException()} then
	 * <ul>
	 * <li>Use selected {@link ResponseWrapper}</li>
	 * <li>Serialize the response if needed (depends on which
	 * {@link ResponseWrapper} is used</li>
	 * <li>add {@link ResponseHeader#CONTENT_TYPE}</li>
	 * </ul>
	 * 
	 * @see org.restexpress.response.Serializer#serialize(org.restexpress.Response)
	 */
	@Override
	public void serialize(Response response) throws SerializationException {
		if (response.hasException() || response.isSerialized()) {
			// serialization configuration can change
			final Object wrapped = responseProcessor.wrapper().wrap(response);
			response.setEntity(wrapped);
		}
		responseProcessor.serialize(response);
		// serialized way: don't override
		if (response.isSerialized() && !response.hasHeader(HttpHeader.CONTENT_TYPE)) {
			response.setContentType(mediaType);
		} else if (response.hasException()) {
			// in case of exception we must set right content type
			response.setContentType(mediaType);
		}
	}

}
