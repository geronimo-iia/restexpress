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
import java.text.ParseException;
import java.util.Date;

import org.owasp.encoder.Encode;
import org.restexpress.exception.DeserializationException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.strategicgains.util.date.DateAdapter;
import com.strategicgains.util.date.Iso8601TimepointAdapter;
import com.strategicgains.util.date.TimestampAdapter;

/**
 * JacksonJsonModule.
 * 
 * @author <a href="mailto:jguibert@intelligents-ia.com" >Jerome Guibert</a>
 * 
 */
public class JacksonJsonModule extends SimpleModule {

	private static final long serialVersionUID = 5607854920381206410L;

	public JacksonJsonModule() {
		super();
		addSerializer(String.class, new JacksonEncodingStringSerializer());
		addSerializer(Date.class, new JacksonTimepointSerializer());
		addDeserializer(Date.class, new JacksonTimepointDeserializer());
	}

	public static class JacksonEncodingStringSerializer extends JsonSerializer<String> {

		@Override
		public void serialize(final String value, final JsonGenerator jgen, final SerializerProvider provider) throws IOException, JsonProcessingException {
			jgen.writeString(Encode.forXmlContent(value));
		}
	}

	public static class JacksonTimepointSerializer extends JsonSerializer<Date> {
		private final DateAdapter adapter;

		public JacksonTimepointSerializer() {
			this(new TimestampAdapter());
		}

		public JacksonTimepointSerializer(final DateAdapter adapter) {
			super();
			this.adapter = adapter;
		}

		@Override
		public void serialize(final Date date, final JsonGenerator gen, final SerializerProvider sp) throws IOException, JsonProcessingException {
			gen.writeString(adapter.format(date));
		}
	}

	public static class JacksonTimepointDeserializer extends JsonDeserializer<Date> {
		private final DateAdapter adapter;

		public JacksonTimepointDeserializer() {
			this(new Iso8601TimepointAdapter());
		}

		public JacksonTimepointDeserializer(final DateAdapter adapter) {
			super();
			this.adapter = adapter;
		}

		@Override
		public Date deserialize(final JsonParser parser, final DeserializationContext context) throws IOException, JsonProcessingException {
			try {
				return adapter.parse(parser.getText());
			} catch (final ParseException e) {
				throw new DeserializationException(e);
			}
		}
	}

}
