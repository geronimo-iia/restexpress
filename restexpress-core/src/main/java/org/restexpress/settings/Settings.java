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
package org.restexpress.settings;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;

import org.restexpress.RestExpress;
import org.yaml.snakeyaml.TypeDescription;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.DumperOptions.FlowStyle;
import org.yaml.snakeyaml.constructor.Constructor;
import org.yaml.snakeyaml.introspector.BeanAccess;
import org.yaml.snakeyaml.introspector.PropertyUtils;
import org.yaml.snakeyaml.nodes.Tag;
import org.yaml.snakeyaml.representer.Representer;

import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

/**
 * {@link Settings} provide:
 * <ul>
 * <li>load/save {@link RestExpressSettings} from different source</li>
 * <li>{@link RestExpress} initialization</li>
 * </ul>
 * 
 * @author <a href="mailto:jguibert@intelligents-ia.com" >Jerome Guibert</a>
 * 
 * use extention .json or .yaml
 */
public enum Settings {
	;

	/**
	 * @return default {@link RestExpressSettings}.
	 */
	public static RestExpressSettings defaultRestExpressSettings() {
		return new RestExpressSettings();
	}
	/**
	 * @param jsonSettingsPath
	 *            json settings source path
	 * @return a new instance of {@link RestExpressSettings}.
	 * @throws JsonParseException
	 * @throws JsonMappingException
	 * @throws IOException
	 * @throws IllegalArgumentException
	 *             if jsonSettingsPath did not exists.
	 */
	public static RestExpressSettings loadFromJson(final Path jsonSettingsPath) throws JsonParseException, JsonMappingException, IOException, IllegalArgumentException {
		if (!jsonSettingsPath.toFile().exists()) {
			throw new IllegalArgumentException("Unable to find settings " + jsonSettingsPath.toAbsolutePath());
		}
		return loadFromJson(new FileInputStream(jsonSettingsPath.toFile()));
	}

	/**
	 * @param jsonSettingsStream
	 *            json settings source
	 * @return a new instance of settingClassName which extends
	 *         {@link RestExpressSettings}
	 * @throws JsonParseException
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	public static RestExpressSettings loadFromJson(final InputStream jsonSettingsStream) throws JsonParseException, JsonMappingException, IOException {
		return getObjectMapper().readValue(jsonSettingsStream, RestExpressSettings.class);
	}

	/**
	 * Save settings in specified file.
	 * 
	 * @param jsonSettingsPath
	 *            json settings source path
	 * @param settings
	 *            settings instance to save
	 * @throws IOException
	 * @throws JsonMappingException
	 * @throws JsonGenerationException
	 */
	public static void save(final Path jsonSettingsPath, final RestExpressSettings settings) throws JsonGenerationException, JsonMappingException, IOException {
		getObjectMapper().writeValue(jsonSettingsPath.toFile(), settings);
	}

	/**
	 * @return an {@link ObjectMapper} instance used only here.
	 */
	private static ObjectMapper getObjectMapper() {
		return new ObjectMapper()//
				.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)//
				.setSerializationInclusion(JsonInclude.Include.NON_NULL)//
				.setVisibility(PropertyAccessor.FIELD, Visibility.ANY)//
				.setVisibility(PropertyAccessor.GETTER, Visibility.NONE)//
				.setVisibility(PropertyAccessor.SETTER, Visibility.NONE)//
				.setVisibility(PropertyAccessor.IS_GETTER, Visibility.NONE)//
				.enable(SerializationFeature.INDENT_OUTPUT);
	}
	
	/**
	 * @return a configured {@link Yaml} instance.
	 */
	static Yaml getYaml() {
	    PropertyUtils propUtils = new PropertyUtils();
        propUtils.setBeanAccess(BeanAccess.FIELD);
        Representer repr = new Representer();
        repr.setPropertyUtils(propUtils);
        repr.addClassTag(RestExpressSettings.class, new Tag("!restexpress"));
        repr.setDefaultFlowStyle(FlowStyle.AUTO);
        Constructor constructor = new Constructor();
        constructor.setPropertyUtils(propUtils);
        constructor.addTypeDescription(new TypeDescription(RestExpressSettings.class, "!restexpress"));
        return new Yaml(constructor, repr);
	}
}
