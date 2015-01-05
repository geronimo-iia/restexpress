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

import static org.junit.Assert.assertNotNull;

import java.util.Date;

import org.junit.Test;
import org.restexpress.route.invoker.RestExpressParamConverterProvider;
/**
 * {@link RestExpressParamConverterProviderTest} implement test case for {@link RestExpressParamConverterProvider}. 
 *
 * @author <a href="mailto:jguibert@intelligents-ia.com" >Jerome Guibert</a>
 *
 */
public class RestExpressParamConverterProviderTest {

	@Test
	public void checkInitialize() {
		RestExpressParamConverterProvider provider = new RestExpressParamConverterProvider();
		assertNotNull(provider.getConverter(boolean.class, null, null));
		assertNotNull(provider.getConverter(byte.class, null, null));
		assertNotNull(provider.getConverter(char.class, null, null));
		assertNotNull(provider.getConverter(double.class, null, null));
		assertNotNull(provider.getConverter(float.class, null, null));
		assertNotNull(provider.getConverter(int.class, null, null));
		assertNotNull(provider.getConverter(long.class, null, null));
		
		assertNotNull(provider.getConverter(short.class, null, null));
		assertNotNull(provider.getConverter(String.class, null, null));
		assertNotNull(provider.getConverter(Date.class, null, null));
	}
}
