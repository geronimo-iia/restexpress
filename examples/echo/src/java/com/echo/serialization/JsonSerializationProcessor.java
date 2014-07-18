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
package com.echo.serialization;

import java.util.Date;

import com.google.gson.GsonBuilder;
import com.strategicgains.restexpress.serialization.json.DefaultJsonProcessor;
import com.strategicgains.restexpress.serialization.json.GsonTimestampSerializer;
import com.strategicgains.util.date.DateAdapterConstants;

/**
 * @author toddf
 * @since Oct 10, 2011
 */
public class JsonSerializationProcessor
extends DefaultJsonProcessor
{

	public JsonSerializationProcessor()
    {
	    super(
	    	new GsonBuilder()
			.disableHtmlEscaping()
			.registerTypeAdapter(Date.class, new GsonTimestampSerializer())
//			.registerTypeAdapter(ObjectId.class, new GsonObjectIdSerializer())
			.setDateFormat(DateAdapterConstants.TIMESTAMP_OUTPUT_FORMAT)
			.create()
	    );
    }
}
