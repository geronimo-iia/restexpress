package org.restexpress.pipeline.observer;

import org.restexpress.Request;
import org.restexpress.Response;
import org.restexpress.pipeline.MessageObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * {@link Slf4fErrorLogMessageObserver} implements a {@link MessageObserver} to
 * log error using SLF4J.
 * 
 * @author <a href="mailto:jguibert@intelligents-ia.com" >Jerome Guibert</a>
 * 
 */
public class Slf4fErrorLogMessageObserver extends BaseMessageObserver {

	private final Logger logger;

	/**
	 * Build a new instance of {@link Slf4fErrorLogMessageObserver} using a
	 * logger created on {@link Slf4fErrorLogMessageObserver#getClass()}.
	 */
	public Slf4fErrorLogMessageObserver() {
		super();
		logger = LoggerFactory.getLogger(Slf4fErrorLogMessageObserver.class);
	}

	/**
	 * Build a new instance of {@link Slf4fErrorLogMessageObserver} .
	 * 
	 * @param logger
	 */
	public Slf4fErrorLogMessageObserver(Logger logger) {
		super();
		this.logger = logger;
	}

	@Override
	public void onException(Throwable exception, Request request, Response response) {
		logger.error("restexpress error", exception);
	}
}
