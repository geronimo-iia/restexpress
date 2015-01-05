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
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.ws.rs.ext.ParamConverter;
import javax.ws.rs.ext.ParamConverterProvider;

import org.restexpress.exception.ConfigurationException;
import org.restexpress.route.invoker.param.BigDecimalParamConverter;
import org.restexpress.route.invoker.param.BooleanParamConverter;
import org.restexpress.route.invoker.param.ByteParamConverter;
import org.restexpress.route.invoker.param.CharacterParamConverter;
import org.restexpress.route.invoker.param.DateParamConverter;
import org.restexpress.route.invoker.param.DoubleParamConverter;
import org.restexpress.route.invoker.param.FloatParamConverter;
import org.restexpress.route.invoker.param.FromConstructorStringParamConverter;
import org.restexpress.route.invoker.param.FromValueOfMethodParamConverter;
import org.restexpress.route.invoker.param.IntegerParamConverter;
import org.restexpress.route.invoker.param.LongParamConverter;
import org.restexpress.route.invoker.param.ShortParamConverter;
import org.restexpress.route.invoker.param.StringParamConverter;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

/**
 * {@link RestExpressParamConverterProvider} implement a
 * {@link ParamConverterProvider}.
 *
 * @author <a href="mailto:jguibert@intelligents-ia.com" >Jerome Guibert</a>
 *
 */
public final class RestExpressParamConverterProvider implements ParamConverterProvider {

	private final Map<String, ParamConverter<?>> paramConverters = Maps.newHashMap();
	private final List<ParamConverterProvider> converterProviders = Lists.newArrayList();

	/**
	 * Build a new instance of {@link RestExpressParamConverterProvider}
	 * initialized with all default {@link ParamConverter}.
	 */
	public RestExpressParamConverterProvider() {
		super();
		initialize();
	}

	/**
	 * Register all {@link ParamConverter}.
	 */
	protected void initialize() {
		// register primitive
		paramConverters.put(boolean.class.getName(), new BooleanParamConverter());
		paramConverters.put(byte.class.getName(), new ByteParamConverter());
		paramConverters.put(char.class.getName(), new CharacterParamConverter());
		paramConverters.put(double.class.getName(), new DoubleParamConverter());
		paramConverters.put(float.class.getName(), new FloatParamConverter());
		paramConverters.put(int.class.getName(), new IntegerParamConverter());
		paramConverters.put(long.class.getName(), new LongParamConverter());
		paramConverters.put(short.class.getName(), new ShortParamConverter());

		// Register other.
		paramConverters.put(Date.class.getName(), new DateParamConverter());
		paramConverters.put(String.class.getName(), new StringParamConverter());
		paramConverters.put(BigDecimal.class.getName(), new BigDecimalParamConverter());

	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> ParamConverter<T> getConverter(Class<T> rawType, Type genericType, Annotation[] annotations) {
		ParamConverter<T> paramConverter = null;

		// find from registered ParamConverterProvider
		if (!converterProviders.isEmpty()) {
			Iterator<ParamConverterProvider> iterator = converterProviders.iterator();
			while (iterator.hasNext() && (paramConverter == null)) {
				paramConverter = iterator.next().getConverter(rawType, genericType, annotations);
			}
		}
		
		// find from local ParamConverter
		paramConverter = (ParamConverter<T>) paramConverters.get(rawType.getName());
		
		// find with value Of method or constructor ?
		if (paramConverter == null) {
			try {
				paramConverter = (ParamConverter<T>) new FromValueOfMethodParamConverter(rawType);
			} catch (ConfigurationException e) {
				try {
					paramConverter = (ParamConverter<T>) new FromConstructorStringParamConverter(rawType);
				} catch (ConfigurationException e1) {
					// no luck
				}
			}
			// register for other call
			if (paramConverter != null) {
				register(rawType, paramConverter);
			}
		}
		return paramConverter;
	}

	/**
	 * Register specified {@link ParamConverter}.
	 * 
	 * @param rawType
	 *            {@link Class} to associate with the {@link ParamConverter}
	 * @param paramConverter
	 *            {@link ParamConverter} to use
	 */
	public <T> void register(Class<T> rawType, ParamConverter<T> paramConverter) {
		paramConverters.put(rawType.getName(), paramConverter);
	}

	/**
	 * Register another {@link ParamConverterProvider}.
	 * 
	 * @param converterProvider
	 *            {@link ParamConverterProvider} instance to register
	 */
	public void register(ParamConverterProvider converterProvider) {
		converterProviders.add(converterProvider);
	}
}
