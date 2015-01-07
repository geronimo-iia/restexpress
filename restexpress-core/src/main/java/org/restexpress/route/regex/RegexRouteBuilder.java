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
package org.restexpress.route.regex;

import java.util.Map;
import java.util.Set;

import org.jboss.netty.handler.codec.http.HttpMethod;
import org.restexpress.route.Route;
import org.restexpress.route.RouteBuilder;
import org.restexpress.route.invoker.Invoker;

/**
 * @author toddf
 * @since Jan 13, 2011
 */
public final class RegexRouteBuilder extends RouteBuilder {
    /**
     * @param uri
     * @param controller
     * @param routeType
     */
    public RegexRouteBuilder(final String uri, final Object controller) {
        super(uri, controller);
    }

    @Override
    protected Route newRoute(final String pattern, final Invoker invoker, final HttpMethod method,
            final boolean shouldSerializeResponse, final String name, final Set<String> flags, final Map<String, Object> parameters) {
        return new RegexRoute(pattern, invoker, method, shouldSerializeResponse, name, flags, parameters);
    }

    @Override
    protected String toRegexPattern(final String uri) {
        // do not modify the uri, since the caller is building their own regex
        // and is ON THEIR OWN... :-)
        return uri;
    }
}
