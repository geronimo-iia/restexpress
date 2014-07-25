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
 Copyright 2013, Strategic Gains, Inc.

 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at

 http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
 */
package org.restexpress.response;

import org.intelligentsia.commons.http.ResponseHeader;
import org.jboss.netty.buffer.ChannelBuffer;
import org.restexpress.Request;
import org.restexpress.Response;
import org.restexpress.exception.DeserializationException;
import org.restexpress.exception.SerializationException;

/**
 * {@link ResponseProcessorSetting} represent a selection of a media Type with
 * his {@link ResponseProcessor} in order to deal with serialization for a
 * {@link Request} or a {@link Response}.
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

	@Override
	public <T> T deserialize(Request request, Class<T> type) throws DeserializationException {
		return responseProcessor.deserialize(request, type);
	}

	/**
	 * Add {@link ResponseHeader#CONTENT_TYPE} if not defined.
	 * 
	 * @see org.restexpress.response.Serializer#serialize(org.restexpress.Response)
	 */
	@Override
	public ChannelBuffer serialize(Response response) throws SerializationException {
		if (!response.hasHeader(ResponseHeader.CONTENT_TYPE.getHeader())) {
			response.setContentType(mediaType);
		}
		return responseProcessor.serialize(response);
	}

}
