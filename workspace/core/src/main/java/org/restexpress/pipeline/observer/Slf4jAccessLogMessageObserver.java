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

import org.restexpress.pipeline.MessageObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * {@link Slf4jAccessLogMessageObserver} implements an {@link MessageObserver}
 * to create an access log using SLF4j.
 * 
 * @author <a href="mailto:jguibert@intelligents-ia.com" >Jerome Guibert</a>
 * 
 */
public class Slf4jAccessLogMessageObserver extends AbstractAccessLogMessageObserver {

	private final Logger logger;

	/**
	 * Build a new instance of {@link Slf4jAccessLogMessageObserver} using a
	 * logger created on {@link Slf4jAccessLogMessageObserver#getClass()}.
	 */
	public Slf4jAccessLogMessageObserver() {
		super();
		logger = LoggerFactory.getLogger(Slf4jAccessLogMessageObserver.class);
	}

	/**
	 * Build a new instance of {@link Slf4jAccessLogMessageObserver} .
	 * 
	 * @param logger
	 */
	public Slf4jAccessLogMessageObserver(Logger logger) {
		super();
		this.logger = logger;
	}

	@Override
	protected void access(String method, String url, String status, long duration) {
		if (duration >= 0) {
			logger.info("{} - {} - responded with {} in {} ms", method, url, status, duration);
		} else {
			logger.info("{} - {} - responded with {}", method, url, status);
		}
	}
}
