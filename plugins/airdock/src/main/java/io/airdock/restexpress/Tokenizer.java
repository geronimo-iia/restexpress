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
package io.airdock.restexpress;

import java.util.Map;

import org.mvel2.templates.CompiledTemplate;
import org.mvel2.templates.TemplateCompiler;
import org.mvel2.templates.TemplateRuntime;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;

/**
 * {@link Tokenizer} is an utility class to generate url using mvel templating.
 * 
 * @author <a href="mailto:jguibert@intelligents-ia.com" >Jerome Guibert</a>
 */
public class Tokenizer {

	private Map<String, CompiledTemplate> templates = Maps.newHashMap();

	/**
	 * 
	 * @param name
	 *            route name
	 * @param template
	 *            associated URI template
	 */
	public void addTemplate(String name, String template) {
		CompiledTemplate compiledTemplate = TemplateCompiler.compileTemplate(template);
		templates.put(name, compiledTemplate);
	}

	public String execute(String name, Map<String, String> vars) {
		return TemplateRuntime.execute(templates.get(name), vars).toString();
	}

	public String execute(String name, String key, String value) {
		Map<String, String> vars = ImmutableMap.<String, String>builder().put(key, value).build();
		return execute(name, vars);
	}
}
