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

import org.restexpress.http.HttpStatus;

/**
 * GatewayTimeoutException.
 * 
 * @author <a href="mailto:jguibert@intelligents-ia.com" >Jerome Guibert</a>
 * 
 */
public class GatewayTimeoutException extends HttpRuntimeException {

	private static final long serialVersionUID = -2113715167053813305L;

	public GatewayTimeoutException() {
		super(HttpStatus.GATEWAY_TIMEOUT);
	}

	public GatewayTimeoutException(final String message) {
		super(HttpStatus.GATEWAY_TIMEOUT, message);
	}

	public GatewayTimeoutException(final Throwable cause) {
		super(HttpStatus.GATEWAY_TIMEOUT, cause);
	}

	public GatewayTimeoutException(final String message, final Throwable cause) {
		super(HttpStatus.GATEWAY_TIMEOUT, message, cause);
	}
}
