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

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.restexpress.Request;
import org.restexpress.Response;

/**
 * {@link AbstractAccessLogMessageObserver} is base class to build "access log".
 * 
 * @author <a href="mailto:jguibert@intelligents-ia.com" >Jerome Guibert</a>
 * 
 */
public abstract class AbstractAccessLogMessageObserver extends BaseMessageObserver {

	private final Map<String, Long> timers = new ConcurrentHashMap<String, Long>();

	public AbstractAccessLogMessageObserver() {
	}

	@Override
	public final void onReceived(final Request request, final Response response) {
		timers.put(request.getCorrelationId(), System.currentTimeMillis());
	}

	@Override
	public final void onComplete(Request request, Response response) {
		final Long stop = System.currentTimeMillis();
		final Long start = timers.remove(request.getCorrelationId());
		access(request.getEffectiveHttpMethod().toString(), request.getUrl(), response.getResponseStatus().toString(), start != null ? stop - start : -1);
	}

	protected abstract void access(String method, String url, String status, long duration);
}
