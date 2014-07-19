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
    Copyright 2011, Strategic Gains, Inc.

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
package org.restexpress.plugin.gson.json;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Calendar;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.junit.Test;
import org.restexpress.ContentType;
import org.restexpress.plugin.gson.KnownObject;
import org.restexpress.plugin.gson.json.GsonJsonProcessor;
import org.restexpress.serialization.SerializationProcessor;

/**
 * @author toddf
 * @since Aug 4, 2011
 */
public class GsonJsonProcessorTest {
	private static final String JSON = "{\"integer\":2,\"string\":\"another string value\",\"date\":\"1963-12-06T12:30:00.000Z\",\"p\":\"good stuff\"}";
	private static final String JSON_UTF8 = "{\"integer\":2,\"string\":\"????????????\",\"date\":\"1963-12-06T12:30:00.000Z\"}";

	private final SerializationProcessor processor = new GsonJsonProcessor();

	@Test
	public void shouldSerializeObject() {
		final String json = processor.serialize(new KnownObject());
		// System.out.println(json);
		assertNotNull(json);
		assertTrue(json.startsWith("{"));
		assertTrue(json.contains("\"integer\":1"));
		assertTrue(json.contains("\"string\":\"string value\""));
		assertTrue(json.contains("\"date\":\"1964-12-17T"));
		assertTrue(json.contains("\"p\":\"something private"));
		assertFalse(json.contains("\"q\":"));
		assertTrue(json.endsWith("}"));
	}

	@Test
	public void shouldSerializeNull() {
		final String json = processor.serialize(null);
		assertEquals("", json);
	}

	@Test
	public void shouldDeserializeObject() {
		final KnownObject o = processor.deserialize(JSON, KnownObject.class);
		assertNotNull(o);
		assertTrue(o.getClass().isAssignableFrom(KnownObject.class));
		assertEquals(2, o.integer);
		assertEquals("another string value", o.string);
		final Calendar c = Calendar.getInstance();
		c.setTime(o.date);
		assertEquals(11, c.get(Calendar.MONTH));
		assertEquals(6, c.get(Calendar.DAY_OF_MONTH));
		assertEquals(1963, c.get(Calendar.YEAR));
		assertEquals("good stuff", o.getP());
	}

	@Test
	public void shouldDeserializeEmptyObject() {
		final KnownObject o = processor.deserialize("{}", KnownObject.class);
		assertNotNull(o);
		assertTrue(o.getClass().isAssignableFrom(KnownObject.class));
	}

	@Test
	public void shouldDeserializeEmptyString() {
		final Object o = processor.deserialize("", KnownObject.class);
		assertNull(o);
	}

	@Test
	public void shouldDeserializeNullString() {
		final Object o = processor.deserialize((String) null, KnownObject.class);
		assertNull(o);
	}

	@Test
	public void shouldDeserializeChannelBuffer() {
		final ChannelBuffer buf = ChannelBuffers.copiedBuffer(JSON, ContentType.CHARSET);
		final Object o = processor.deserialize(buf, KnownObject.class);
		assertNotNull(o);
	}

	@Test
	public void shouldDeserializeEmptyChannelBuffer() {
		final ChannelBuffer buf = ChannelBuffers.EMPTY_BUFFER;
		final Object o = processor.deserialize(buf, KnownObject.class);
		assertNull(o);
	}

	@Test
	public void shouldDeserializeUTF8ChannelBuffer() {
		final KnownObject o = processor.deserialize(ChannelBuffers.wrappedBuffer(JSON_UTF8.getBytes(ContentType.CHARSET)), KnownObject.class);
		assertNotNull(o);
		assertTrue(o.getClass().isAssignableFrom(KnownObject.class));
		assertEquals(2, o.integer);
		assertEquals("????????????", o.string);
		final Calendar c = Calendar.getInstance();
		c.setTime(o.date);
		assertEquals(11, c.get(Calendar.MONTH));
		assertEquals(6, c.get(Calendar.DAY_OF_MONTH));
		assertEquals(1963, c.get(Calendar.YEAR));
	}
}
