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
package org.restexpress.response;

import org.jboss.netty.buffer.ChannelBuffer;
import org.restexpress.Request;
import org.restexpress.Response;
import org.restexpress.exception.DeserializationException;
import org.restexpress.exception.SerializationException;

/**
 * {@link Serializer} define methods to serialize and deserialize data from/to
 * {@link Request} and {@link Response}.
 * 
 * @author <a href="mailto:jguibert@intelligents-ia.com" >Jerome Guibert</a>
 * 
 */
public interface Serializer {

	/**
	 * Deserialize specified class type from {@link Request}.
	 * 
	 * @param request
	 *            {@link Request}
	 * @param type
	 *            class type
	 * @return object instance.
	 * @throws DeserializationException
	 *             if an error occurs
	 */
	public <T> T deserialize(final Request request, final Class<T> type) throws DeserializationException;

	/**
	 * Serialize {@link Response}.
	 * 
	 * @param response
	 *            {@link Response} instance.
	 * @return {@link ChannelBuffer} or null if no object was found.
	 * @throws SerializationException
	 *             if an error occurs
	 */
	public ChannelBuffer serialize(final Response response) throws SerializationException;
}
