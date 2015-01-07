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
package org.restexpress.route.invoker.param;

import java.lang.reflect.Constructor;

import javax.ws.rs.ext.ParamConverter;

import org.restexpress.ConfigurationException;

import com.google.common.base.Throwables;

/**
 * FromConstructorStringParamConverter implements a {@link ParamConverter} for
 * {@link Object} using a {@link Constructor} with a {@link String} argument.
 *
 * @author <a href="mailto:jguibert@intelligents-ia.com" >Jerome Guibert</a>
 *
 */
public class FromConstructorStringParamConverter implements ParamConverter<Object> {
	private final Constructor<?> constructor;

	public FromConstructorStringParamConverter(Class<?> targetType) {
		super();
		try {
			this.constructor = targetType.getDeclaredConstructor(String.class);
		} catch (Throwable e) {
			throw new ConfigurationException("Constructor(String) is not accessible on " + targetType.getSimpleName());
		}
	}

	@Override
	public Object fromString(String value) {
		try {
			return constructor.newInstance(value);
		} catch (Throwable t) {
			throw Throwables.propagate(t);
		}
	}

	@Override
	public String toString(Object value) {
		return value.toString();
	}
}