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
package org.restexpress.util;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.jboss.netty.handler.codec.http.HttpHeaders;
import org.junit.Before;
import org.junit.Test;
import org.restexpress.HttpSpecification;
import org.restexpress.Response;
import org.restexpress.domain.CharacterSet;
import org.restexpress.domain.MediaType;
import org.restexpress.http.HttpSpecificationException;
import org.restexpress.http.HttpStatus;

/**
 * @author toddf
 * @since Mar 4, 2011
 */
public class HttpSpecificationTest {
	private static String JSON = MediaType.APPLICATION_JSON.withCharset(CharacterSet.UTF_8.getCharsetName());
	private static String XML = MediaType.APPLICATION_XML.withCharset(CharacterSet.UTF_8.getCharsetName());
	private Response response;

	@Before
	public void setUp() {
		response = new Response();
	}

	@Test
	public void shouldPassOn200() {
		response.setStatusInfo(HttpStatus.OK);
		response.setEntity("Should be allowed.");
		response.setContentType(JSON);
		response.addHeader(HttpHeaders.Names.CONTENT_LENGTH, "15");
		HttpSpecification.enforce(response);
	}

	@Test
	public void shouldPassOn201() {
		response.setStatusInfo(HttpStatus.CREATED);
		response.setEntity("Should be allowed.");
		response.setContentType(JSON);
		response.addHeader(HttpHeaders.Names.CONTENT_LENGTH, "15");
		HttpSpecification.enforce(response);
	}

	@Test
	public void shouldPassOn409() {
		response.setStatusInfo(HttpStatus.CONFLICT);
		response.setEntity("Should be allowed.");
		response.setContentType(JSON);
		response.addHeader(HttpHeaders.Names.CONTENT_LENGTH, "15");
		HttpSpecification.enforce(response);
	}

	@Test
	public void shouldPassOn500() {
		response.setStatusInfo(HttpStatus.INTERNAL_SERVER_ERROR);
		response.setEntity("Should be allowed.");
		response.setContentType(JSON);
		response.addHeader(HttpHeaders.Names.CONTENT_LENGTH, "15");
		HttpSpecification.enforce(response);
	}

	@Test
	public void shouldPassOn100WithoutBody() {
		response.setStatusInfo(HttpStatus.CONTINUE);
		response.setEntity(null);
		HttpSpecification.enforce(response);
	}

	@Test(expected = HttpSpecificationException.class)
	public void shouldThrowExceptionOn100WithBody() {
		response.setStatusInfo(HttpStatus.CONTINUE);
		response.setEntity("Should not be allowed.");
		HttpSpecification.enforce(response);
	}

	@Test(expected = HttpSpecificationException.class)
	public void shouldThrowExceptionOn100WithContentType() {
		response.setStatusInfo(HttpStatus.CONTINUE);
		response.addHeader(HttpHeaders.Names.CONTENT_TYPE, XML);
		HttpSpecification.enforce(response);
	}

	@Test(expected = HttpSpecificationException.class)
	public void shouldThrowExceptionOn100WithContentLength() {
		response.setStatusInfo(HttpStatus.CONTINUE);
		response.addHeader(HttpHeaders.Names.CONTENT_LENGTH, "25");
		HttpSpecification.enforce(response);
	}

	@Test
	public void shouldPassOn204WithoutBody() {
		response.setStatusInfo(HttpStatus.NO_CONTENT);
		response.setEntity(null);
		HttpSpecification.enforce(response);
	}

	@Test(expected = HttpSpecificationException.class)
	public void shouldThrowExceptionOn204WithBody() {
		response.setStatusInfo(HttpStatus.NO_CONTENT);
		response.setEntity("Should not be allowed.");
		HttpSpecification.enforce(response);
	}

