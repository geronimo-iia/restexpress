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
package org.restexpress.route.parameterized;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jboss.netty.handler.codec.http.HttpMethod;
import org.restexpress.route.Route;
import org.restexpress.route.invoker.Invoker;
import org.restexpress.url.UrlMatch;
import org.restexpress.url.UrlPattern;

/**
 * 
 * @author <a href="mailto:jguibert@intelligents-ia.com" >Jerome Guibert</a>
 * @author toddf
 * @since Jan 7, 2011
 */
public final class ParameterizedRoute extends Route {
    private UrlPattern[] aliases;

    public ParameterizedRoute(final UrlPattern urlMatcher, final Invoker invoker, final HttpMethod method,
            final boolean shouldSerializeResponse, final String name, final Set<String> flags, final Map<String, Object> parameters) {
        super(urlMatcher, invoker, method, shouldSerializeResponse, name, flags, parameters);
    }

    public ParameterizedRoute(final String urlPattern, final Invoker invoker, final HttpMethod method,
            final boolean shouldSerializeResponse, final String name, final Set<String> flags, final Map<String, Object> parameters) {
        this(new UrlPattern(urlPattern), invoker, method, shouldSerializeResponse, name, flags, parameters);
    }

    public void addAliases(final List<String> uris) {
        if (uris == null) {
            return;
        }

        aliases = new UrlPattern[uris.size()];
        int i = 0;

        for (final String uri : uris) {
            aliases[i++] = new UrlPattern(uri);
        }
    }

    @Override
    public UrlMatch match(final String url) {
        UrlMatch match = super.match(url);
        if ((match == null) && (aliases != null)) {
            for (final UrlPattern alias : aliases) {
                match = alias.match(url);
                if (match != null) {
                    break;
                }
            }
        }
        return match;
    }
}
