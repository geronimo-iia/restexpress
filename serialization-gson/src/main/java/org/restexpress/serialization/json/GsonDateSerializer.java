/*
 * Copyright 2009, Strategic Gains, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 *
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.restexpress.serialization.json;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.util.Date;

import com.google.gson.InstanceCreator;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.strategicgains.util.date.DateAdapter;

/**
 * A GSON serializer for Date instances represented (and to be presented) as a
 * date (without time component).
 * 
 * @author Todd Fredrich
 * @since Nov 13, 2009
 * @see JsonSerializer
 * @see JsonDeserializer
 * @see InstanceCreator
 */
public class GsonDateSerializer implements GsonSerializer<Date> {
	private final DateAdapter adapter;

	public GsonDateSerializer() {
		this(new DateAdapter());
	}

	public GsonDateSerializer(final DateAdapter adapter) {
		super();
		this.adapter = adapter;
	}

	@Override
	public Date deserialize(final JsonElement json, final Type typeOf, final JsonDeserializationContext context) throws JsonParseException {
		try {
			return adapter.parse(json.getAsJsonPrimitive().getAsString());
		} catch (final ParseException e) {
			throw new JsonParseException(e);
		}
	}

	@Override
	public JsonElement serialize(final Date date, final Type typeOf, final JsonSerializationContext context) {
		return new JsonPrimitive(adapter.format(date));
	}

	@Override
	public Date createInstance(final Type typeOf) {
		return new Date();
	}
}
