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

import org.intelligentsia.commons.http.exception.HttpRuntimeException;
import org.intelligentsia.commons.http.status.HttpResponseStatus;

public class HttpSpecificationException extends HttpRuntimeException {

	private static final long serialVersionUID = -3390366424725358082L;

	public HttpSpecificationException() {
		super();
	}

	public HttpSpecificationException(final HttpResponseStatus httpResponseStatus, final String message, final Throwable cause) {
		super(httpResponseStatus, message, cause);
	}

	public HttpSpecificationException(final HttpResponseStatus httpResponseStatus, final String message) {
		super(httpResponseStatus, message);
	}

	public HttpSpecificationException(final HttpResponseStatus httpResponseStatus, final Throwable cause) {
		super(httpResponseStatus, cause);
	}

	public HttpSpecificationException(final HttpResponseStatus httpResponseStatus) {
		super(httpResponseStatus);
	}

	public HttpSpecificationException(final String message, final Throwable cause) {
		super(message, cause);
	}

	public HttpSpecificationException(final String message) {
		super(message);
	}

	public HttpSpecificationException(final Throwable cause) {
		super(cause);
	}

}
