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
package org.restexpress.common.exception;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;
import org.restexpress.exception.Exceptions;
import org.restexpress.exception.HttpRuntimeException;

/**
 * @author toddf
 * @since Apr 8, 2011
 */
public class ExceptionsTest {
	@Test
	public void shouldHandleNull() {
		assertNull(Exceptions.findRootCause(null));
	}

	@Test
	public void shouldReturnSame() {
		final Throwable t = new NullPointerException();
		assertEquals(t, Exceptions.findRootCause(t));
	}

	@Test
	public void shouldReturnRoot() {
		final Throwable npe = new NullPointerException("Manually-thrown NullPointerException");
		final Throwable t = new HttpRuntimeException(new Exception(npe));
		assertEquals(npe, Exceptions.findRootCause(t));
	}
}
