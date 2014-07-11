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
package org.restexpress.preprocessor;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.jboss.netty.handler.codec.http.DefaultHttpRequest;
import org.jboss.netty.handler.codec.http.HttpHeaders;
import org.jboss.netty.handler.codec.http.HttpMethod;
import org.jboss.netty.handler.codec.http.HttpVersion;
import org.junit.Test;
import org.restexpress.Request;
import org.restexpress.Response;
import org.restexpress.exception.UnauthorizedException;
import org.restexpress.pipeline.Preprocessor;
import org.restexpress.preprocessor.HttpBasicAuthenticationPreprocessor;

/**
 * @author toddf
 * @since Feb 28, 2013
 */
public class HttpBasicAuthenticationPreprocessorTest
{
	private Preprocessor p = new HttpBasicAuthenticationPreprocessor("Test Realm");
	Request r = new Request(new DefaultHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.GET, "/"), null);

	@Test(expected=UnauthorizedException.class)
	public void shouldThrowUnauthorizedExceptionOnNullHeader()
	{
		p.process(r);
	}

	@Test
	public void shouldSetRequestHeadersOnSuccess()
	{
		r.addHeader(HttpHeaders.Names.AUTHORIZATION, "Basic QWxhZGRpbjpvcGVuIHNlc2FtZQ==");
		p.process(r);
		assertEquals("Aladdin", r.getHeader(HttpBasicAuthenticationPreprocessor.X_AUTHENTICATED_USER));
		assertEquals("open sesame", r.getHeader(HttpBasicAuthenticationPreprocessor.X_AUTHENTICATED_PASSWORD));
	}

	@Test
	public void shouldSetWwwAuthenticateOnUnauthenticatedRequest()
	{
		try
		{
			p.process(r);
		}
		catch(UnauthorizedException e)
		{
			String value = e.getHeader(HttpHeaders.Names.WWW_AUTHENTICATE);
			assertNotNull(value);
			assertEquals("Basic realm=\"Test Realm\"", value);
			
			Response res = new Response();
			e.augmentResponse(res);
			String header = res.getHeader(HttpHeaders.Names.WWW_AUTHENTICATE);
			assertNotNull(header);
			assertEquals("Basic realm=\"Test Realm\"", header);
		}
	}

	@Test(expected=UnauthorizedException.class)
	public void shouldHandleEmptyCredentials()
	{
		r.addHeader(HttpHeaders.Names.AUTHORIZATION, "Basic");
		p.process(r);
	}

	@Test(expected=UnauthorizedException.class)
	public void shouldHandleBadCredentials()
	{
		r.addHeader(HttpHeaders.Names.AUTHORIZATION, "Basic toddf:no-worky");
		p.process(r);
	}

	@Test(expected=UnauthorizedException.class)
	public void shouldHandleInvalidAuthType()
	{
		r.addHeader(HttpHeaders.Names.AUTHORIZATION, "Basicorsomething");
		p.process(r);
	}
}
