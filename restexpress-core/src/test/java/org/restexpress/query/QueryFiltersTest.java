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
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.jboss.netty.handler.codec.http.DefaultHttpRequest;
import org.jboss.netty.handler.codec.http.HttpMethod;
import org.jboss.netty.handler.codec.http.HttpRequest;
import org.jboss.netty.handler.codec.http.HttpVersion;
import org.junit.Test;
import org.restexpress.Request;
import org.restexpress.TestToolKit;
import org.restexpress.http.BadRequestException;

/**
 * @author toddf
 * @since Jul 27, 2012
 */
public class QueryFiltersTest
{
	@Test
	public void shouldParseQueryString()
	{
		HttpRequest httpRequest = new DefaultHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.GET, "http://www.example.com/somethings?filter=name::todd|description::amazing");
		Request request = TestToolKit.newRequest(httpRequest);
		QueryFilter f = QueryFilters.parseFrom(request);
		assertTrue(f.hasFilters());
		FCallback callback = new FCallback();
		f.iterate(callback);
		assertEquals(2, callback.getFilterCount());
		assertEquals("todd", callback.get("name"));
		assertEquals("amazing", callback.get("description"));
	}

	@Test
	public void shouldParseFilterHeader()
	{
		HttpRequest httpRequest = new DefaultHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.GET, "http://www.example.com/somethings");
		httpRequest.headers().add("filter", "name::todd|description::amazing");
		Request request = TestToolKit.newRequest(httpRequest);
		QueryFilter f = QueryFilters.parseFrom(request);
		assertTrue(f.hasFilters());
		FCallback callback = new FCallback();
		f.iterate(callback);
		assertEquals(2, callback.getFilterCount());
		assertEquals("todd", callback.get("name"));
		assertEquals("amazing", callback.get("description"));
	}

	@Test
	public void shouldAllowSupportedFilterProperties()
	{
		HttpRequest httpRequest = new DefaultHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.GET, "http://www.example.com/somethings");
		httpRequest.headers().add("filter", "name::todd|description::amazing");
		Request request = TestToolKit.newRequest(httpRequest);
		QueryFilter f = QueryFilters.parseFrom(request, Arrays.asList(new String[] {"name", "description"}));
		assertTrue(f.hasFilters());
		FCallback callback = new FCallback();
		f.iterate(callback);
		assertEquals(2, callback.getFilterCount());
		assertEquals("todd", callback.get("name"));
		assertEquals("amazing", callback.get("description"));
	}

	@Test(expected=BadRequestException.class)
	public void shouldThrowOnInvalidFilter()
	{
		HttpRequest httpRequest = new DefaultHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.GET, "http://www.example.com/somethings");
		httpRequest.headers().add("filter", "name::todd|description::amazing");
		Request request = TestToolKit.newRequest(httpRequest);
		 QueryFilters.parseFrom(request, Arrays.asList(new String[] {"abc", "def", "ghi"}));
	}

	@Test
	public void shouldAllowSingleFilter()
	{
		HttpRequest httpRequest = new DefaultHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.GET, "http://www.example.com/somethings");
		httpRequest.headers().add("filter", "abc::todd");
		Request request = TestToolKit.newRequest(httpRequest);
		 QueryFilters.parseFrom(request, Arrays.asList(new String[] {"abc", "def", "ghi"}));
	}

	@Test(expected=BadRequestException.class)
	public void shouldThrowOnSingleInvalidFilter()
	{
		HttpRequest httpRequest = new DefaultHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.GET, "http://www.example.com/somethings");
		httpRequest.headers().add("filter", "name::todd");
		Request request = TestToolKit.newRequest(httpRequest);
		 QueryFilters.parseFrom(request, Arrays.asList(new String[] {"abc", "def", "ghi"}));
	}
	
	private class FCallback
	implements FilterCallback
	{
		private Map<String, Object> filters = new HashMap<String, Object>();

        @Override
        public void filterOn(FilterComponent c)
        {
        	filters.put(c.getField(), c.getValue());
        }
        
        public int getFilterCount()
        {
        	return filters.size();
        }
        
        public Object get(String name)
        {
        	return filters.get(name);
        }
	}
}
