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
package org.restexpress.route;

import static org.jboss.netty.handler.codec.http.HttpMethod.DELETE;
import static org.jboss.netty.handler.codec.http.HttpMethod.GET;
import static org.jboss.netty.handler.codec.http.HttpMethod.HEAD;
import static org.jboss.netty.handler.codec.http.HttpMethod.OPTIONS;
import static org.jboss.netty.handler.codec.http.HttpMethod.POST;
import static org.jboss.netty.handler.codec.http.HttpMethod.PUT;
import static org.reflections.ReflectionUtils.getAllMethods;
import static org.reflections.ReflectionUtils.withName;
import static org.reflections.ReflectionUtils.withParameters;
import static org.reflections.ReflectionUtils.withParametersCount;

import java.io.File;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.handler.codec.http.HttpMethod;
import org.restexpress.Request;
import org.restexpress.Response;
import org.restexpress.domain.metadata.RouteMetadata;
import org.restexpress.domain.metadata.UriMetadata;
import org.restexpress.exception.ConfigurationException;
import org.restexpress.route.invoker.Invoker;
import org.restexpress.route.invoker.Invokers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Predicates;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

/**
 * Builds a route for a single URI. If a URI is given with no methods or actions, the builder creates routes for the GET, POST, PUT,
 * and DELETE HTTP methods for the given URI.
 * 
 * Change
 * <ul>
 * <li>When using default mapping only, this builder did not raise an exception if a declared action has no method, but log a warning.</li>
 * <li>Turn off serialization option when a method return a {@link File}</li>
 * </ul>
 * 
 * @author <a href="mailto:jguibert@intelligents-ia.com" >Jerome Guibert</a>
 * @author toddf
 * @since 2010
 */
public abstract class RouteBuilder {

    public static Logger logger = LoggerFactory.getLogger(RouteBuilder.class);

    /**
     * Default action name.
     */
    static final String DELETE_ACTION_NAME = "delete";
    static final String GET_ACTION_NAME = "read";
    static final String POST_ACTION_NAME = "create";
    static final String PUT_ACTION_NAME = "update";
    static final String HEAD_ACTION_NAME = "headers";
    static final String OPTION_ACTION_NAME = "options";
    /**
     * Default supported HTTP methods.
     */
    static final List<HttpMethod> DEFAULT_HTTP_METHODS = Arrays.asList(new HttpMethod[] { GET, POST, PUT, DELETE });
    /**
     * Default action mapping
     */
    static final Map<HttpMethod, String> ACTION_MAPPING = Maps.newHashMap();

    static {
        ACTION_MAPPING.put(DELETE, DELETE_ACTION_NAME);
        ACTION_MAPPING.put(GET, GET_ACTION_NAME);
        ACTION_MAPPING.put(POST, POST_ACTION_NAME);
        ACTION_MAPPING.put(PUT, PUT_ACTION_NAME);
        ACTION_MAPPING.put(HEAD, HEAD_ACTION_NAME);
        ACTION_MAPPING.put(OPTIONS, OPTION_ACTION_NAME);
    }

    private final String uri;
    /**
     * Set of supported HTTP method.
     */
    private List<HttpMethod> methods = Lists.newArrayList();
    /**
     * List of action name by HTTP method.
     */
    private final Map<HttpMethod, String> actionNames = Maps.newHashMap();
    /**
     * List of Invoker method by HTTP method.
     */
    private final Map<HttpMethod, Invoker> invokers = Maps.newHashMap();
    /**
     * Controller object.
     */
    private final Object controller;
    /**
     * Should user serialization flag.
     */
    private boolean shouldSerializeResponse = true;
    /**
     * Route name.
     */
    private String name;
    /**
     * Set of flag.
     */
    private final Set<String> flags = new HashSet<>();
    /**
     * Set of parameter.
     */
    private final Map<String, Object> parameters = new HashMap<String, Object>();
    /**
     * Aliases.
     */
    protected final List<String> aliases = new ArrayList<String>();

    /**
     * Create a RouteBuilder instance for the given URI pattern. URIs that match the pattern will map to methods on the POJO
     * controller.
     * 
     * @param uri a URI pattern
     * @param controller the POJO service controller.
     */
    public RouteBuilder(final String uri, final Object controller) {
        super();
        this.uri = uri;
        this.controller = controller;
    }

