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
package org.restexpress.response;

import org.restexpress.Request;
import org.restexpress.Response;
import org.restexpress.exception.BadRequestException;
import org.restexpress.exception.NotAcceptableException;

/**
 * {@link ResponseProcessorSettingResolver} define methods to resolve:
 * <ul>
 * <li>Which {@link ResponseProcessorSetting} to use for deserialize
 * {@link Request}</li>
 * <li>Which {@link ResponseProcessorSetting} to use for serialize
 * {@link Response}</li>
 * </ul>
 * 
 * @author <a href="mailto:jguibert@intelligents-ia.com" >Jerome Guibert</a>
 */
public interface ResponseProcessorSettingResolver {

	/**
	 * Resolve {@link Request} and find which {@link ResponseProcessorSetting}
	 * to use for deserialize {@link Request}.
	 * 
	 * @param request
	 * @return a {@link ResponseProcessorSetting}
	 * @throws {@link BadRequestException} if format is not supported
	 */
	public ResponseProcessorSetting resolve(Request request) throws BadRequestException;

	/**
	 * Resolve {@link Response} and find which {@link ResponseProcessorSetting}
	 * to use for serialize {@link Response}.
	 * 
	 * @param request
	 *            {@link Request}
	 * @param response
	 *            {@link Response}
	 * @param shouldForce
	 * @return a {@link ResponseProcessorSetting}.
	 * @throws {@link BadRequestException} if format is not supported
	 * @throws {@link NotAcceptableException} if media type is not supported
	 */
	public ResponseProcessorSetting resolve(Request request, Response response, boolean shouldForce) throws BadRequestException, NotAcceptableException;
}
