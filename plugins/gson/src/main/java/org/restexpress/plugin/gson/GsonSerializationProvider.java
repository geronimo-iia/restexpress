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
/*
    Copyright 2013, Strategic Gains, Inc.

	Licensed under the Apache License, Version 2.0 (the "License");
	you may not use this file except in compliance with the License.
	You may obtain a copy of the License at

		http://www.apache.org/licenses/LICENSE-2.0

	Unless required by applicable law or agreed to in writing, software
	distributed under the License is distributed on an "AS IS" BASIS,
	WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
	See the License for the specific language governing permissions and
	limitations under the License.
 */
package org.restexpress.plugin.gson;

import org.restexpress.plugin.gson.json.GsonJsonProcessor;
import org.restexpress.response.Wrapper;
import org.restexpress.serialization.DefaultSerializationProvider;
import org.restexpress.serialization.xml.XstreamXmlProcessor;

/**
 * A serialization provider that uses Gson for JSON and Xstream fro XML
 * serialization/deserialization.
 * 
 * @author toddf
 * @since Jul 18, 2013
 */
public class GsonSerializationProvider extends DefaultSerializationProvider {
	public GsonSerializationProvider() {
		super();
		add(new GsonJsonProcessor(), Wrapper.newJsendResponseWrapper(), true);
		add(new XstreamXmlProcessor(), Wrapper.newJsendResponseWrapper());
	}
}
