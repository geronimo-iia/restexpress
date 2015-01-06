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
package org.restexpress.http.status;

import java.io.Serializable;

/**
 * DefaultHttpResponseStatus declare all http response status which are not in
 * standard definition (@see {@link HttpResponseStandardStatus} (but valid).
 * 
 * @see idea based on netty (netty.io, see notice fomr more information)
 * 
 * @author <a href="mailto:jguibert@intelligents-ia.com" >Jerome Guibert</a>
 */
public class DefaultHttpResponseStatus implements Serializable, HttpResponseStatus {

	private static final long serialVersionUID = 3603787056797696909L;

	private final int code;

	private final String reasonPhrase;

	/**
	 * Creates a new instance with the specified {@code code} and its
	 * {@code reasonPhrase}.
	 * 
	 * @param code
	 * @param reasonPhrase
	 */
	public DefaultHttpResponseStatus(final HttpResponseStandardStatusCode code, final String reasonPhrase) {
		this(code.getCode(), reasonPhrase);
	}

	/**
	 * Creates a new instance with the specified {@code code} and its
	 * {@code reasonPhrase}.
	 */
	public DefaultHttpResponseStatus(final int code, final String reasonPhrase) {
		if (code < 0) {
			throw new IllegalArgumentException("code: " + code + " (expected: 0+)");
		}

		if (reasonPhrase == null) {
			throw new NullPointerException("reasonPhrase");
		}

		for (int i = 0; i < reasonPhrase.length(); i++) {
			final char c = reasonPhrase.charAt(i);
			// Check prohibited characters.
			switch (c) {
			case '\n':
			case '\r':
				throw new IllegalArgumentException("reasonPhrase contains one of the following prohibited characters: " + "\\r\\n: " + reasonPhrase);
			}
		}

		this.code = code;
		this.reasonPhrase = reasonPhrase;
	}

	/**
	 * Returns the code of this status.
	 */
	@Override
	public int getCode() {
		return code;
	}

	/**
	 * Returns the reason phrase of this status.
	 */
	@Override
	public String getReasonPhrase() {
		return reasonPhrase;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = (prime * result) + code;
		return result;
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final DefaultHttpResponseStatus other = (DefaultHttpResponseStatus) obj;
		if (code != other.code) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "DefaultHttpResponseStatus {code=\"" + code + "\", reasonPhrase=\"" + reasonPhrase + "}";
	}

}
