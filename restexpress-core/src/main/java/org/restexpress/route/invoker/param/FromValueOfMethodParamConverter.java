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

import java.lang.reflect.Method;

import javax.ws.rs.ext.ParamConverter;

import org.restexpress.exception.ConfigurationException;

import com.google.common.base.Throwables;

/**
 * {@link FromValueOfMethodParamConverter} implements a {@link ParamConverter}
 * for {@link Object} using a {@link Method} named "valueOf" with a
 * {@link String} parameter.
 *
 * @author <a href="mailto:jguibert@intelligents-ia.com" >Jerome Guibert</a>
 *
 */
public class FromValueOfMethodParamConverter implements ParamConverter<Object> {

	private final Class<?> targetType;
	private final Method valueOf;

	public FromValueOfMethodParamConverter(Class<?> targetType) {
		super();
		this.targetType = targetType;
		try {
			this.valueOf = targetType.getDeclaredMethod("valueOf", String.class);
		} catch (Throwable e) {
			throw new ConfigurationException("Method 'valueOf' is not accessible on " + targetType.getSimpleName());
		}
	}

	@Override
	public Object fromString(String value) {
		try {
			return targetType.cast(valueOf.invoke(null, value));
		} catch (Throwable t) {
			throw Throwables.propagate(t);
		}
	}

	@Override
	public String toString(Object value) {
		return value.toString();
	}
}