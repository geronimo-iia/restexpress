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
package org.restexpress.serialization.json;

import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBufferInputStream;
import org.restexpress.ContentType;
import org.restexpress.Format;
import org.restexpress.serialization.DeserializationException;
import org.restexpress.serialization.SerializationException;

import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.strategicgains.util.date.DateAdapterConstants;

/**
 * A SerializationProcessor to handle JSON input/output. It anticipates ISO
 * 8601-compatible time points for date instances and outputs dates as ISO 8601
 * time points.
 * 
 * @author toddf
 * @since Mar 16, 2010
 */
public class JacksonJsonProcessor extends JsonSerializationProcessor {
	private final ObjectMapper mapper;

	public JacksonJsonProcessor() {
		this(Format.JSON);
	}

	public JacksonJsonProcessor(final String format) {
		super(format);
		mapper = initializeMapper(new ObjectMapper())//
				.registerModule(initializeModule());
	}

	public JacksonJsonProcessor(final ObjectMapper mapper) {
		super();
		this.mapper = mapper;
	}

	/**
	 * Template method for sub-classes to augment the module with desired
	 * serializers and/or deserializers. Sub-classes should call super() to get
	 * default settings.
	 */
	protected SimpleModule initializeModule() {
		return new SimpleModule().addSerializer(Date.class, new JacksonTimepointSerializer())//
				.addSerializer(String.class, new JacksonEncodingStringSerializer())//
				.addDeserializer(Date.class, new JacksonTimepointDeserializer());
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

	@Override
	public <T> T deserialize(final String string, final Class<T> type) {
		try {
			return ((string == null) || string.trim().isEmpty() ? null : mapper.readValue(string, type));
		} catch (final JsonProcessingException e) {
			throw new DeserializationException(e);
		} catch (final IOException e) {
			throw new DeserializationException(e);
		}
	}

	@Override
	public <T> T deserialize(final ChannelBuffer buffer, final Class<T> type) {
		try {
			return ((buffer == null) || (buffer.readableBytes() == 0) ? null : mapper.readValue(new InputStreamReader(new ChannelBufferInputStream(buffer), ContentType.CHARSET), type));
		} catch (final JsonProcessingException e) {
			throw new DeserializationException(e);
		} catch (final IOException e) {
			throw new DeserializationException(e);
		}
	}

	@Override
	public String serialize(final Object object) {
		try {
			return (object == null ? "" : mapper.writeValueAsString(object));
		} catch (final JsonProcessingException e) {
			throw new SerializationException(e);
		}
	}
}
