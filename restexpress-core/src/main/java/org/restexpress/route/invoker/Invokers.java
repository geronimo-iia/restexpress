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

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.List;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.ext.ParamConverter;
import javax.ws.rs.ext.ParamConverterProvider;

import org.restexpress.ConfigurationException;
import org.restexpress.Request;
import org.restexpress.Response;
import org.restexpress.http.HttpRuntimeException;
import org.restexpress.pipeline.MessageContext;
import org.restexpress.route.invoker.ParamMappers.ArrayParamMapper;
import org.restexpress.route.invoker.ParamMappers.HeaderParamMapper;
import org.restexpress.route.invoker.ParamMappers.ParamMapper;
import org.restexpress.route.invoker.ParamMappers.RequestParamMapper;
import org.restexpress.route.invoker.ParamMappers.ResponseParamMapper;

import com.google.common.collect.Lists;

/**
 * {@link Invokers} build {@link Invoker} according {@link Method} profile.
 * 
 * @author <a href="mailto:jguibert@intelligents-ia.com" >Jerome Guibert</a>
 */
public enum Invokers {
	;

	/**
	 * Build an Field Map Invoker.
	 * 
	 * @param controller
	 *            controller object
	 * @param action
	 *            {@link Method} to call
	 * @return {@link Invoker} instance for specified method.
	 * @throws ConfigurationException
	 *             if method parameter can not be mapped.
	 */
	public static Invoker newInvoker(ParamConverterProvider paramConverterProvider, Object controller, Method action) throws ConfigurationException {
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
		// more complex case
		List<ParamMapper> paramMappers = Lists.newArrayList();
		Type[] genericParameterTypes = action.getGenericParameterTypes();
		Annotation[][] annotations = action.getParameterAnnotations();
		for (int i = 0; i < parameterTypes.length; i++) {
			if (Request.class.isAssignableFrom(parameterTypes[i])) {
				paramMappers.add(new RequestParamMapper());
			} else if (Response.class.isAssignableFrom(parameterTypes[i])) {
				paramMappers.add(new ResponseParamMapper());
			} else {
				// extract name and default value
				String name = null;
				String defaultValueParameter = null;
				for (int j = 0; j < annotations[i].length; j++) {
					if (PathParam.class.isAssignableFrom(annotations[i][j].getClass())) {
						name = ((PathParam) annotations[i][j]).value();
					}
					if (QueryParam.class.isAssignableFrom(annotations[i][j].getClass())) {
						name = ((QueryParam) annotations[i][j]).value();
					}
					if (DefaultValue.class.isAssignableFrom(annotations[i][j].getClass())) {
						defaultValueParameter = ((DefaultValue) annotations[i][j]).value();
					}
				}
				if (name == null) {
					throw new ConfigurationException("No annotation found for parameter " + i + " of method " + action.getName());
				}
				final ParamConverter<?> paramConverter = paramConverterProvider.getConverter(parameterTypes[i], genericParameterTypes[i], annotations[i]);
				if (paramConverter == null) {
					throw new ConfigurationException("parameter " + name + " of type " + parameterTypes[i].getSimpleName() + " is not supported");
				}
				// compute default value
				Object defaultValue = null;
				if (defaultValueParameter != null) {
					defaultValue = paramConverter.fromString(defaultValueParameter);
				} else if (parameterTypes[i].isPrimitive()) {
					defaultValue = paramConverter.fromString(null);
				}
				paramMappers.add(new HeaderParamMapper(name, paramConverter, defaultValue));
			}
		}
		return Invokers.newInvoker(controller, action, new ArrayParamMapper(paramMappers.toArray(new ParamMapper[paramMappers.size()])));
	}

	/**
	 * 
	 * If arrayParamMapper as no field, this return an {@link NoArgInvoker}
	 * instance.
	 * 
	 * @param controller
	 *            controller object
	 * @param action
	 *            {@link Method} to call
	 * @param arrayParamMapper
	 *            {@link ArrayParamMapper} instance
	 * @return {@link Invoker} instance for specified method.
	 */
	public static Invoker newInvoker(Object controller, Method action, ArrayParamMapper arrayParamMapper) {
		return arrayParamMapper.length() == 0 ? new NoArgInvoker(controller, action) : new FieldMapInvoker(controller, action, arrayParamMapper);
	}

	/**
	 * {@link AbstractInvoker} implement basic method of {@link Invoker}.
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
	 * {@link FieldMapInvoker} implements an {@link Invoker} using an
	 * {@link ArrayParamMapper}.
	 *
	 * @author <a href="mailto:jguibert@intelligents-ia.com" >Jerome Guibert</a>
	 *
	 */
	public static final class FieldMapInvoker extends AbstractInvoker {

		private final ArrayParamMapper arrayParamMapper;

		/**
		 * Build a new instance.
		 * 
		 * @param controller
		 *            controller instance
		 * @param action
		 *            which method
		 * @param arrayParamMapper
		 *            {@link ArrayParamMapper} to use
		 */
		public FieldMapInvoker(Object controller, Method action, ArrayParamMapper arrayParamMapper) {
			super(controller, action);
			this.arrayParamMapper = arrayParamMapper;
		}

		@Override
		public Object invoke(MessageContext context) {
			try {
				return action.invoke(controller, (Object[]) arrayParamMapper.map(context));
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
	 * {@link StandardInvoker} implements an {@link Invoker} for all method with
	 * standard RestExpress profile (with {@link Request} and {@link Response}
	 * parameters).
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
	 * {@link NoArgInvoker} implements an {@link Invoker} for all method with no
	 * parameter.
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
