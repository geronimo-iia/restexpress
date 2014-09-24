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

/**
 * ServiceUnavailableException.
 * 
 * @author <a href="mailto:jguibert@intelligents-ia.com" >Jerome Guibert</a>
 * 
 */
public class ServiceUnavailableException extends HttpRuntimeException {

	private static final long serialVersionUID = 3732813253500357763L;

	public ServiceUnavailableException() {
		super(HttpResponseStandardStatus.SERVICE_UNAVAILABLE);
	}

	public ServiceUnavailableException(final String message) {
		super(HttpResponseStandardStatus.SERVICE_UNAVAILABLE, message);
	}

	public ServiceUnavailableException(final Throwable cause) {
		super(HttpResponseStandardStatus.SERVICE_UNAVAILABLE, cause);
	}

	public ServiceUnavailableException(final String message, final Throwable cause) {
		super(HttpResponseStandardStatus.SERVICE_UNAVAILABLE, message, cause);
	}
}
