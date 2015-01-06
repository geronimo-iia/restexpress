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
 * NotFoundException.
 * 
 * @author <a href="mailto:jguibert@intelligents-ia.com" >Jerome Guibert</a>
 * 
 */
public class NotFoundException extends HttpRuntimeException {

	private static final long serialVersionUID = -1574740789203900506L;

	public NotFoundException() {
		super(HttpStatus.NOT_FOUND);
	}

	public NotFoundException(final String message) {
		super(HttpStatus.NOT_FOUND, message);
	}

	public NotFoundException(final Throwable cause) {
		super(HttpStatus.NOT_FOUND, cause);
	}

	public NotFoundException(final String message, final Throwable cause) {
		super(HttpStatus.NOT_FOUND, message, cause);
	}
}
