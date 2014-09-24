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

import org.restexpress.domain.CharacterSet;

/**
 * {@link ConsoleAccesLogMessageObserver} add an access log observer.
 * 
 * @author <a href="mailto:jguibert@intelligents-ia.com" >Jerome Guibert</a>
 * 
 */
public class ConsoleAccesLogMessageObserver extends AbstractAccessLogMessageObserver {

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
		this(outputStream, CharacterSet.UTF_8.getCharset());
	}

	public ConsoleAccesLogMessageObserver(OutputStream outputStream, Charset charset) {
		super();
		this.outputStream = outputStream;
		this.charset = charset;

	}

	@Override
	protected void access(String method, String url, String status, long duration) {
		final StringBuffer sb = new StringBuffer(method);
		sb.append(" ");
		sb.append(url);

		if (duration >= 0) {
			sb.append(" responded with ");
			sb.append(status);
			sb.append(" in ");
			sb.append(duration);
			sb.append(" ms");
		} else {
			sb.append(" responded with ");
			sb.append(status);
		}
		sb.append("\n");
		byte[] bytes = sb.toString().getBytes(charset);
		try {
			outputStream.write(bytes, 0, bytes.length);
		} catch (IOException e) {
			// we just lost access log
		}
	}

}
