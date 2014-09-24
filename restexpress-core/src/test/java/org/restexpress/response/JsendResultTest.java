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
    Copyright 2011, Strategic Gains, Inc.

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
package org.restexpress.response;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.IOException;

import org.intelligentsia.commons.http.exception.HttpRuntimeException;
import org.junit.Test;
import org.restexpress.Response;
import org.restexpress.domain.response.JsendResult;
import org.restexpress.domain.response.JsendResult.State;


/**
 * @author toddf
 * @since May 11, 2011
 */
public class JsendResultTest
{
	private Response response = new Response();

	@Test
	public void shouldHandleCheckedException()
	{
		response.setException(new IOException("An IOException was thrown"));
		response.setResponseCode(1);
		JsendResult  w = (JsendResult) Wrapper.newJsendResponseWrapper().wrap(response);
		assertNotNull(w);
		assertEquals(State.FAIL.toString(), w.getStatus());
		assertEquals("An IOException was thrown", w.getMessage());
		assertEquals(IOException.class.getSimpleName(), w.getData());
	}

	@Test
	public void shouldHandleUncheckedException()
	{
		response.setException(new ArrayIndexOutOfBoundsException("An ArrayIndexOutOfBoundsException was thrown"));
		response.setResponseCode(2);
		JsendResult  w = (JsendResult) Wrapper.newJsendResponseWrapper().wrap(response);
		assertNotNull(w);
		assertEquals(State.FAIL.toString(), w.getStatus());
		assertEquals("An ArrayIndexOutOfBoundsException was thrown", w.getMessage());
		assertEquals(ArrayIndexOutOfBoundsException.class.getSimpleName(), w.getData());
	}

	@Test
	public void shouldHandleHttpRuntimeException()
	{
		response.setException(new HttpRuntimeException("A ServiceException was thrown"));
		response.setResponseCode(3);
		JsendResult  w = (JsendResult) Wrapper.newJsendResponseWrapper().wrap(response);
		assertNotNull(w);
		assertEquals(State.ERROR.toString(), w.getStatus());
		assertEquals("A ServiceException was thrown", w.getMessage());
		assertEquals(HttpRuntimeException.class.getSimpleName(), w.getData());
	}

	@Test
	public void shouldHandleRaw100Code()
	{
		response.setResponseCode(100);
		response.setBody("Success Body");
		JsendResult  w = (JsendResult) Wrapper.newJsendResponseWrapper().wrap(response);
		assertNotNull(w);
		assertEquals(State.SUCCESS.toString(), w.getStatus());
		assertNull(w.getMessage());
		assertEquals("Success Body", w.getData());
	}

	@Test
	public void shouldHandleRaw400Code()
	{
		response.setResponseCode(400);
		response.setBody("Error Body");
		JsendResult  w = (JsendResult) Wrapper.newJsendResponseWrapper().wrap(response);
		assertNotNull(w);
		assertEquals(State.ERROR.toString(), w.getStatus());
		assertNull(w.getMessage());
		assertEquals("Error Body", w.getData());
	}

	@Test
	public void shouldHandleRaw500Code()
	{
		response.setResponseCode(500);
		response.setBody("Fail Body");
		JsendResult  w = (JsendResult) Wrapper.newJsendResponseWrapper().wrap(response);
		assertNotNull(w);
		assertEquals(State.FAIL.toString(), w.getStatus());
		assertNull(w.getMessage());
		assertEquals("Fail Body", w.getData());
	}

	@Test
	public void shouldHandleSuccess()
	{
		response.setBody("Success Body");
		JsendResult  w = (JsendResult) Wrapper.newJsendResponseWrapper().wrap(response);
		assertNotNull(w);
		assertEquals(State.SUCCESS.toString(), w.getStatus());
		assertNull(w.getMessage());
		assertEquals("Success Body", w.getData());
	}

}
 