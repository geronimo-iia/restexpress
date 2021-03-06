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
package org.restexpress.plugin;

import org.restexpress.RestExpress;
import org.restexpress.processor.CacheHeaderPostprocessor;
import org.restexpress.processor.DateHeaderPostprocessor;
import org.restexpress.processor.EtagHeaderPostprocessor;
import org.restexpress.processor.LastModifiedHeaderPostprocessor;

/**
 * {@link CacheControlPlugin} adds caching-related headers to GET and HEAD
 * responses.
 * 
 * @author <a href="mailto:jguibert@intelligents-ia.com" >Jerome Guibert</a>
 * @author toddf
 * @since Dec 23, 2011
 */
public class CacheControlPlugin extends AbstractPlugin {

	/**
	 * Build a new instance.
	 */
	public CacheControlPlugin() {
		super();
	}

	/**
	 * Build a new instance.
	 * 
	 * @param priority
	 */
	public CacheControlPlugin(int priority) {
		super(priority);
	}

	@Override
	public void initialize(RestExpress server) {
		server.addPostprocessor(new DateHeaderPostprocessor())//
				.addPostprocessor(new CacheHeaderPostprocessor())//
				.addPostprocessor(new EtagHeaderPostprocessor())//
				.addPostprocessor(new LastModifiedHeaderPostprocessor());
	}

}
