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
package org.restexpress.reader;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.HEAD;
import javax.ws.rs.OPTIONS;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;

import org.jboss.netty.handler.codec.http.HttpMethod;
import org.restexpress.RestExpress;
import org.restexpress.route.RouteBuilder;
import org.restexpress.route.invoker.Invoker;
import org.restexpress.route.invoker.Invokers;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

/**
 * {@link JaxRsReader} read an object and scan it for JaxRs annotation.
 * 
 * @author <a href="mailto:jguibert@intelligents-ia.com" >Jerome Guibert</a>
 */
public enum JaxRsReader {
	;

	/**
	 * Read class of specified controller and declare route.
	 * 
	 * @param restExpress
	 *            {@link RestExpress} configuration service.
	 * @param controller
	 * @return number of route declared
	 */
	public static int register(final RestExpress restExpress, final Object controller) {
		Class<?> cls = controller.getClass();
		Map<String, RouteBuilder> routes = Maps.newHashMap();
		// load controller Path
		String controllerPath = null;
		Path path = cls.getAnnotation(Path.class);
		if (path != null) {
			controllerPath = path.value();
		}
		for (Method method : cls.getMethods()) {
			String actionPath = controllerPath;
			List<HttpMethod> httpMethods = Lists.newArrayList();

			Path methodPath = method.getAnnotation(Path.class);
			if (methodPath != null) {
				actionPath = methodPath.value();
			}

			// find HTTP Method
			if (method.getAnnotation(HEAD.class) != null) {
				httpMethods.add(HttpMethod.HEAD);
			}
			if (method.getAnnotation(GET.class) != null) {
				httpMethods.add(HttpMethod.GET);
			}
			if (method.getAnnotation(POST.class) != null) {
				httpMethods.add(HttpMethod.POST);
			}
			if (method.getAnnotation(PUT.class) != null) {
				httpMethods.add(HttpMethod.PUT);
			}
			if (method.getAnnotation(DELETE.class) != null) {
				httpMethods.add(HttpMethod.DELETE);
			}
			if (method.getAnnotation(OPTIONS.class) != null) {
				httpMethods.add(HttpMethod.OPTIONS);
			}

			if (!Strings.isNullOrEmpty(actionPath) && !httpMethods.isEmpty()) {
				// create builder if necessary
				RouteBuilder builder = null;
				if (!routes.containsKey(actionPath)) {
					String routeName = formatRouteName(cls, actionPath, routes.size());
					builder = restExpress.uri(actionPath, controller).name(routeName);
					routes.put(actionPath, builder);
				} else {
					builder = routes.get(actionPath);
				}
				// create invoker
				Invoker invoker = Invokers.newInvoker(restExpress.paramConverterProvider(), controller, method);
				method.setAccessible(true);
				// add HTTP method
				for (HttpMethod httpMethod : httpMethods) {
					builder.action(invoker, httpMethod);
				}
			}
		}
		return routes.size();
	}

	/**
	 * Format a route name.
	 * 
	 * @param cls
	 * @param actionPath
	 * @return a route name
	 */
	private static String formatRouteName(Class<?> cls, String actionPath, int index) {
		String name = actionPath.replaceAll("/", "\\.").replaceAll("\\{", "").replaceAll("\\}", "");
		if (".".equals(name)) {
			name = ".root";
		}
		name = cls.getSimpleName() + name + "." + index;
		return name.toLowerCase(Locale.US);
	}

}