	@Test(expected = HttpSpecificationException.class)
	public void shouldThrowExceptionOn204WithContentType() {
		response.setStatusInfo(HttpStatus.NO_CONTENT);
		response.addHeader(HttpHeaders.Names.CONTENT_TYPE, XML);
		HttpSpecification.enforce(response);
	}

	@Test(expected = HttpSpecificationException.class)
	public void shouldThrowExceptionOn204WithContentLength() {
		response.setStatusInfo(HttpStatus.NO_CONTENT);
		response.addHeader(HttpHeaders.Names.CONTENT_LENGTH, "25");
		HttpSpecification.enforce(response);
	}

	@Test
	public void shouldPassOn304WithoutBody() {
		response.setStatusInfo(HttpStatus.NOT_MODIFIED);
		response.setEntity(null);
		HttpSpecification.enforce(response);
	}

	@Test(expected = HttpSpecificationException.class)
	public void shouldThrowExceptionOn304WithBody() {
		response.setStatusInfo(HttpStatus.NOT_MODIFIED);
		response.setEntity("Should not be allowed.");
		HttpSpecification.enforce(response);
	}

	@Test(expected = HttpSpecificationException.class)
	public void shouldThrowExceptionOn304WithContentType() {
		response.setStatusInfo(HttpStatus.NOT_MODIFIED);
		response.addHeader(HttpHeaders.Names.CONTENT_TYPE, XML);
		HttpSpecification.enforce(response);
	}

	@Test(expected = HttpSpecificationException.class)
	public void shouldThrowExceptionOn304WithContentLength() {
		response.setStatusInfo(HttpStatus.NOT_MODIFIED);
		response.addHeader(HttpHeaders.Names.CONTENT_LENGTH, "25");
		HttpSpecification.enforce(response);
	}

	@Test
	public void shouldAllowContentType() {
		response.setStatusInfo(HttpStatus.OK);
		assertTrue(HttpSpecification.isContentTypeAllowed(response));
		response.setStatusInfo(HttpStatus.CREATED);
		assertTrue(HttpSpecification.isContentTypeAllowed(response));
		response.setStatusInfo(HttpStatus.CONFLICT);
		assertTrue(HttpSpecification.isContentTypeAllowed(response));
		response.setStatusInfo(HttpStatus.INTERNAL_SERVER_ERROR);
		assertTrue(HttpSpecification.isContentTypeAllowed(response));
	}

	@Test
	public void shouldNotAllowContentType() {
		response.setStatusInfo(HttpStatus.CONTINUE);
		assertFalse(HttpSpecification.isContentTypeAllowed(response));
		response.setStatusInfo(HttpStatus.NO_CONTENT);
		assertFalse(HttpSpecification.isContentTypeAllowed(response));
		response.setStatusInfo(HttpStatus.NOT_MODIFIED);
		assertFalse(HttpSpecification.isContentTypeAllowed(response));
	}

	@Test
	public void shouldAllowContentLength() {
		response.setStatusInfo(HttpStatus.OK);
		assertTrue(HttpSpecification.isContentLengthAllowed(response));
		response.setStatusInfo(HttpStatus.CREATED);
		assertTrue(HttpSpecification.isContentLengthAllowed(response));
		response.setStatusInfo(HttpStatus.CONFLICT);
		assertTrue(HttpSpecification.isContentLengthAllowed(response));
		response.setStatusInfo(HttpStatus.INTERNAL_SERVER_ERROR);
		assertTrue(HttpSpecification.isContentLengthAllowed(response));
	}

	@Test
	public void shouldNotAllowContentLength() {
		response.setStatusInfo(HttpStatus.CONTINUE);
		assertFalse(HttpSpecification.isContentLengthAllowed(response));
		response.setStatusInfo(HttpStatus.NO_CONTENT);
		assertFalse(HttpSpecification.isContentLengthAllowed(response));
		response.setStatusInfo(HttpStatus.NOT_MODIFIED);
		assertFalse(HttpSpecification.isContentLengthAllowed(response));
	}
}
