package org.restexpress.common.exception;

public enum Exceptions {
	;
	public static Throwable findRootCause(Throwable throwable) {
		Throwable cause = throwable;
		Throwable rootCause = null;
		while (cause != null) {
			rootCause = cause;
			cause = cause.getCause();
		}
		return rootCause;
	}
}
