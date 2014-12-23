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
package org.restexpress.route.invoker;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.intelligentsia.commons.http.exception.HttpRuntimeException;
import org.restexpress.Request;
import org.restexpress.Response;
import org.restexpress.pipeline.MessageContext;

/**
 * {@link Invokers} build {@link Invoker} according {@link Method} profile.
 * 
 * @author <a href="mailto:jguibert@intelligents-ia.com" >Jerome Guibert</a>
 */
public enum Invokers {
    ;

    /**
     * @param controller contrller object
     * @param action {@link Method} to call
     * @return {@link Invoker} instance for specified method.
     * @throws IllegalStateException if method is not supported
     */
    public static Invoker newInvoker(Object controller, Method action) throws IllegalStateException {

        Class<?>[] parameterTypes = action.getParameterTypes();
        // no argument case
        if (parameterTypes.length == 0) {
            return new NoArgInvoker(controller, action);
        }
        // standard case
        if (parameterTypes.length == 2) {
            if (parameterTypes[0].isAssignableFrom(Request.class) && parameterTypes[1].isAssignableFrom(Response.class)) {
                return new StandardInvoker(controller, action);
            }
        }
        // here we use a field set
        // TODO implements
        throw new IllegalStateException("Not yet supported");
    }

    /**
     * 
     * @author <a href="mailto:jguibert@intelligents-ia.com" >Jerome Guibert</a>
     * 
     */
    public static abstract class AbstractInvoker implements Invoker {

        protected final Object controller;
        protected final Method action;

        /**
         * Build a new instance.
         * 
         * @param controller
         * @param action
         */
        public AbstractInvoker(Object controller, Method action) {
            super();
            this.controller = controller;
            this.action = action;
        }

        public final Object controller() {
            return controller;
        }

        public final Method action() {
            return action;
        }
    }

    /**
     * {@link StandardInvoker} implements an {@link Invoker} for all method with standard RestExpress profile (with {@link Request} and
     * {@link Response} parameters).
     * 
     * @author <a href="mailto:jguibert@intelligents-ia.com" >Jerome Guibert</a>
     * 
     */
    public static final class StandardInvoker extends AbstractInvoker {

        /**
         * Build a new instance.
         * 
         * @param controller
         * @param action
         */
        public StandardInvoker(Object controller, Method action) {
            super(controller, action);
        }

        @Override
        public Object invoke(MessageContext context) {
            try {
                return action.invoke(controller, context.getRequest(), context.getResponse());
            } catch (final InvocationTargetException e) {
                final Throwable cause = e.getCause();
                if (RuntimeException.class.isAssignableFrom(cause.getClass())) {
                    throw (RuntimeException) e.getCause();
                } else {
                    throw new RuntimeException(cause);
                }
            } catch (final Exception e) {
                throw new HttpRuntimeException(e);
            }
        }

    }

    /**
     * {@link NoArgInvoker} implements an {@link Invoker} for all method with no parameter.
     * 
     * @author <a href="mailto:jguibert@intelligents-ia.com" >Jerome Guibert</a>
     * 
     */
    public static final class NoArgInvoker extends AbstractInvoker {

        /**
         * Build a new instance.
         * 
         * @param controller
         * @param action
         */
        public NoArgInvoker(Object controller, Method action) {
            super(controller, action);
        }

        @Override
        public Object invoke(MessageContext context) {
            try {
                return action.invoke(controller);
            } catch (final InvocationTargetException e) {
                final Throwable cause = e.getCause();
                if (RuntimeException.class.isAssignableFrom(cause.getClass())) {
                    throw (RuntimeException) e.getCause();
                } else {
                    throw new RuntimeException(cause);
                }
            } catch (final Exception e) {
                throw new HttpRuntimeException(e);
            }
        }

    }
}
