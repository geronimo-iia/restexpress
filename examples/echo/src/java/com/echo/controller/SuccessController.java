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
package com.echo.controller;

import com.strategicgains.restexpress.Request;
import com.strategicgains.restexpress.Response;

/**
 * @author toddf
 * @since Aug 31, 2010
 */
public class SuccessController
extends AbstractDelayingController
{
	public Object create(Request request, Response response)
	{
		long delayms = delay(request);
		response.setResponseCreated();
		return new DelayResponse("create", delayms);
	}

	public Object read(Request request, Response response)
	{
		long delayms = delay(request);
		return new DelayResponse("read", delayms);
	}

	public Object update(Request request, Response response)
	{
		long delayms = delay(request);
		return new DelayResponse("udpate", delayms);
	}

	public Object delete(Request request, Response response)
	{
		long delayms = delay(request);
		return new DelayResponse("delete", delayms);
	}
}
