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
package org.restexpress.response;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.restexpress.Request;
import org.restexpress.Response;
import org.restexpress.exception.DeserializationException;
import org.restexpress.exception.SerializationException;
import org.restexpress.serialization.Processor;

import com.google.common.base.Preconditions;

/**
 * {@link ResponseProcessor} is an association of a {@link Processor} for
 * serialization purpose and a {@link ResponseWrapper} to render error result or
 * adapt response format. This class implements {@link Serializer} interface.
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
	 * @param org
	 *            .restexpress.serialization
	 * @param wrapper
	 * @throws NullPointerException
	 *             if one of parameter is null
	 */
	public ResponseProcessor(final Processor processor, final ResponseWrapper wrapper) throws NullPointerException {
		super();
		this.processor = Preconditions.checkNotNull(processor, "Processor can't be null");
		this.wrapper = Preconditions.checkNotNull(wrapper, "ResponseWrapper can't be null");
		;
	}

	/**
	 * @return inner {@link Processor} instance.
	 */
	public Processor processor() {
		return processor;
	}

	/**
	 * @return inner {@link ResponseWrapper} instance.
	 */
	public ResponseWrapper wrapper() {
		return wrapper;
	}

	@Override
	public <T> T deserialize(final Request request, final Class<T> type) throws DeserializationException {
		return processor.read(request.getBody(), type);
	}

	/**
	 * Serialize response body if response is serialized.
	 * 
	 * @see org.restexpress.response.Serializer#serialize(org.restexpress.Response)
	 */
	@Override
	public void serialize(final Response response) throws SerializationException {
		if (response.isSerialized()) {
			ChannelBuffer content = ChannelBuffers.dynamicBuffer();
			processor.write(response.getBody(), content);
			response.setBody(content);
		}
	}

	@Override
	public String toString() {
		return "ResponseProcessor [processor=" + processor + ", wrapper=" + wrapper + "]";
	}

}
