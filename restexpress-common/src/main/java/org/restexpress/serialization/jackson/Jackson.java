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
package org.restexpress.serialization.jackson;

import java.text.SimpleDateFormat;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.datatype.guava.GuavaModule;
import com.fasterxml.jackson.datatype.joda.JodaModule;
import com.fasterxml.jackson.module.jaxb.JaxbAnnotationModule;

/**
 * Utility to configure {@link ObjectMapper}.
 * 
 * @author <a href="mailto:jguibert@intelligents-ia.com" >Jerome Guibert</a>
 * 
 */
public enum Jackson {
    ;

    /**
     * Configure an {@link XmlMapper} with standard features.
     * 
     * @param mapper an ObjectMapper
     * @return configured {@link XmlMapper}
     */
    public static XmlMapper configureXmlMapper(final XmlMapper mapper) {
        return (XmlMapper) configureObjectMapper(mapper)
        // enable wrap
                .enable(SerializationFeature.WRAP_ROOT_VALUE);
    }

    /**
     * Configure an {@link ObjectMapper} with standard features.
     * 
     * @param mapper an ObjectMapper
     * @return configured {@link ObjectMapper}
     */
    public static ObjectMapper configureObjectMapper(final ObjectMapper mapper) {
        return mapper
        // .enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT)
                .disable(SerializationFeature.FAIL_ON_EMPTY_BEANS)
                // Ignore additional/unknown properties in a payload.
                .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
                // Only serialize populated properties (do no serialize nulls)
                .setSerializationInclusion(JsonInclude.Include.NON_NULL)
                // Use fields directly.
                .setVisibility(PropertyAccessor.FIELD, Visibility.ANY)
                // Ignore accessor and mutator methods (use fields per above).
                .setVisibility(PropertyAccessor.GETTER, Visibility.NONE)//
                .setVisibility(PropertyAccessor.SETTER, Visibility.NONE)//
                .setVisibility(PropertyAccessor.IS_GETTER, Visibility.NONE)
                // Set default date output format.
                .setDateFormat(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'"));
    }

    /**
     * Register module on specified mapper ({@link ObjectMapper} or {@link XmlMapper}).
     * 
     * @param mapper
     * @return configured mapper.
     */
    public static ObjectMapper registerModule(ObjectMapper mapper) {
        mapper.registerModule(new RestExpressJacksonJsonModule());
        mapper.registerModule(new GuavaModule());
        mapper.registerModule(new JodaModule());
        if (XmlMapper.class.isAssignableFrom(mapper.getClass())) {
            mapper.registerModule(new JaxbAnnotationModule());
        }
        return mapper;
    }

    /**
     * @return an initialized {@link XmlMapper}.
     */
    public static XmlMapper newXmlMapper() {
        return (XmlMapper) registerModule(configureXmlMapper(new XmlMapper()));
    }

    /**
     * @return an initialized {@link ObjectMapper}.
     */
    public static ObjectMapper newObjectMapper() {
        return registerModule(configureObjectMapper(new ObjectMapper()));
    }
}
