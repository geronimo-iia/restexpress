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
package org.restexpress.query;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.jboss.netty.handler.codec.http.DefaultHttpRequest;
import org.jboss.netty.handler.codec.http.HttpMethod;
import org.jboss.netty.handler.codec.http.HttpRequest;
import org.jboss.netty.handler.codec.http.HttpVersion;
import org.junit.Test;
import org.restexpress.Request;
import org.restexpress.common.query.QueryRange;
import org.restexpress.exception.BadRequestException;
import org.restexpress.query.QueryRanges;

/**
 * @author toddf
 * @since May 24, 2012
 */
public class QueryRangesTest
{
	@Test
	public void shouldParseZeroBasedRange()
	{
		HttpRequest httpRequest = new DefaultHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.GET, "http://www.example.com/somethings");
		httpRequest.headers().add("Range", "items=0-24");
		Request request = new Request(httpRequest, null);
		QueryRange r = QueryRanges.parseFrom(request);
		assertEquals(25, r.getLimit());
		assertEquals(0, r.getOffset());
		assertEquals(24, r.getEnd());
	}

	@Test
	public void shouldHandleNullOffset()
	{
		HttpRequest httpRequest = new DefaultHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.GET, "http://www.example.com/somethings");
		httpRequest.headers().add("limit", "5");
		Request request = new Request(httpRequest, null);
		QueryRange r = QueryRanges.parseFrom(request);
		assertEquals(5, r.getLimit());
		assertEquals(0, r.getOffset());
		assertEquals(4, r.getEnd());
	}

	@Test(expected=BadRequestException.class)
	public void shouldThrowOnNullLimitWithOffset()
	{
		HttpRequest httpRequest = new DefaultHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.GET, "http://www.example.com/somethings");
		httpRequest.headers().add("offset", "25");
		Request request = new Request(httpRequest, null);
		QueryRanges.parseFrom(request);
		fail("Should have thrown");
	}

	@Test
	public void shouldHandleNullLimitWithDefault()
	{
		HttpRequest httpRequest = new DefaultHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.GET, "http://www.example.com/somethings");
		httpRequest.headers().add("offset", "25");
		Request request = new Request(httpRequest, null);
		QueryRange r = QueryRanges.parseFrom(request, 5);
		assertEquals(5, r.getLimit());
		assertEquals(25, r.getOffset());
		assertEquals(29, r.getEnd());
	}

	@Test(expected=BadRequestException.class)
	public void shouldThrowExceptionOnNonNumericRange()
	{
		HttpRequest httpRequest = new DefaultHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.GET, "http://www.example.com/somethings");
		httpRequest.headers().add("Range", "items=A-24");
		Request request = new Request(httpRequest, null);
		QueryRanges.parseFrom(request);
		fail("Did not throw exception as expected.");
	}

	@Test(expected=BadRequestException.class)
	public void shouldThrowExceptionOnReversedRange()
	{
		HttpRequest httpRequest = new DefaultHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.GET, "http://www.example.com/somethings");
		httpRequest.headers().add("Range", "items=24-23");
		Request request = new Request(httpRequest, null);
		QueryRanges.parseFrom(request);
		fail("Did not throw exception as expected.");
	}

	@Test(expected=BadRequestException.class)
	public void shouldThrowExceptionOnReversedRangeAroundZero()
	{
		HttpRequest httpRequest = new DefaultHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.GET, "http://www.example.com/somethings");
		httpRequest.headers().add("Range", "items=1-0");
		Request request = new Request(httpRequest, null);
		QueryRanges.parseFrom(request);
		fail("Did not throw exception as expected.");
	}

	@Test
	public void shouldParseNextPageRange()
	{
		HttpRequest httpRequest = new DefaultHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.GET, "http://www.example.com/somethings");
		httpRequest.headers().add("Range", "items=200-299");
		Request request = new Request(httpRequest, null);
		QueryRange r = QueryRanges.parseFrom(request);
		assertEquals(100, r.getLimit());
		assertEquals(200, r.getStart());
		assertEquals(299, r.getEnd());
	}

	@Test
	public void shouldFavorQueryStringParameters()
	{
		HttpRequest httpRequest = new DefaultHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.GET, "http://www.example.com/somethings");
		httpRequest.headers().add("Range", "items=0-24");
		// RestExpress parses the query-string into headers.
		httpRequest.headers().add("limit", "100");
		httpRequest.headers().add("offset", "200");
		Request request = new Request(httpRequest, null);
		QueryRange r = QueryRanges.parseFrom(request);
		assertEquals(100, r.getLimit());
		assertEquals(200, r.getStart());
		assertEquals(299, r.getEnd());
	}
}
