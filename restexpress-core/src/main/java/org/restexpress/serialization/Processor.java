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

import org.jboss.netty.buffer.ChannelBuffer;
import org.restexpress.DeserializationException;
import org.restexpress.SerializationException;

/**
 * Processor define methods to manage:
 * <ul>
 * <li>supported media type</li>
 * <li>serialization into {@link ChannelBuffer}</li>
 * <li>deserialization from {@link ChannelBuffer}</li>
 * </ul>
 * 
 * @author <a href="mailto:jguibert@intelligents-ia.com" >Jerome Guibert</a>
 * 
 */
public interface Processor {

	/**
	 * @return a non empty {@link List} of supported media type.
	 */
	public List<String> supportedMediaTypes();

	/**
	 * @return the principal media type.
	 */
	public String mediaType();

	/**
	 * Write value into specified buffer.
	 * 
	 * @param value
	 * @param buffer
	 * @throws SerializationException
	 *             if error occurs
	 */
	public void write(Object value, ChannelBuffer buffer) throws SerializationException;

	/**
	 * Read object of specified type from buffer.
	 * 
	 * @param buffer
	 *            {@link ChannelBuffer} source
	 * @param valueType
	 *            expected type
	 * @return Object instance
	 * @throws DeserializationException
	 *             if error occurs
	 */
	public <T> T read(ChannelBuffer buffer, Class<T> valueType) throws DeserializationException;
}
