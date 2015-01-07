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
 * UnauthorizedException.
 * 
 * @author <a href="mailto:jguibert@intelligents-ia.com" >Jerome Guibert</a>
 * 
 */
public class UnauthorizedException extends HttpRuntimeException {

	private static final long serialVersionUID = 7935920301143675954L;

	public UnauthorizedException() {
		super(HttpStatus.UNAUTHORIZED);
	}

	public UnauthorizedException(final String message) {
		super(HttpStatus.UNAUTHORIZED, message);
	}

	public UnauthorizedException(final Throwable cause) {
		super(HttpStatus.UNAUTHORIZED, cause);
	}

	public UnauthorizedException(final String message, final Throwable cause) {
		super(HttpStatus.UNAUTHORIZED, message, cause);
	}
}