    /**
     * Child builder must implements this method.
     * 
     * @param pattern
     * @param invoker
     * @param method
     * @param shouldSerializeResponse
     * @param name
     * @param supportedFormats
     * @param flags
     * @param parameters
     * @param baseUrl
     * @return
     */
    protected abstract Route newRoute(String pattern, Invoker invoker, HttpMethod method, boolean shouldSerializeResponse,
            String name, Set<String> flags, Map<String, Object> parameters);

    /**
     * Child builder must implements this method.
     * 
     * @param uri
     * @return
     */
    protected abstract String toRegexPattern(String uri);

    /**
     * Map a service method name (action) to a particular HTTP method (e.g. GET, POST, PUT, DELETE, HEAD, OPTIONS)
     * 
     * @param action the name of a method within the service POJO.
     * @param method the HTTP method that should invoke the service method.
     * @return the RouteBuilder instance.
     */
    public RouteBuilder action(final String action, final HttpMethod method) {
        if (!actionNames.containsKey(method)) {
            actionNames.put(method, action);
        }
        if (!methods.contains(method))
            methods.add(method);
        return this;
    }

    /**
     * Map a service method (action) to a particular HTTP method (e.g. GET, POST, PUT, DELETE, HEAD, OPTIONS)
     * 
     * @param invoker {@link Invoker} instance to call the method within the service POJO.
     * @param method the HTTP method that should invoke the service method.
     * @return the RouteBuilder instance.
     */
    public RouteBuilder action(final Invoker invoker, final HttpMethod method) {
        if (!invokers.containsKey(method)) {
            invokers.put(method, invoker);
        }
        if (!methods.contains(method))
            methods.add(method);
        return this;
    }

    /**
     * Defines HTTP methods that the route will support (e.g. GET, PUT, POST, DELETE, OPTIONS, HEAD). This utilizes the default HTTP
     * method to service action mapping (e.g. GET maps to read(), PUT to update(), etc.).
     * 
     * @param methods the HTTP methods supported by the route.
     * @return the RouteBuilder instance.
     */
    public RouteBuilder method(final HttpMethod... methods) {
        for (HttpMethod method : methods) {
            if (!this.methods.contains(method)) {
                this.methods.add(method);
            }
        }
        return this;
    }

    /**
     * Turns off serialization of the response--returns the response body as pain text.
     * 
     * @return the RouteBuilder instance.
     */
    public RouteBuilder noSerialization() {
        this.shouldSerializeResponse = false;
        return this;
    }

    /**
     * Turns on response serialization (the default) so the response body will be serialized (e.g. into JSON or XML).
     * 
     * @return the RouteBuilder instance.
     */
    public RouteBuilder performSerialization() {
        this.shouldSerializeResponse = true;
        return this;
    }

    /**
     * Give the route a known name to facilitate retrieving the route by name. This facilitates using the route URI pattern to create
     * Link instances via LinkUtils.asLinks().
     * 
     * The name must be unique for each URI pattern.
     * 
     * @param name the given name of the route for later retrieval.
     * @return the RouteBuilder instance.
     */
    public RouteBuilder name(final String name) {
        this.name = name;
        return this;
    }

    /**
     * Flags are boolean settings that are created at route definition time. These flags can be used to pass booleans to preprocessors,
     * controllers, or postprocessors. An example might be: flag(NO_AUTHORIZATION), which might inform an authorization preprocessor to
     * skip authorization for this route.
     * 
     * @param flagValue the name of the flag.
     * @return this RouteBuilder to facilitate method chaining.
     */
    public RouteBuilder flag(final String flagValue) {
        flags.add(flagValue);
        return this;
    }

    /**
     * Parameters are named settings that are created at route definition time. These parameters can be used to pass data to subsequent
     * preprocessors, controllers, or postprocessors. This is a way to pass data from a route definition down to subsequent
     * controllers, etc. An example might be: setParameter("route", "read_foo") setParameter("permission", "view_private_data"), which
     * might inform an authorization preprocessor of what permission is being requested on a given resource.
     * 
     * @param name the name of the parameter.
     * @param value an object that is the parameter value.
     * @return this RouteBuilder to facilitate method chaining.
     */
    public RouteBuilder parameter(final String name, final Object value) {
        parameters.put(name, value);
        return this;
    }

