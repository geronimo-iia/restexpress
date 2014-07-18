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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBufferInputStream;
import org.restexpress.Format;
import org.restexpress.common.response.JsendResult;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.converters.SingleValueConverter;

/**
 * The operation of these SerializationProcessor methods MUST match the behavior
 * of those in the DefaultJsonProcessor (and any other serialization
 * processors).
 * 
 * @author toddf
 * @since Mar 16, 2010
 */
public class XstreamXmlProcessor extends XmlSerializationProcessor {
	private final XStream xstream;
	private final Map<Class<?>, String> aliases = new HashMap<Class<?>, String>();
	private boolean shouldAutoAlias = true;

	public XstreamXmlProcessor() {
		this(Format.XML);
	}

	public XstreamXmlProcessor(final String format) {
		this(new XStream(), format);
		xstream.registerConverter(new XstreamTimestampConverter());
		xstream.alias("list", ArrayList.class);
		xstream.alias("response", JsendResult.class);
	}

	public XstreamXmlProcessor(final XStream xstream, final String format) {
		super(format);
		this.xstream = xstream;
		shouldAutoAlias = false;
	}

	protected XStream getXStream() {
		return this.xstream;
	}

	@Override
	public void alias(final String name, final Class<?> type) {
		xstream.alias(name, type);
	}

	public void registerConverter(final SingleValueConverter converter) {
		xstream.registerConverter(converter);
	}

	@Override
	public String serialize(final Object object) {
		if (object == null) {
			return "";
		}

		return xstream.toXML(object);
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T> T deserialize(final String xml, final Class<T> type) {
		if ((xml == null) || xml.trim().isEmpty()) {
			return null;
		}

		if (shouldAutoAlias) {
			addAliasIfNecessary(type);
		}

		return (T) xstream.fromXML(xml);
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T> T deserialize(final ChannelBuffer xml, final Class<T> type) {
		if (!xml.readable()) {
			return null;
		}

		return (T) xstream.fromXML(new ChannelBufferInputStream(xml));
	}

	private void addAliasIfNecessary(final Class<?> type) {
		if (!aliases.containsKey(type)) {
			final String name = type.getSimpleName().trim();

			if ("[]".equals(name) || "".equals(name)) {
				return;
			}

			xstream.alias(name, type);
		}
	}
}
