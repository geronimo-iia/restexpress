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

import java.util.Collection;
import java.util.Map.Entry;

import org.restexpress.Request;
import org.restexpress.Response;
import org.restexpress.url.UrlMatch;

/**
 * {@link Action} is a {@link Route} for a specific {@link Request} (ie result of {@link UrlMatch} .
 * 
 * @author toddf
 * @since July 30, 2010
 */
public final class Action {

    private final Route route;
    private final UrlMatch match;

    public Action(final Route route, final UrlMatch match) {
        super();
        this.route = route;
        this.match = match;
    }

    /**
     * @return resolved {@link Route}.
     */
    public Route resolvedRoute() {
        return route;
    }

    /**
     * Retrieves the parameters from the URL match. These are used as Request headers before invocation of the route.
     * 
     * @return a Collection of Map Entry name/value pairs to be used for headers.
     */
    public Collection<Entry<String, String>> parameters() {
        return match.parameterSet();
    }

    /**
     * Returns whether the underlying Route should serialize the response.
     * 
     * @return
     */
    public boolean shouldSerializeResponse() {
        return resolvedRoute().shouldSerializeResponse();
    }

    /**
     * Invokes the underlying Route, returning the result of the call, if any.
     * 
     * @param request
     * @param response
     * @return
     */
    public Object invoke(final Request request, final Response response) {
        return resolvedRoute().invoke(request, response);
    }

}
