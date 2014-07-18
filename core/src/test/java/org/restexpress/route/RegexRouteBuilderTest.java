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
import org.restexpress.route.regex.RegexRouteBuilder;

/**
 * @author toddf
 * @since Jun 11, 2012
 */
public class RegexRouteBuilderTest {

	@Test
	public void shouldNotModifyUri() {
		String pattern = "^/foo(.*)";
		RegexRouteBuilder rb = new RegexRouteBuilder(pattern, new NoopController());
		List<Route> routes = rb.build();
		assertNotNull(routes);
		assertEquals(4, routes.size());
		assertEquals(pattern, routes.get(0).getPattern());
	}

}
