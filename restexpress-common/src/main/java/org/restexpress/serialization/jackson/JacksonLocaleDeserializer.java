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
package org.restexpress.serialization.jackson;

import java.io.IOException;
import java.util.Locale;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

/**
 * JacksonLocaleDeserializer. Default locale is {@link Locale#ENGLISH}.
 * 
 * @author <a href="mailto:jguibert@intelligents-ia.com" >Jerome Guibert</a>
 */
public class JacksonLocaleDeserializer extends StdDeserializer<Locale> {
	/**
	 * serialVersionUID:long
	 */
	private static final long serialVersionUID = -23475403657913331L;
	/**
	 * Separator.
	 */
	public static final String SEPARATOR = "_";

	/**
	 * Build a new instance of JacksonLocaleDeserializer.java.
	 */
	public JacksonLocaleDeserializer() {
		super(Locale.class);
	}

	@Override
	public Locale deserialize(final JsonParser jp, final DeserializationContext ctxt) throws IOException, JsonProcessingException {
		final String locale = jp.readValueAs(String.class);
		return parse(locale);
	}

	/**
	 * Parse locale.
	 * 
	 * @param locale
	 * @return {@link Locale} instance.
	 */
	static Locale parse(final String locale) {
		final String[] localeSplitted = locale.split(JacksonLocaleDeserializer.SEPARATOR);
		if ((localeSplitted.length == 0) || (localeSplitted.length > 3)) { // malFormed
			return Locale.ENGLISH; // default Language
		}
		// load locale component
		final String language = localeSplitted[0];
		final String country = localeSplitted.length >= 2 ? localeSplitted[1] : "";
		final String variant = localeSplitted.length == 3 ? localeSplitted[2] : "";
		// default Language
		if (language.equals("")) {
			return Locale.ENGLISH;
		}
		return new Locale(language, country, variant);
	}

}
