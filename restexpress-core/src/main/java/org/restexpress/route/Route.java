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
 Copyright 2010, Strategic Gains, Inc.

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

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jboss.netty.handler.codec.http.HttpMethod;
import org.restexpress.Flags;
import org.restexpress.pipeline.MessageContext;
import org.restexpress.route.invoker.Invoker;
import org.restexpress.url.UrlMatch;
import org.restexpress.url.UrlMatcher;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

/**
 * A {@link Route} is an immutable relationship between a URL pattern and a REST service.
 * 
 * @author toddf
 * @since May 4, 2010
 */
public abstract class Route implements Invoker {

    private final UrlMatcher urlMatcher;
    private final Invoker invoker;
    private final HttpMethod method;
    private boolean shouldSerializeResponse = true;
    private final String name;
    private final Set<String> flags = Sets.newHashSet();
    private final Map<String, Object> parameters = Maps.newHashMap();

    public Route(final UrlMatcher urlMatcher, final Invoker invoker, final HttpMethod method, final boolean shouldSerializeResponse,
            final String name, final Set<String> flags, final Map<String, Object> parameters) {
        super();
        this.urlMatcher = urlMatcher;
        this.invoker = invoker;
        this.method = method;
        this.shouldSerializeResponse = shouldSerializeResponse;
        this.name = name;
        this.flags.addAll(flags);
        this.parameters.putAll(parameters);
    }

    /**
     * @return {@link Invoker} instance.
     */
    @VisibleForTesting
    public final Invoker invoker() {
        return invoker;
    }

    @Override
    public final Object invoke(final MessageContext context) {
        return invoker.invoke(context);
    }

    @Override
    public final Object controller() {
        return invoker.controller();
    }

    @Override
    public final Method action() {
        return invoker.action();
    }

    public boolean isFlagged(final String flag) {
        return flags.contains(flag);
    }

    public final boolean isFlagged(final Flags flag) {
        return isFlagged(flag.toString());
    }

    public final boolean hasParameter(final String name) {
        return (getParameter(name) != null);
    }

    public final Object getParameter(final String name) {
        return parameters.get(name);
    }

    public final HttpMethod getMethod() {
        return method;
    }

    public final String getName() {
        return name;
    }

    public final boolean hasName() {
        return ((getName() != null) && !getName().trim().isEmpty());
    }

    /**
     * Returns the URL pattern without any '.{format}' at the end. In essence, a 'short' URL pattern.
     * 
     * @return a URL pattern
     */
    public final String getPattern() {
        return urlMatcher.getPattern();
    }

    public final boolean shouldSerializeResponse() {
        return shouldSerializeResponse;
    }

    public UrlMatch match(final String url) {
        return urlMatcher.match(url);
    }

    public final List<String> getUrlParameters() {
        return urlMatcher.getParameterNames();
    }

}
