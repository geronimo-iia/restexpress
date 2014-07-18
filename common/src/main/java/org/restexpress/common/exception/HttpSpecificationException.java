package org.restexpress.common.exception;

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
