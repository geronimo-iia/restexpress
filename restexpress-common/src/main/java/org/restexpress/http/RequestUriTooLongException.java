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
package org.restexpress.http;


/**
 * RequestUriTooLongException.
 * 
 * @author <a href="mailto:jguibert@intelligents-ia.com" >Jerome Guibert</a>
 * 
 */
public class RequestUriTooLongException extends HttpRuntimeException {

	private static final long serialVersionUID = 4463345798955463381L;

	public RequestUriTooLongException() {
		super(HttpStatus.REQUEST_URI_TOO_LONG);
	}

	public RequestUriTooLongException(final String message) {
		super(HttpStatus.REQUEST_URI_TOO_LONG, message);
	}

	public RequestUriTooLongException(final Throwable cause) {
		super(HttpStatus.REQUEST_URI_TOO_LONG, cause);
	}

	public RequestUriTooLongException(final String message, final Throwable cause) {
		super(HttpStatus.REQUEST_URI_TOO_LONG, message, cause);
	}
}
