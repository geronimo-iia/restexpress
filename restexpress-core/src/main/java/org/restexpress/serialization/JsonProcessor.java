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

import org.restexpress.domain.CharacterSet;
import org.restexpress.domain.MediaType;

/**
 * {@link JsonProcessor} define default supported {@link MediaType} in order of
 * priority:
 * <ul>
 * <li>MediaType.APPLICATION_JSON with CharacterSet.UTF_8</li>
 * <li>MediaType.APPLICATION_ALL_JSON with CharacterSet.UTF_8</li>
 * <li>MediaType.APPLICATION_JAVASCRIPT with CharacterSet.UTF_8</li>
 * <li>MediaType.TEXT_JAVASCRIPT with CharacterSet.UTF_8</li>
 * <li>MediaType.APPLICATION_HAL_JSON with CharacterSet.UTF_8</li>
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
		super(MediaType.APPLICATION_JSON.withCharset(CharacterSet.UTF_8.getCharsetName()));
		addMediaType(MediaType.APPLICATION_ALL_JSON.withCharset(CharacterSet.UTF_8.getCharsetName()));
		addMediaType(MediaType.APPLICATION_JAVASCRIPT.withCharset(CharacterSet.UTF_8.getCharsetName()));
		addMediaType(MediaType.TEXT_JAVASCRIPT.withCharset(CharacterSet.UTF_8.getCharsetName()));
		addMediaType(MediaType.APPLICATION_HAL_JSON.withCharset(CharacterSet.UTF_8.getCharsetName()));
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
