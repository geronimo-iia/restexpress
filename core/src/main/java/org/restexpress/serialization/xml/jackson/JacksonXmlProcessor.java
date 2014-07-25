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
package org.restexpress.serialization.xml.jackson;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;

import org.jboss.netty.buffer.ChannelBuffer;
import org.restexpress.domain.MediaType;
import org.restexpress.exception.DeserializationException;
import org.restexpress.exception.SerializationException;
import org.restexpress.serialization.json.jackson.RestExpressJacksonJsonModule;
import org.restexpress.serialization.xml.XmlProcessor;

import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.datatype.guava.GuavaModule;
import com.fasterxml.jackson.datatype.joda.JodaModule;
import com.strategicgains.util.date.DateAdapterConstants;

public class JacksonXmlProcessor extends XmlProcessor {

	private final ObjectMapper objectMapper;

	/**
	 * Build a new instance.
	 */
	public JacksonXmlProcessor() {
		super();
		this.objectMapper = newObjectMapper();
	}

	public JacksonXmlProcessor(List<String> supportedMediaType) {
		super(supportedMediaType);
		this.objectMapper = newObjectMapper();
	}

	public JacksonXmlProcessor(MediaType... mediaTypes) {
		super(mediaTypes);
		this.objectMapper = newObjectMapper();
	}

	public JacksonXmlProcessor(String... mediaTypes) throws IllegalArgumentException {
		super(mediaTypes);
		this.objectMapper = newObjectMapper();
	}

	public JacksonXmlProcessor(ObjectMapper objectMapper, List<String> mediaTypes) throws IllegalArgumentException {
		super(mediaTypes);
		this.objectMapper = objectMapper;
	}

	public JacksonXmlProcessor(ObjectMapper objectMapper, MediaType... mediaTypes) throws IllegalArgumentException {
		super(mediaTypes);
		this.objectMapper = objectMapper;
	}

	public JacksonXmlProcessor(ObjectMapper objectMapper, String... mediaTypes) throws IllegalArgumentException {
		super(mediaTypes);
		this.objectMapper = objectMapper;
	}

	@Override
	public <T> T read(ChannelBuffer buffer, Class<T> valueType) throws DeserializationException {
		try {
			return ((buffer == null) || (buffer.readableBytes() == 0) ? null : objectMapper.readValue(getInputStreamReader(buffer), valueType));
		} catch (final JsonProcessingException e) {
			throw new DeserializationException(e);
		} catch (final IOException e) {
			throw new DeserializationException(e);
		}
	}

	@Override
	public void write(Object value, ChannelBuffer buffer) throws SerializationException {
		try {
			if (value != null) {
				objectMapper.writeValue(getOutputStreamWriter(buffer), value);
			}
		} catch (final JsonProcessingException e) {
			throw new SerializationException(e);
		} catch (IOException e) {
			throw new SerializationException(e);
		}
	}

	@Override
	public void alias(String name, Class<?> theClass) {
	//	((XmlMapper) objectMapper).
//		xstream.alias("list", ArrayList.class);
//		xstream.alias("response", JsendResult.class);
	}

	/**
	 * Template method for sub-classes to augment the mapper with desired
	 * settings. Sub-classes should call super() to get default settings.
	 * 
	 * @param module
	 *            a SimpleModule
	 */
	protected ObjectMapper initializeMapper(final ObjectMapper mapper) {
		return mapper
		// .enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT)
				.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS)
				// Ignore additional/unknown properties in a payload.
				.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
				// Only serialize populated properties (do no serialize nulls)
				.setSerializationInclusion(JsonInclude.Include.NON_NULL)
				// Use fields directly.
				.setVisibility(PropertyAccessor.FIELD, Visibility.ANY)
				// Ignore accessor and mutator methods (use fields per above).
				.setVisibility(PropertyAccessor.GETTER, Visibility.NONE)//
				.setVisibility(PropertyAccessor.SETTER, Visibility.NONE)//
				.setVisibility(PropertyAccessor.IS_GETTER, Visibility.NONE)
				// Set default date output format.
				.setDateFormat(new SimpleDateFormat(DateAdapterConstants.TIME_POINT_OUTPUT_FORMAT));
	}

	protected ObjectMapper newObjectMapper() {
		return initializeMapper(new XmlMapper())//
				.registerModule(new RestExpressJacksonJsonModule())//
				.registerModule(new GuavaModule())//
				.registerModule(new JodaModule());
	}
}
