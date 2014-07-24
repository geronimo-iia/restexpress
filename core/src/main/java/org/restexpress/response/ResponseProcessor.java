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
 Copyright 2012, Strategic Gains, Inc.

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

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.restexpress.Request;
import org.restexpress.Response;
import org.restexpress.exception.DeserializationException;
import org.restexpress.exception.SerializationException;
import org.restexpress.processor.Processor;

/**
 * {@link ResponseProcessor} is an association of a {@link Processor} for
 * serialization purpose and a {@link ResponseWrapper} to render error result.
 * This class implements {@link Serializer} interface.
 * 
 * 
 * @author <a href="mailto:jguibert@intelligents-ia.com" >Jerome Guibert</a>
 * @author toddf
 * @since May 14, 2012
 */
public final class ResponseProcessor implements Serializer {

	/**
	 * {@link Processor} instance.
	 */
	private final Processor processor;
	/**
	 * {@link ResponseWrapper} instance.
	 */
	private final ResponseWrapper wrapper;

	/**
	 * Build a new instance of {@link ResponseProcessor}.
	 * 
	 * @param org.restexpress.processor
	 * @param wrapper
	 * @throws IllegalArgumentException
	 *             if one of parameter is null
	 */
	public ResponseProcessor(final Processor processor, final ResponseWrapper wrapper) throws IllegalArgumentException {
		super();
		if (processor == null)
			throw new IllegalArgumentException("Processor can't be null");
		if (wrapper == null)
			throw new IllegalArgumentException("ResponseWrapper can't be null");
		this.processor = processor;
		this.wrapper = wrapper;
	}

	/**
	 * @return inner {@link Processor} instance.
	 */
	public Processor processor() {
		return processor;
	}

	@Override
	public <T> T deserialize(final Request request, final Class<T> type) throws DeserializationException {
		return processor.read(request.getBody(), type);
	}

	@Override
	public ChannelBuffer serialize(final Response response) throws SerializationException {
		final Object wrapped = wrapper.wrap(response);
		if (wrapped != null) {
			ChannelBuffer content = ChannelBuffers.dynamicBuffer();
			processor.write(wrapped, content);
			return content;
		}
		return null;
	}

}
