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
package org.restexpress.response;

import org.intelligentsia.commons.http.exception.HttpRuntimeException;
import org.restexpress.Response;
import org.restexpress.domain.response.ErrorResult;
import org.restexpress.domain.response.JsendResult;
import org.restexpress.domain.response.JsendResult.State;
import org.restexpress.exception.Exceptions;

/**
 * {@link Wrapper} utility class to define instance of {@link ResponseWrapper}.
 * 
 * @author <a href="mailto:jguibert@intelligents-ia.com" >Jerome Guibert</a>
 * 
 */
public enum Wrapper {
	;

	/**
	 * @return a new instance of {@link JsendResponseWrapper}.
	 */
	public static ResponseWrapper newJsendResponseWrapper() {
		return new JsendResponseWrapper();
	}

	/**
	 * @return a new instance of {@link ErrorResponseWrapper}.
	 */
	public static ResponseWrapper newErrorResponseWrapper() {
		return new ErrorResponseWrapper();
	}

	/**
	 * @return a new instance of {@link RawResponseWrapper}.
	 */
	public static ResponseWrapper newRawResponseWrapper() {
		return new RawResponseWrapper();
	}

	/**
	 * {@link JsendResponseWrapper} wraps the out bound Response body in a
	 * JSEND-style object. This wrapper should be only used in JSON format.
	 * 
	 * @author <a href="mailto:jguibert@intelligents-ia.com" >Jerome Guibert</a>
	 * @author toddf
	 * @since Feb 10, 2011
	 */
	public static class JsendResponseWrapper implements ResponseWrapper {
		@Override
		public Object wrap(final Response response) {
			if (response.hasException()) {
				final Throwable exception = response.getException();
				final Throwable rootCause = Exceptions.findRootCause(exception);
				final String message = (rootCause != null ? rootCause.getMessage() : exception.getMessage());
				final String causeName = (rootCause != null ? rootCause.getClass().getSimpleName() : exception.getClass().getSimpleName());

				if (HttpRuntimeException.class.isAssignableFrom(exception.getClass())) {
					return new JsendResult(State.ERROR, message, causeName);
				}

				return new JsendResult(State.FAIL, message, causeName);
			}
			final int code = response.getResponseStatus().getCode();
			if ((code >= 400) && (code < 500)) {
				return new JsendResult(State.ERROR, null, response.getBody());
			}
			if ((code >= 500) && (code < 600)) {
				return new JsendResult(State.FAIL, null, response.getBody());
			}
			return new JsendResult(State.SUCCESS, null, response.getBody());
		}

		@Override
		public String toString() {
			return "JsendResponseWrapper";
		}

	}

	/**
	 * {@link ErrorResponseWrapper} leaves the response alone, returning it
	 * without wrapping it at all, unless there is an exception.
	 * <p>
	 * If there is an exception, the exception is returned as an
	 * {@link ErrorResult} instance, serialization will be forced.
	 * </p>
	 * 
	 * @author <a href="mailto:jguibert@intelligents-ia.com" >Jerome Guibert</a>
	 */
	public static class ErrorResponseWrapper implements ResponseWrapper {
		@Override
		public Object wrap(final Response response) {
			if (addsBodyContent(response)) {
				response.setIsSerialized(true);
				if (response.hasException()) {
					final Throwable exception = response.getException();
					final Throwable rootCause = Exceptions.findRootCause(exception);
					final String message = (rootCause != null ? rootCause.getMessage() : exception.getMessage());
					String causeName = null;
					causeName = (rootCause != null ? rootCause.getClass().getSimpleName() : exception.getClass().getSimpleName());
					return new ErrorResult(response.getResponseStatus().getCode(), message, causeName);
				}
				return new ErrorResult(response.getResponseStatus().getCode(), null, null);
			}
			return response.getBody();
		}

		public static boolean addsBodyContent(final Response response) {
			if (response.hasException()) {
				return true;
			}
			final int code = response.getResponseStatus().getCode();
			return ((code >= 400) && (code < 600));
		}

		@Override
		public String toString() {
			return "ErrorResponseWrapper";
		}

	}

	/**
	 * {@link RawResponseWrapper} leaves the response alone, returning it
	 * without wrapping it at all, unless there is an exception.
	 * <p>
	 * If there is an exception, the exception is returned as a simple message,
	 * content type will be set in text/plain with no serialization.
	 * </p>
	 * 
	 * 
	 * @author <a href="mailto:jguibert@intelligents-ia.com" >Jerome Guibert</a>
	 * @author toddf
	 * @since Feb 10, 2011
	 */
	public static class RawResponseWrapper implements ResponseWrapper {

		@Override
		public Object wrap(final Response response) {
			if (!response.hasException()) {
				return response.getBody();
			}
			response.setIsSerialized(false);
			return response.getException().getMessage();
		}

		@Override
		public String toString() {
			return "RawResponseWrapper";
		}

	}

}
