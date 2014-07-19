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
/*
    Copyright 2013, Strategic Gains, Inc.

	Licensed under the Apache License, Version 2.0 (the "License");
	you may not use this file except in compliance with the License.
	You may obtain a copy of the License at

		http://www.apache.org/licenses/LICENSE-2.0

	Unless required by applicable law or agreed to in writing, software
	distributed under the License is distributed on an "AS IS" BASIS,
	WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
	See the License for the specific language governing permissions and
	limitations under the License.
 */
package org.restexpress.serialization.xml;

import java.util.Arrays;
import java.util.List;

import org.restexpress.ContentType;
import org.restexpress.Format;
import org.restexpress.common.util.StringUtils;
import org.restexpress.contenttype.MediaTypeParser;
import org.restexpress.serialization.AbstractSerializationProcessor;
import org.restexpress.serialization.Aliasable;
import org.restexpress.serialization.SerializationProcessor;

/**
 * {@link XmlSerializationProcessor} define an abstract XML processor and
 * implements {@link Aliasable} and {@link SerializationProcessor}.
 * 
 * @author toddf
 * @since Jul 18, 2013
 */
public abstract class XmlSerializationProcessor extends AbstractSerializationProcessor implements Aliasable, SerializationProcessor {

	private static final String SUPPORTED_MEDIA_TYPES = StringUtils.join(",", ContentType.XML, "text/xml; charset=" + ContentType.ENCODING);

	public XmlSerializationProcessor() {
		this(Arrays.asList(Format.XML));
	}

	public XmlSerializationProcessor(final String format) {
		this(Arrays.asList(format));
	}

	public XmlSerializationProcessor(final List<String> supportedFormats) {
		super(supportedFormats, MediaTypeParser.parse(SUPPORTED_MEDIA_TYPES));
	}
}