/**
 * ====
 *            Licensed to the Apache Software Foundation (ASF) under one
 *            or more contributor license agreements.  See the NOTICE file
 *            distributed with this work for additional information
 *            regarding copyright ownership.  The ASF licenses this file
 *            to you under the Apache License, Version 2.0 (the
 *            "License"); you may not use this file except in compliance
 *            with the License.  You may obtain a copy of the License at
 *
 *              http://www.apache.org/licenses/LICENSE-2.0
 *
 *            Unless required by applicable law or agreed to in writing,
 *            software distributed under the License is distributed on an
 *            "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *            KIND, either express or implied.  See the License for the
 *            specific language governing permissions and limitations
 *            under the License.
 *
 * ====
 *
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
 * Exceptions.
 * 
 * methods {@link Exceptions#getExceptionFor(int, String, Throwable)} return
 * associated {@link HttpRuntimeException} with code.
 * 
 * @author <a href="mailto:jguibert@intelligents-ia.com" >Jerome Guibert</a>
 */
public enum Exceptions {
	;

	/**
	 * @param code
	 *            http response code
	 * @param message
	 *            message
	 * @param cause
	 *            error cause
	 * @return a {@link HttpRuntimeException} instance associated with http
	 *         error code.
	 */
	public static HttpRuntimeException getExceptionFor(final int code, final String message, final Throwable cause) {
		switch (code) {
		case 502:
			return new BadGatewayException(message, cause);
		case 400:
			return new BadRequestException(message, cause);
		case 409:
			return new ConflictException(message, cause);
		case 417:
			return new ExpectationFailedException(message, cause);
		case 403:
			return new ForbiddenException(message, cause);
		case 504:
			return new GatewayTimeoutException(message, cause);
		case 505:
			return new HttpVersionNotSupportedException(message, cause);
		case 500:
			return new InternalServerErrorException(message, cause);
		case 405:
			return new MethodNotAllowedException(message, cause);
		case 406:
			return new NotAcceptableException(message, cause);
		case 404:
			return new NotFoundException(message, cause);
		case 412:
			return new PreconditionFailedException(message, cause);
		case 413:
			return new RequestEntityTooLargeException(message, cause);
		case 408:
			return new RequestTimeoutException(message, cause);
		case 414:
			return new RequestUriTooLongException(message, cause);
		case 503:
			return new ServiceUnavailableException(message, cause);
		case 401:
			return new UnauthorizedException(message, cause);
		case 422:
			return new UnprocessableEntityException(message, cause);
		case 415:
			return new UnsupportedMediaTypeException(message, cause);
		default:
			return new HttpRuntimeException(HttpResponseStandardStatus.valueOf(code), message, cause);
		}
	}

}
