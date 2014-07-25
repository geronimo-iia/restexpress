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
