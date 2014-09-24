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
package com.kickstart.serialization;

import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.strategicgains.restexpress.serialization.json.DefaultJsonProcessor;

public class JsonSerializationProcessor
extends DefaultJsonProcessor
{

	public JsonSerializationProcessor()
	{
		super();
	}

	@Override
	protected void initializeModule(SimpleModule module)
	{
		super.initializeModule(module);
		// module.addDeserializer(ObjectId.class, new
		// JacksonObjectIdDeserializer());
		// module.addSerializer(ObjectId.class, new
		// JacksonObjectIdSerializer());
	}

	@Override
	protected void initializeMapper(ObjectMapper mapper)
	{
		super.initializeMapper(mapper);
		mapper.configure(MapperFeature.REQUIRE_SETTERS_FOR_GETTERS, true);
		mapper.setPropertyNamingStrategy(PropertyNamingStrategy.CAMEL_CASE_TO_LOWER_CASE_WITH_UNDERSCORES);
	}
}
