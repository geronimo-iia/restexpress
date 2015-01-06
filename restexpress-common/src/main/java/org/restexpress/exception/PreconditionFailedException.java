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

import org.restexpress.http.status.HttpResponseStandardStatus;

/**
 * PreconditionFailedException.
 * 
 * @author <a href="mailto:jguibert@intelligents-ia.com" >Jerome Guibert</a>
 * 
 */
public class PreconditionFailedException extends HttpRuntimeException {

	private static final long serialVersionUID = -1991232020269814812L;

	public PreconditionFailedException() {
		super(HttpResponseStandardStatus.PRECONDITION_FAILED);
	}

	public PreconditionFailedException(final String message) {
		super(HttpResponseStandardStatus.PRECONDITION_FAILED, message);
	}

	public PreconditionFailedException(final Throwable cause) {
		super(HttpResponseStandardStatus.PRECONDITION_FAILED, cause);
	}

	public PreconditionFailedException(final String message, final Throwable cause) {
		super(HttpResponseStandardStatus.PRECONDITION_FAILED, message, cause);
	}
}
