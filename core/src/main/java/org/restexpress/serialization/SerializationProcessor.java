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
import org.restexpress.contenttype.MediaRange;

/**
 * {@link SerializationProcessor} define methods to deal with
 * serialization/deserialization
 * 
 * 
 * @author toddf
 * @since Mar 16, 2010
 */
public interface SerializationProcessor {

	/**
	 * @param object
	 * @return String representation
	 */
	public String serialize(Object object);

	/**
	 * @return an unmodifiable {@link List} of supported {@link MediaRange}.
	 */
	public List<MediaRange> getSupportedMediaRanges();

	/**
	 * @return supported format.
	 */
	public String getSupportedFormat();

	/**
	 * TODO remove this method used for testing only.
	 * 
	 * @param string
	 *            string source value
	 * @param type
	 *            expected type
	 * @return deserialized object
	 */
	public <T> T deserialize(String string, Class<T> type);

	/**
	 * @param buffer
	 *            {@link ChannelBuffer} source
	 * @param type
	 *            expected type
	 * @return Object instance
	 */
	public <T> T deserialize(ChannelBuffer buffer, Class<T> type);
}
