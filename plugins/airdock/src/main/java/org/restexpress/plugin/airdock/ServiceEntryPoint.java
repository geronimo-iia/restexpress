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
package org.restexpress.plugin.airdock;

/**
 * {@link ServiceEntryPoint} is an entry point for service that we attach on a
 * restexpress instance.
 * 
 * @author <a href="mailto:jguibert@intelligents-ia.com" >Jerome Guibert</a>
 * 
 */
public interface ServiceEntryPoint {

	/**
	 * @return the name
	 */
	public String name();

	/**
	 * Initialize entry point service: controller instance, resources...
	 * 
	 * @param context
	 */
	public void initialize(Context context);

	/**
	 * Destroy entry point, all controller and resource must be released.
	 */
	public void destroy();
}
