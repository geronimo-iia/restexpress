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

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.restexpress.Request;
import org.restexpress.Response;

/**
 * {@link ConsoleAccesLogMessageObserver} add an access log observer.
 * 
 * @author <a href="mailto:jguibert@intelligents-ia.com" >Jerome Guibert</a>
 * 
 */
public class ConsoleAccesLogMessageObserver extends BaseMessageObserver {

	private final Map<String, Long> timers = new ConcurrentHashMap<String, Long>();
	private final OutputStream outputStream;
	private final Charset charset;

	/**
	 * Build a new instance of {@link ConsoleAccesLogMessageObserver}.
	 * 
	 * @param outputStream
	 *            {@link OutputStream} instance which can be obtained by a
	 *            <code>Files.newOutputStream</code> for example.
	 */
	public ConsoleAccesLogMessageObserver(OutputStream outputStream) {
		super();
		this.outputStream = outputStream;
		charset = Charset.forName("UTF-8");
	}

	@Override
	public void onReceived(final Request request, final Response response) {
		timers.put(request.getCorrelationId(), System.currentTimeMillis());
	}

	@Override
	public void onComplete(Request request, Response response) {
		final Long stop = System.currentTimeMillis();
		final Long start = timers.remove(request.getCorrelationId());

		final StringBuffer sb = new StringBuffer(request.getEffectiveHttpMethod().toString());
		sb.append(" ");
		sb.append(request.getUrl());

		if (start != null) {
			sb.append(" responded with ");
			sb.append(response.getResponseStatus().toString());
			sb.append(" in ");
			sb.append(stop - start);
			sb.append(" ms");
		} else {
			sb.append(" responded with ");
			sb.append(response.getResponseStatus().toString());
		}

		byte[] bytes = sb.toString().getBytes(charset);
		try {
			outputStream.write(bytes, 0, bytes.length);
		} catch (IOException e) {
			// we just lost access log
		}

	}

}
