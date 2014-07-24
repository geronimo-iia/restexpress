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

import java.util.List;

import org.restexpress.domain.CharacterSet;
import org.restexpress.domain.MediaType;
import org.restexpress.serialization.AbstractProcessor;

/**
 * {@link JsonProcessor} define default supported {@link MediaType} in order of priority:
 * <ul>
 * <li>MediaType.APPLICATION_JSON with CharacterSet.UTF_8</li>
 * <li>MediaType.APPLICATION_JSON</li>
 * <li>MediaType.APPLICATION_ALL_JSON</li>
 * <li>MediaType.APPLICATION_JAVASCRIPT</li>
 * <li>MediaType.TEXT_JAVASCRIPT</li>
 * </ul>
 * 
 * @author <a href="mailto:jguibert@intelligents-ia.com" >Jerome Guibert</a>
 * 
 */
public abstract class JsonProcessor extends AbstractProcessor {
	/**
	 * Build a new instance of {@link JsonProcessor} with default
	 * {@link MediaType}.
	 */
	public JsonProcessor() {
		super(MediaType.APPLICATION_JSON.withCharset(CharacterSet.UTF_8.getCharsetName()), //
				MediaType.APPLICATION_JSON.getMime(),//
				MediaType.APPLICATION_ALL_JSON.getMime(), //
				MediaType.APPLICATION_JAVASCRIPT.getMime(),//
				MediaType.TEXT_JAVASCRIPT.getMime());
	}

	public JsonProcessor(String... mediaTypes) throws IllegalArgumentException {
		super(mediaTypes);
	}

	public JsonProcessor(List<String> mediaTypes) {
		super(mediaTypes);
	}

	public JsonProcessor(MediaType... mediaTypes) {
		super(mediaTypes);
	}

}
