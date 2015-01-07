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
package org.restexpress;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;

import org.restexpress.http.BadGatewayException;
import org.restexpress.http.BadRequestException;
import org.restexpress.http.ConflictException;
import org.restexpress.http.ExpectationFailedException;
import org.restexpress.http.ForbiddenException;
import org.restexpress.http.GatewayTimeoutException;
import org.restexpress.http.HttpRuntimeException;
import org.restexpress.http.HttpStatus;
import org.restexpress.http.HttpVersionNotSupportedException;
import org.restexpress.http.InternalServerErrorException;
import org.restexpress.http.MethodNotAllowedException;
import org.restexpress.http.NotAcceptableException;
import org.restexpress.http.NotFoundException;
import org.restexpress.http.PreconditionFailedException;
import org.restexpress.http.RequestEntityTooLargeException;
import org.restexpress.http.RequestTimeoutException;
import org.restexpress.http.RequestUriTooLongException;
import org.restexpress.http.ServiceUnavailableException;
import org.restexpress.http.UnauthorizedException;
import org.restexpress.http.UnprocessableEntityException;
import org.restexpress.http.UnsupportedMediaTypeException;

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
	public static Throwable findRootCause(final Throwable throwable) {
		Throwable cause = throwable;
		Throwable rootCause = null;
		while (cause != null) {
			rootCause = cause;
			cause = cause.getCause();
		}
		return rootCause;
	}

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
			return new HttpRuntimeException(HttpStatus.valueOf(code), message, cause);
		}
	}
	

	/**
	 * Join a collection of string with a delimiter.
	 * @param delimiter
	 * @param objects
	 * @return a String.
	 */
	public static String join(final String delimiter, final Collection<? extends Object> objects) {
		if ((objects == null) || objects.isEmpty()) {
			return "";
		}

		final Iterator<? extends Object> iterator = objects.iterator();
		final StringBuilder builder = new StringBuilder();
		builder.append(iterator.next());

		while (iterator.hasNext()) {
			builder.append(delimiter).append(iterator.next());
		}

		return builder.toString();
	}

	public static String join(final String delimiter, final Object... objects) {
		return join(delimiter, Arrays.asList(objects));
	}
}
