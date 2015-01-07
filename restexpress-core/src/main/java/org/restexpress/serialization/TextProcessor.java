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
package org.restexpress.serialization;

import java.util.List;

import org.jboss.netty.buffer.ChannelBuffer;
import org.restexpress.DeserializationException;
import org.restexpress.SerializationException;
import org.restexpress.domain.CharacterSet;
import org.restexpress.domain.MediaType;

/**
 * {@link TextProcessor} implement a {@link Processor} for all text/* media
 * type.
 * 
 * @author <a href="mailto:jguibert@intelligents-ia.com" >Jerome Guibert</a>
 * 
 */
public class TextProcessor extends AbstractProcessor {

	public TextProcessor() {
		super(MediaType.TEXT_ALL.withCharset(CharacterSet.UTF_8.getCharsetName()));
	}

	public TextProcessor(MediaType... mediaTypes) throws IllegalArgumentException {
		super(mediaTypes);
	}

	public TextProcessor(String... mediaTypes) throws IllegalArgumentException {
		super(mediaTypes);
	}

	public TextProcessor(List<String> mediaTypes) throws IllegalArgumentException {
		super(mediaTypes);
	}

	@Override
	public void write(Object value, ChannelBuffer buffer) throws SerializationException {
		if (value != null) {
			buffer.writeBytes(value.toString().getBytes(CharacterSet.UTF_8.getCharset()));
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T read(ChannelBuffer buffer, Class<T> valueType) throws DeserializationException {
		if (!String.class.getName().equals(valueType.getName()))
			throw new DeserializationException("Only String class is supported");
		return (T) buffer.toString(CharacterSet.UTF_8.getCharset());
	}

}
