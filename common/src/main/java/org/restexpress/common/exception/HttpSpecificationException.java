package org.restexpress.common.exception;

import org.intelligentsia.commons.http.exception.HttpRuntimeException;
import org.intelligentsia.commons.http.status.HttpResponseStatus;

public class HttpSpecificationException  extends HttpRuntimeException{

	private static final long serialVersionUID = -3390366424725358082L;

	public HttpSpecificationException() {
		super();
	}

	public HttpSpecificationException(HttpResponseStatus httpResponseStatus, String message, Throwable cause) {
		super(httpResponseStatus, message, cause);
	}

	public HttpSpecificationException(HttpResponseStatus httpResponseStatus, String message) {
		super(httpResponseStatus, message);
	}

	public HttpSpecificationException(HttpResponseStatus httpResponseStatus, Throwable cause) {
		super(httpResponseStatus, cause);
	}

	public HttpSpecificationException(HttpResponseStatus httpResponseStatus) {
		super(httpResponseStatus);
	}

	public HttpSpecificationException(String message, Throwable cause) {
		super(message, cause);
	}

	public HttpSpecificationException(String message) {
		super(message);
	}

	public HttpSpecificationException(Throwable cause) {
		super(cause);
	}

	
}
