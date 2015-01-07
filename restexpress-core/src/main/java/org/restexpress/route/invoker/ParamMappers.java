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

import javax.ws.rs.ext.ParamConverter;

import org.restexpress.Request;
import org.restexpress.Response;
import org.restexpress.pipeline.MessageContext;

/**
 * {@link ParamMappers} declare stuff related to parameter mapping and type
 * Conversion.
 * 
 * @author <a href="mailto:jguibert@intelligents-ia.com" >Jerome Guibert</a>
 */
public enum ParamMappers {
	;

	/**
	 * {@link ParamMapper} declare method to map a {@link MessageContext} into
	 * parameters method.
	 *
	 * @author <a href="mailto:jguibert@intelligents-ia.com" >Jerome Guibert</a>
	 *
	 */
	public static interface ParamMapper {
		/**
		 * @param context
		 *            {@link MessageContext} instance
		 * @return result object map
		 */
		public Object map(MessageContext context);
	}

	/**
	 * {@link ArrayParamMapper} map an array of {@link ParamMapper}.
	 *
	 * @author <a href="mailto:jguibert@intelligents-ia.com" >Jerome Guibert</a>
	 *
	 */
	public static class ArrayParamMapper implements ParamMapper {

		private final ParamMapper[] paramMappers;
		private final int length;

		public ArrayParamMapper(final ParamMapper[] paramMappers) {
			super();
			this.paramMappers = paramMappers;
			length = paramMappers.length;
		}

		public ParamMapper get(int i) {
			return paramMappers[i];
		}

		public int length() {
			return length;
		}

		@Override
		public Object map(final MessageContext context) {
			final Object[] parameters = new Object[length];
			for (int i = 0; i < length; i++)
				parameters[i] = paramMappers[i].map(context);
			return parameters;
		}
	}

	/**
	 * {@link RequestParamMapper} map {@link Request}.
	 *
	 * @author <a href="mailto:jguibert@intelligents-ia.com" >Jerome Guibert</a>
	 *
	 */
	public static class RequestParamMapper implements ParamMapper {
		@Override
		public Object map(final MessageContext context) {
			return context.getRequest();
		}
	}

	/**
	 * {@link ResponseParamMapper} map {@link Response}.
	 *
	 * @author <a href="mailto:jguibert@intelligents-ia.com" >Jerome Guibert</a>
	 *
	 */
	public static class ResponseParamMapper implements ParamMapper {
		@Override
		public Object map(final MessageContext context) {
			return context.getResponse();
		}
	}

	/**
	 * {@link HeaderParamMapper} map a query string or path parameter into a
	 * specific type.
	 *
	 * @author <a href="mailto:jguibert@intelligents-ia.com" >Jerome Guibert</a>
	 *
	 */
	public static class HeaderParamMapper implements ParamMapper {

		private final String name;
		private final Object defaultValue;
		private final ParamConverter<?> paramConverter;

		public HeaderParamMapper(final String name, final ParamConverter<?> paramConverter, final Object defaultValue) {
			this.name = name;
			this.paramConverter = paramConverter;
			this.defaultValue = defaultValue;
		}

		@Override
		public Object map(final MessageContext context) {
			String source = context.getRequest().getHeader(name);
			Object result = null;
			if (source != null)
				result = paramConverter.fromString(source);
			return result != null ? result : defaultValue;
		}

		public String name() {
			return name;
		}

		public Object defaultValue() {
			return defaultValue;
		}

		public ParamConverter<?> paramConverter() {
			return paramConverter;
		}
	}

}