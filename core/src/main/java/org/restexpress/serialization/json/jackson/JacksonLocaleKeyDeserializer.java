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
package org.restexpress.serialization.json.jackson;

import java.util.Locale;

import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdKeyDeserializer;

/**
 * Deserializer for class {@link Locale} used as a key in a Map.
 * 
 * @author LRI
 * @author <a href="mailto:jguibert@intelligents-ia.com" >Jerome Guibert</a>
 */
public class JacksonLocaleKeyDeserializer extends StdKeyDeserializer {

	private static final long serialVersionUID = -923987142048841491L;

	/**
	 * Build a new instance of JacksonLocaleKeyDeserializer.java.
	 */
	public JacksonLocaleKeyDeserializer() {
		super(Locale.class);
	}

	@Override
	protected Object _parse(final String key, final DeserializationContext ctxt) throws Exception {
		return JacksonLocaleDeserializer.parse(key);
	}
}
