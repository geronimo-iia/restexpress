package org.restexpress.common.response;

import java.io.Serializable;

import org.intelligentsia.commons.http.exception.Exceptions;
import org.intelligentsia.commons.http.exception.HttpRuntimeException;

/**
 * ErrorResult is a wrapper class to deal with restexpress error return.
 * 
 * @author <a href="mailto:jguibert@intelligents-ia.com" >Jerome Guibert</a>
 * 
 */
public class ErrorResult implements Serializable {

	private static final long serialVersionUID = -3700889923122706476L;
	private int httpStatus;
	private String message;
	private String errorType;

	public ErrorResult() {
		super();
	}

	public ErrorResult(final int httpStatusCode, final String message, final String errorType) {
		super();
		this.httpStatus = httpStatusCode;
		this.message = message;
		this.errorType = errorType;
	}

	/**
	 * @return an {@link HttpRuntimeException} associated with this error.
	 */
	public HttpRuntimeException toHttpRuntimeException() {
		return Exceptions.getExceptionFor(httpStatus, message, errorType != null ? new RuntimeException(errorType) : null);
	}

	@Override
	public String toString() {
		return "ErrorResult {httpStatusCode=\"" + httpStatus + "\", message=\"" + message + "\", errorType=\"" + errorType + "\"}";
	}

}
