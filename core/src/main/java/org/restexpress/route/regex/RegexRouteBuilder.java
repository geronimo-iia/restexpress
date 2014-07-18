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
package org.restexpress.route.regex;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jboss.netty.handler.codec.http.HttpMethod;
import org.restexpress.Flags;
import org.restexpress.route.Route;
import org.restexpress.route.RouteBuilder;
import org.restexpress.settings.RouteDefaults;

/**
 * @author toddf
 * @since Jan 13, 2011
 */
public class RegexRouteBuilder extends RouteBuilder {
	/**
	 * @param uri
	 * @param controller
	 * @param routeType
	 */
	public RegexRouteBuilder(final String uri, final Object controller, final RouteDefaults defaults) {
		super(uri, controller, defaults);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.strategicgains.restexpress.route.RouteBuilder#newRoute(java.lang.
	 * String, java.lang.Object, java.lang.reflect.Method,
	 * org.jboss.netty.handler.codec.http.HttpMethod, boolean, java.lang.String,
	 * java.util.List, java.lang.String)
	 */
	@Override
	protected Route newRoute(final String pattern, final Object controller, final Method action, final HttpMethod method, final boolean shouldSerializeResponse, final String name, final List<String> supportedFormats, final String defaultFormat,
			final Set<Flags> flags, final Map<String, Object> parameters, final String baseUrl) {
		return new RegexRoute(pattern, controller, action, method, shouldSerializeResponse, name, supportedFormats, defaultFormat, flags, parameters, baseUrl);
	}

	@Override
	protected String toRegexPattern(final String uri) {
		// do not modify the uri, since the caller is building their own regex
		// and is ON THEIR OWN... :-)
		return uri;
	}
}
