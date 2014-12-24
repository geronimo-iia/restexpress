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

import org.restexpress.pipeline.MessageContext;

/**
 * 
 * @author <a href="mailto:jguibert@intelligents-ia.com" >Jerome Guibert</a>
 */
public enum FieldSet {
	;

	public static interface FieldMap {

		public Object map(MessageContext context);
	}

	public static class ArrayFieldMap implements FieldMap {

		private final FieldMap[] fieldMaps;
		private final int length;

		public ArrayFieldMap(final FieldMap[] fieldMaps) {
			super();
			this.fieldMaps = fieldMaps;
			length = fieldMaps.length;
		}

		public int length() {
			return length;
		}

		@Override
		public Object map(final MessageContext context) {
			final Object[] parameters = new Object[length];
			for (int i = 0; i < length; i++)
				parameters[i] = fieldMaps[i].map(context);
			return parameters;
		}
	}

	public static class RequestFieldMap implements FieldMap {
		@Override
		public Object map(final MessageContext context) {
			return context.getRequest();
		}
	}

	public static class ResponseFieldMap implements FieldMap {
		@Override
		public Object map(final MessageContext context) {
			return context.getResponse();
		}
	}

	public static class HeaderFieldMap implements FieldMap {

		private final String name;
		private final String defaultValue;

		public HeaderFieldMap(final String name, final String defaultValue) {
			this.name = name;
			this.defaultValue = defaultValue;
		}

		@Override
		public Object map(final MessageContext context) {
			String result = context.getRequest().getHeader(name);
			return result != null ? result : defaultValue;
		}
	}

}