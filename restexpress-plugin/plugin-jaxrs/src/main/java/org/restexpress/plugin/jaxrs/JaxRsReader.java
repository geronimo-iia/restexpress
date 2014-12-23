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
package org.restexpress.plugin.jaxrs;

import java.lang.reflect.Method;
import java.util.Locale;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.HEAD;
import javax.ws.rs.OPTIONS;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;

import org.jboss.netty.handler.codec.http.HttpMethod;
import org.restexpress.RestExpress;

import com.google.common.base.Strings;

/**
 * {@link JaxRsReader} read class.
 * 
 * @author <a href="mailto:jguibert@intelligents-ia.com" >Jerome Guibert</a>
 */
public class JaxRsReader {

    /**
     * {@link RestExpress} configuration service.
     */
    private final RestExpress restExpress;

    public JaxRsReader(final RestExpress restExpress) {
        super();
        this.restExpress = restExpress;
    }

    /**
     * Read class of specified controller and declare route.
     * 
     * @param controller
     * @return number of route declared
     */

    public int read(final Object controller) {
        int result = 0;
        Class<?> cls = controller.getClass();
        String controllerPath = null;
        Path path = cls.getAnnotation(Path.class);
        if (path != null) {
            controllerPath = path.value();
        }
        for (Method method : cls.getMethods()) {

            String actionPath = controllerPath;
            HttpMethod httpMethod = null;

            Path methodPath = method.getAnnotation(Path.class);
            if (methodPath != null) {
                actionPath = methodPath.value();
            }

            // find HTTP Method
            if (method.getAnnotation(HEAD.class) != null) {
                httpMethod = HttpMethod.HEAD;
            } else if (method.getAnnotation(GET.class) != null) {
                httpMethod = HttpMethod.GET;
            } else if (method.getAnnotation(POST.class) != null) {
                httpMethod = HttpMethod.POST;
            } else if (method.getAnnotation(PUT.class) != null) {
                httpMethod = HttpMethod.PUT;
            } else if (method.getAnnotation(DELETE.class) != null) {
                httpMethod = HttpMethod.DELETE;
            } else if (method.getAnnotation(OPTIONS.class) != null) {
                httpMethod = HttpMethod.OPTIONS;
            }
            // good for define toute ?
            if (!Strings.isNullOrEmpty(actionPath) && httpMethod != null) {
                String routeName = formatRouteName(cls, actionPath, httpMethod);
                restExpress.uri(actionPath, controller)//
                        .action(method, httpMethod)//
                        .name(routeName);
                result++;
            }
        }
        return result;
    }

    /**
     * Format a route name.
     * 
     * @param cls
     * @param actionPath
     * @param httpMethod
     * @return a route name
     */
    private static String formatRouteName(Class<?> cls, String actionPath, HttpMethod httpMethod) {
        String name = actionPath.replaceAll("/", "\\.").replaceAll("\\{", "").replaceAll("\\}", "");
        if (".".equals(name)) {
            name = ".root";
        }
        name = cls.getSimpleName() + name + "." + httpMethod.getName();
        return name.toLowerCase(Locale.US);
    }

}
