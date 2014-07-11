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
package org.restexpress.route;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.junit.Test;
import org.restexpress.Format;
import org.restexpress.Request;
import org.restexpress.Response;
import org.restexpress.route.Route;
import org.restexpress.route.regex.RegexRouteBuilder;
import org.restexpress.settings.RouteDefaults;

/**
 * @author toddf
 * @since Jun 11, 2012
 */
public class RegexRouteBuilderTest
{
	@Test
	public void shouldHaveDefaultFormat()
	{
		RouteDefaults defaults = new RouteDefaults();
		defaults.setDefaultFormat(Format.JSON);
		RegexRouteBuilder rb = new RegexRouteBuilder("/foo", new NoopController(), defaults);
		List<Route> routes = rb.build();
		assertNotNull(routes);
		assertEquals(4, routes.size());
		assertEquals(Format.JSON, routes.get(0).getDefaultFormat());
	}
	
	@Test
	public void shouldNotModifyUri()
	{
		String pattern = "^/foo(.*)";
		RegexRouteBuilder rb = new RegexRouteBuilder(pattern, new NoopController(), null);
		List<Route> routes = rb.build();
		assertNotNull(routes);
		assertEquals(4, routes.size());
		assertEquals(pattern, routes.get(0).getPattern());
	}

	@SuppressWarnings("unused")
    private class NoopController
	{
		public void create(Request request, Response response)
		{
		}

		public void read(Request request, Response response)
		{
		}

		public void update(Request request, Response response)
		{
		}

		public void delete(Request request, Response response)
		{
		}
	}
}
