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
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;
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
    JSON, YAML;
    ;

    /**
     * @return default {@link RestExpressSettings}.
     */
    public static RestExpressSettings defaultRestExpressSettings() {
        return new RestExpressSettings();
    }

    /**
     * Load from file. Determine format by extention (.json or .yaml)
     * 
     * @param settingsPath settings source path
     * @return a new instance of {@link RestExpressSettings}.
     * @throws JsonParseException
     * @throws JsonMappingException
     * @throws IOException
     * @throws IllegalArgumentException if jsonSettingsPath did not exists.
     */
    public static RestExpressSettings loadFrom(final Path settingsPath) throws JsonParseException, JsonMappingException, IOException,
            IllegalArgumentException {
        if (settingsPath.getFileName().endsWith("yaml")) {
            return loadFrom(settingsPath, Settings.YAML);
        }
        return loadFrom(settingsPath, Settings.JSON);
    }

    /**
     * @param settingsPath settings source path
     * @param settings format
     * @return a new instance of {@link RestExpressSettings}.
     * @throws JsonParseException
     * @throws JsonMappingException
     * @throws IOException
     * @throws IllegalArgumentException if jsonSettingsPath did not exists.
     */
    public static RestExpressSettings loadFrom(final Path settingsPath, final Settings settings) throws JsonParseException,
            JsonMappingException, IOException, IllegalArgumentException {
        if (!settingsPath.toFile().exists()) {
            throw new IllegalArgumentException("Unable to find settings " + settingsPath.toAbsolutePath());
        }
        return loadFrom(new FileInputStream(settingsPath.toFile()), settings);
    }

    /**
     * @param settingsStream settings source
     * @param settings format
     * @return a new instance of settingClassName which extends {@link RestExpressSettings}
     * @throws JsonParseException
     * @throws JsonMappingException
     * @throws IOException
     */
    public static RestExpressSettings loadFrom(final InputStream settingsStream, final Settings settings) throws JsonParseException,
            JsonMappingException, IOException {
        RestExpressSettings expressSettings = null;
        switch (settings) {
            case JSON:
                expressSettings = getObjectMapper().readValue(settingsStream, RestExpressSettings.class);
                break;
            case YAML:
                expressSettings = (RestExpressSettings) getYaml().load(settingsStream);
                break;
            default:
                expressSettings = defaultRestExpressSettings();
                break;
        }
        return expressSettings;
    }

    /**
     * Save settings in specified file.
     * 
     * @param settingsPath settings source path
     * @param restExpressSettings settings instance to save
     * @param settings format
     * @throws IOException
     * @throws JsonMappingException
     * @throws JsonGenerationException
     */
    public static void save(final Path settingsPath, final RestExpressSettings restExpressSettings, final Settings settings)
            throws JsonGenerationException, JsonMappingException, IOException {
        switch (settings) {
            case YAML:
                Writer writer = null;
                try {
                    writer = new FileWriter(settingsPath.toFile());
                    getYaml().dump(restExpressSettings, writer);
                } finally {
                    writer.close();
                }
                break;
            case JSON:
            default:
                getObjectMapper().writeValue(settingsPath.toFile(), restExpressSettings);
                break;
        }

    }

    /**
     * @return an {@link ObjectMapper} instance used only here.
     */
    static ObjectMapper getObjectMapper() {
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
