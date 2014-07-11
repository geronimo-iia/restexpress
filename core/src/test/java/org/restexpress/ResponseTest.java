/*
    Copyright 2012, Strategic Gains, Inc.

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
package org.restexpress;

import static org.junit.Assert.assertEquals;

import org.jboss.netty.handler.codec.http.HttpHeaders;
import org.junit.Test;
import org.restexpress.Response;
import org.restexpress.common.query.QueryRange;

/**
 * @author toddf
 * @since Oct 18, 2012
 */
public class ResponseTest
{
	@Test
	public void shouldSetOK()
	{
		Response r = new Response();
		r.setCollectionResponse(new QueryRange(0l, 5), 3, 3);
		assertEquals(200, r.getResponseStatus().getCode());
		assertEquals("items 0-2/3", r.getHeader(HttpHeaders.Names.CONTENT_RANGE));

		r = new Response();
		r.setCollectionResponse(new QueryRange(0l, 3), 3, 3);
		assertEquals(200, r.getResponseStatus().getCode());
		assertEquals("items 0-2/3", r.getHeader(HttpHeaders.Names.CONTENT_RANGE));

		r = new Response();
		r.setCollectionResponse(new QueryRange(0l, 10), 0, 0);
		assertEquals(200, r.getResponseStatus().getCode());
		assertEquals("items 0-0/0", r.getHeader(HttpHeaders.Names.CONTENT_RANGE));
	}

	@Test
	public void shouldSetPartialContent()
	{
		Response r = new Response();
		r.setCollectionResponse(new QueryRange(1l, 1), 1, 3);
		assertEquals(206, r.getResponseStatus().getCode());
		assertEquals("items 1-1/3", r.getHeader(HttpHeaders.Names.CONTENT_RANGE));

		r = new Response();
		r.setCollectionResponse(new QueryRange(0l, 1), 1, 3);
		assertEquals(206, r.getResponseStatus().getCode());
		assertEquals("items 0-0/3", r.getHeader(HttpHeaders.Names.CONTENT_RANGE));

		r = new Response();
		r.setCollectionResponse(new QueryRange(1l, 3), 2, 3);
		assertEquals(206, r.getResponseStatus().getCode());
		assertEquals("items 1-2/3", r.getHeader(HttpHeaders.Names.CONTENT_RANGE));

		r = new Response();
		r.setCollectionResponse(new QueryRange(2l, 3), 2, 3);
		assertEquals(206, r.getResponseStatus().getCode());
		assertEquals("items 2-2/3", r.getHeader(HttpHeaders.Names.CONTENT_RANGE));

		r = new Response();
		r.setCollectionResponse(new QueryRange(1l, 5), 2, 3);
		assertEquals(206, r.getResponseStatus().getCode());
		assertEquals("items 1-2/3", r.getHeader(HttpHeaders.Names.CONTENT_RANGE));

		r = new Response();
		r.setCollectionResponse(new QueryRange(2l, 75), 2, 3);
		assertEquals(206, r.getResponseStatus().getCode());
		assertEquals("items 2-2/3", r.getHeader(HttpHeaders.Names.CONTENT_RANGE));
	}

	@Test
	public void shouldSetNotSatisfiable()
	{
		Response r = new Response();
		r.setCollectionResponse(new QueryRange(0l, 5), 0, 3);
		assertEquals(416, r.getResponseStatus().getCode());
		assertEquals("items 0-2/3", r.getHeader(HttpHeaders.Names.CONTENT_RANGE));

		r = new Response();
		r.setCollectionResponse(new QueryRange(3l, 5), 0, 3);
		assertEquals(416, r.getResponseStatus().getCode());
		assertEquals("items 0-2/3", r.getHeader(HttpHeaders.Names.CONTENT_RANGE));

		r = new Response();
		r.setCollectionResponse(new QueryRange(75l, 100), 0, 3);
		assertEquals(416, r.getResponseStatus().getCode());
		assertEquals("items 0-2/3", r.getHeader(HttpHeaders.Names.CONTENT_RANGE));
	}
}
