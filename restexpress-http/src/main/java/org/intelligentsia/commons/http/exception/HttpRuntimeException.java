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
package org.intelligentsia.commons.http.exception;

import org.intelligentsia.commons.http.status.HttpResponseStandardStatus;
import org.intelligentsia.commons.http.status.HttpResponseStatus;

/**
 * 
 * {@link HttpRuntimeException} is the root exception for all HTTP related.
 * 
 * @author <a href="mailto:jguibert@intelligents-ia.com" >Jerome Guibert</a>
 * 
 */
public class HttpRuntimeException extends RuntimeException {

	private static final long serialVersionUID = 3962170392582457984L;

	private HttpResponseStatus httpResponseStatus = HttpResponseStandardStatus.INTERNAL_SERVER_ERROR;

	public HttpRuntimeException() {
		super();
	}

	public HttpRuntimeException(final String message) {
		super(message);
	}

	public HttpRuntimeException(final Throwable cause) {
		super(cause);
	}

	public HttpRuntimeException(final String message, final Throwable cause) {
		super(message, cause);
	}

	public HttpRuntimeException(final HttpResponseStatus httpResponseStatus) {
		super();
		this.httpResponseStatus = httpResponseStatus;
	}

	public HttpRuntimeException(final HttpResponseStatus httpResponseStatus, final String message) {
		super(message);
		this.httpResponseStatus = httpResponseStatus;
	}

	public HttpRuntimeException(final HttpResponseStatus httpResponseStatus, final Throwable cause) {
		super(cause);
		this.httpResponseStatus = httpResponseStatus;
	}

	public HttpRuntimeException(final HttpResponseStatus httpResponseStatus, final String message, final Throwable cause) {
		super(message, cause);
		this.httpResponseStatus = httpResponseStatus;
	}

	/**
	 * @return associated {@link HttpResponseStatus}.
	 */
	public HttpResponseStatus getHttpResponseStatus() {
		return httpResponseStatus;
	}

}
