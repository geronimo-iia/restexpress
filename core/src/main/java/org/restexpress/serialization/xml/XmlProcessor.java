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
package org.restexpress.serialization.xml;

import java.util.List;

import org.restexpress.domain.CharacterSet;
import org.restexpress.domain.MediaType;
import org.restexpress.serialization.AbstractProcessor;

/**
 * {@link XmlProcessor} define default supported {@link MediaType}:
 * <ul>
 * <li>MediaType.APPLICATION_XML with CharacterSet.UTF_8</li>
 * <li>MediaType.APPLICATION_ALL_XML, with CharacterSet.UTF_8</li>
 * <li>MediaType.TEXT_XML, with CharacterSet.UTF_8</li>
 * <li>MediaType.APPLICATION_XML</li>
 * <li>MediaType.APPLICATION_ALL_XML</li>
 * <li>MediaType.TEXT_XML</li>
 * </ul>
 * 
 * @author <a href="mailto:jguibert@intelligents-ia.com" >Jerome Guibert</a>
 * 
 */
public abstract class XmlProcessor extends AbstractProcessor implements Aliasable {

	/**
	 * Build a new instance of {@link XmlProcessor} with default
	 * {@link MediaType}.
	 */
	public XmlProcessor() {
		super(MediaType.APPLICATION_XML.withCharset(CharacterSet.UTF_8.getCharsetName()));
		addMediaType(MediaType.APPLICATION_ALL_XML.withCharset(CharacterSet.UTF_8.getCharsetName()));
		addMediaType(MediaType.TEXT_XML.withCharset(CharacterSet.UTF_8.getCharsetName()));
		addMediaType(MediaType.APPLICATION_XML.getMime());
		addMediaType(MediaType.APPLICATION_ALL_XML.getMime());
		addMediaType(MediaType.TEXT_XML.getMime());
	}

	public XmlProcessor(String... mediaTypes) throws IllegalArgumentException {
		super(mediaTypes);
	}

	public XmlProcessor(List<String> supportedMediaType) {
		super(supportedMediaType);
	}

	public XmlProcessor(MediaType... mediaTypes) {
		super(mediaTypes);
	}

}
