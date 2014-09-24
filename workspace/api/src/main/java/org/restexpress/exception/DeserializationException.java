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
package org.restexpress.exception;

/**
 * @author toddf
 * @since Nov 20, 2009
 */
public class DeserializationException extends RuntimeException {
	private static final long serialVersionUID = 4563135167976559696L;

	public DeserializationException() {
		super();
	}

	/**
	 * @param message
	 */
	public DeserializationException(final String message) {
		super(message);
	}

	/**
	 * @param cause
	 */
	public DeserializationException(final Throwable cause) {
		super(cause);
	}

	/**
	 * @param message
	 * @param cause
	 */
	public DeserializationException(final String message, final Throwable cause) {
		super(message, cause);
	}
}
