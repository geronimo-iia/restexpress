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
package org.restexpress.plugin.gson.json;

import java.io.InputStreamReader;
import java.util.Date;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBufferInputStream;
import org.restexpress.ContentType;
import org.restexpress.common.util.StringUtils;
import org.restexpress.serialization.json.JsonSerializationProcessor;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.strategicgains.util.date.DateAdapterConstants;

/**
 * A SerializationProcessor to handle JSON input/output using GSON. It
 * anticipates ISO 8601-compatible time points for date instances and outputs
 * dates as ISO 8601 time points.
 * 
 * @author toddf
 * @since Mar 16, 2010
 */
public class GsonJsonProcessor extends JsonSerializationProcessor {
	private final Gson gson;

	public GsonJsonProcessor() {
		super();
		gson = new GsonBuilder().disableHtmlEscaping().registerTypeAdapter(Date.class, new GsonTimepointSerializer()).setDateFormat(DateAdapterConstants.TIMESTAMP_OUTPUT_FORMAT).setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
				.create();
	}

	public GsonJsonProcessor(final Gson gson) {
		super();
		this.gson = gson;
	}

	// SECTION: SERIALIZATION PROCESSOR

	@Override
	public <T> T deserialize(final String string, final Class<T> type) {
		return gson.fromJson(string, type);
	}

	@Override
	public <T> T deserialize(final ChannelBuffer buffer, final Class<T> type) {
		return gson.fromJson(new InputStreamReader(new ChannelBufferInputStream(buffer), ContentType.CHARSET), type);
	}

	@Override
	public String serialize(final Object object) {
		if (object == null) {
			return StringUtils.EMPTY_STRING;
		}

		return gson.toJson(object);
	}
}
