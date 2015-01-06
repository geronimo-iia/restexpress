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
/*
 Copyright 2013, Strategic Gains, Inc.

 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at

 http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
 */
package org.restexpress.processor;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.jboss.netty.handler.codec.http.DefaultHttpRequest;
import org.jboss.netty.handler.codec.http.HttpHeaders;
import org.jboss.netty.handler.codec.http.HttpMethod;
import org.jboss.netty.handler.codec.http.HttpVersion;
import org.junit.Test;
import org.restexpress.Response;
import org.restexpress.TestToolKit;
import org.restexpress.exception.UnauthorizedException;
import org.restexpress.http.HttpHeader;
import org.restexpress.pipeline.MessageContext;
import org.restexpress.pipeline.Preprocessor;

/**
 * @author toddf
 * @since Feb 28, 2013
 */
public class HttpBasicAuthenticationPreprocessorTest {
	private Preprocessor p = new HttpBasicAuthenticationPreprocessor("Test Realm");
	private MessageContext context = new MessageContext(TestToolKit.newRequest(new DefaultHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.GET, "/")), new Response());

	@Test(expected = UnauthorizedException.class)
	public void shouldThrowUnauthorizedExceptionOnNullHeader() {
		p.process(context);
	}

	@Test
	public void shouldSetRequestHeadersOnSuccess() {
		context.getRequest().addHeader(HttpHeaders.Names.AUTHORIZATION, "Basic QWxhZGRpbjpvcGVuIHNlc2FtZQ==");
		p.process(context);
		assertEquals("Aladdin", context.getRequest().getHeader(HttpHeader.X_AUTHENTICATED_USER));
		assertEquals("open sesame", context.getRequest().getHeader(HttpHeader.X_AUTHENTICATED_PASSWORD));
	}

	@Test
	public void shouldSetWwwAuthenticateOnUnauthenticatedRequest() {
		try {
			p.process(context);
		} catch (UnauthorizedException e) {
			String header = context.getResponse().getHeader(HttpHeaders.Names.WWW_AUTHENTICATE);
			assertNotNull(header);
			assertEquals("Basic realm=\"Test Realm\"", header);
		}
	}

	@Test(expected = UnauthorizedException.class)
	public void shouldHandleEmptyCredentials() {
		context.getRequest().addHeader(HttpHeaders.Names.AUTHORIZATION, "Basic");
		p.process(context);
	}

	@Test(expected = UnauthorizedException.class)
	public void shouldHandleBadCredentials() {
		context.getRequest().addHeader(HttpHeaders.Names.AUTHORIZATION, "Basic toddf:no-worky");
		p.process(context);
	}

	@Test(expected = UnauthorizedException.class)
	public void shouldHandleInvalidAuthType() {
		context.getRequest().addHeader(HttpHeaders.Names.AUTHORIZATION, "Basicorsomething");
		p.process(context);
	}
}
