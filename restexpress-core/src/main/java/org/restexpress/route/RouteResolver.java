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

import org.jboss.netty.handler.codec.http.HttpMethod;
import org.restexpress.exception.MethodNotAllowedException;
import org.restexpress.exception.NotFoundException;
import org.restexpress.pipeline.MessageContext;

/**
 * {@link RouteResolver} declare method to resolve {@link Route}.
 * 
 * @author <a href="mailto:jguibert@intelligents-ia.com" >Jerome Guibert</a>
 * @author toddf
 * @since May 4, 2010
 */
public interface RouteResolver {
    /**
     * Return a Route by the name and HttpMethod provided in DSL. Returns null if no route found.
     * 
     * @param name route name
     * @param method the HTTP method
     * @return {@link Route} for specified name and {@link HttpMethod} or null if none as found
     */
    public Route getNamedRoute(final String name, final HttpMethod method);

    /**
     * Get the named URL for the given HTTP method
     * 
     * @param method the HTTP method
     * @param resourceName the name of the route
     * @return the URL pattern, or null if the name/method does not exist.
     */
    public String getNamedUrl(final String name, final HttpMethod method);

    /**
     * Finds which action is appropriate for the given request.
     * 
     * @param context {@link MessageContext} instance
     * @return {@link Action}
     */
    public Action resolve(MessageContext context) throws MethodNotAllowedException, NotFoundException;
}