    public RouteBuilder useStreamingMultipartUpload() {
        // TODO: complete supportMultipart()
        return this;
    }

    public RouteBuilder useStreamingDownload() {
        // TODO: complete useStreamingdownload()
        return this;
    }

    /**
     * Build the Route instances. The last step in the Builder process.
     * 
     * @return a List of {@link Route} instances.
     */
    public List<Route> build() {
        if (methods.isEmpty()) {
            methods.addAll(DEFAULT_HTTP_METHODS);
        }

        final List<Route> routes = new ArrayList<Route>();
        final String pattern = toRegexPattern(uri);

        for (final HttpMethod httpMethod : methods) {
            
            boolean serializeResponse = this.shouldSerializeResponse;
            String actionName = actionNames.get(httpMethod);
            
            Invoker invoker = null;
            // no method name, maybe an invoker ?
            if (actionName == null) {
                invoker = invokers.get(httpMethod);
            }
            // no invoker
            if (invoker == null) {
                Method action = null;
                // lookup in default action
                if (actionName == null) {
                    // on default mapping, if method is not found we did not raise a configuration exception. We just log a warning.
                    actionName = ACTION_MAPPING.get(httpMethod);
                    try {
                        action = determineActionMethod(controller, actionName);
                    } catch (ConfigurationException e) {
                        logger.warn("{} did not support HTTP {} method.", controller.getClass().getSimpleName(), httpMethod);
                    }
                } else {
                    action = determineActionMethod(controller, actionName);
                }

                if (action != null) {
                    // set accessible
                    action.setAccessible(true);
                    // compute serialization
                    serializeResponse = shouldSerializeResponse(action);
                    // find a good invoker
                    invoker = Invokers.newInvoker(controller, action);
                }

            } else {
                // we have an invoker
                serializeResponse = shouldSerializeResponse(invoker.action());
            }
            
            // add route
            if (invoker != null) {
                // create route
                Route route = newRoute(pattern, invoker, httpMethod, serializeResponse, name, flags, parameters);
                // add result
                routes.add(route);
            }
        }

        return routes;
    }

    /**
     * Should Serialize Response for specified action ? This method return false if action return type is
     * <ul>
     * <li>a {@link File}</li>
     * <li>a {@link ChannelBuffer}</li>
     * <li>shouldSerializeResponse flag is false (from call on {@link #noSerialization()})</li>
     * </ul>
     * 
     * @param action {@link Method} to check
     * @return True if we should use serialization
     */
    private boolean shouldSerializeResponse(Method action) {
        if (shouldSerializeResponse) {
            Class<?> returnType = action.getReturnType();
            return !(File.class.isAssignableFrom(returnType) || ChannelBuffer.class.isAssignableFrom(returnType));
        }
        return Boolean.FALSE;
    }

    /**
     * TODO a lot of refactoring here
     * 
     * @return {@link RouteMetadata}
     */
    public RouteMetadata asMetadata() {
        final List<String> methods = new ArrayList<String>();
        final UriMetadata uriMeta = new UriMetadata(uri);
        final List<Route> routes = build();

        for (final Route route : routes) {
            uriMeta.addAllParameters(route.getUrlParameters());
            methods.add(route.getMethod().getName());
        }
        final RouteMetadata metadata = new RouteMetadata(name, uriMeta,//
                aliases, methods, shouldSerializeResponse);

        return metadata;
    }

    /**
     * Attempts to find the actionName on the controller.
     * 
     * Assuming a signature of actionName(Request, Response), and returns the action as a Method to be used later when the route is
     * invoked.
     * 
     * 
     * 
     * @param controller a pojo that implements a method named by the action, with Request and Response as parameters.
     * @param actionName the name of a method on the given controller pojo.
     * @return a Method instance referring to the action on the controller.
     * @throws ConfigurationException if an error occurs.
     */
    private Method determineActionMethod(final Object controller, final String actionName) {
        try {
            @SuppressWarnings("unchecked")
            Set<Method> methods = getAllMethods(controller.getClass(),
                    Predicates.and(withName(actionName), withParameters(Request.class, Response.class), withParametersCount(2)));
            if (methods.size() == 1) {
                return methods.iterator().next();
            }
            throw new ConfigurationException(String.format("Method %s(Request request, Response response) NOT FOUND", actionName));
        } catch (final Exception e) {
            throw new ConfigurationException(e);
        }
    }
}
