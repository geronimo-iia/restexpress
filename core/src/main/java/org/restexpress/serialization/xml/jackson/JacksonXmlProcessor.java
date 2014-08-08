/**
 * Licensed to the Apache Software Foundation (ASF) under one or more contributor license agreements. See the NOTICE file distributed
 * with this work for additional information regarding copyright ownership. The ASF licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License
 * at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS"
 * BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing
 * permissions and limitations under the License.
 * 
 */
package org.restexpress.serialization.xml.jackson;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;

import org.jboss.netty.buffer.ChannelBuffer;
import org.restexpress.domain.MediaType;
import org.restexpress.exception.DeserializationException;
import org.restexpress.exception.SerializationException;
import org.restexpress.serialization.Processor;
import org.restexpress.serialization.json.jackson.RestExpressJacksonJsonModule;
import org.restexpress.serialization.xml.XmlProcessor;

import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.datatype.guava.GuavaModule;
import com.fasterxml.jackson.datatype.joda.JodaModule;
import com.fasterxml.jackson.module.jaxb.JaxbAnnotationModule;
import com.strategicgains.util.date.DateAdapterConstants;

/**
 * {@link JacksonXmlProcessor} implement an XML {@link Processor} with Jackson.
 * 
 * Default initilaized module:
 * <ul>
 * <li>RestExpressJacksonJsonModule</li>
 * <li>GuavaModule</li>
 * <li>JodaModule</li>
 * <li>JaxbAnnotationModule</li>
 * </ul>
 * 
 * @author <a href="mailto:jguibert@intelligents-ia.com" >Jerome Guibert</a>
 */
public class JacksonXmlProcessor extends XmlProcessor {

    private final XmlMapper objectMapper;

    /**
     * Build a new instance.
     */
    public JacksonXmlProcessor() {
        super();
        this.objectMapper = newinitializedXmlMapper();
    }

    public JacksonXmlProcessor(List<String> supportedMediaType) {
        super(supportedMediaType);
        this.objectMapper = newinitializedXmlMapper();
    }

    public JacksonXmlProcessor(MediaType... mediaTypes) {
        super(mediaTypes);
        this.objectMapper = newinitializedXmlMapper();
    }

    public JacksonXmlProcessor(String... mediaTypes) throws IllegalArgumentException {
        super(mediaTypes);
        this.objectMapper = newinitializedXmlMapper();
    }

    public JacksonXmlProcessor(XmlMapper objectMapper, List<String> mediaTypes) throws IllegalArgumentException {
        super(mediaTypes);
        this.objectMapper = objectMapper;
    }

    public JacksonXmlProcessor(XmlMapper objectMapper, MediaType... mediaTypes) throws IllegalArgumentException {
        super(mediaTypes);
        this.objectMapper = objectMapper;
    }

    public JacksonXmlProcessor(XmlMapper objectMapper, String... mediaTypes) throws IllegalArgumentException {
        super(mediaTypes);
        this.objectMapper = objectMapper;
    }

    @Override
    public <T> T read(ChannelBuffer buffer, Class<T> valueType) throws DeserializationException {
        try {
            return ((buffer == null) || (buffer.readableBytes() == 0) ? null : objectMapper.readValue(getInputStreamReader(buffer),
                    valueType));
        } catch (final JsonProcessingException e) {
            throw new DeserializationException(e);
        } catch (final IOException e) {
            throw new DeserializationException(e);
        }
    }

    @Override
    public void write(Object value, ChannelBuffer buffer) throws SerializationException {
        try {
            if (value != null) {
                objectMapper.writeValue(getOutputStreamWriter(buffer), value);
            }
        } catch (final JsonProcessingException e) {
            throw new SerializationException(e);
        } catch (IOException e) {
            throw new SerializationException(e);
        }
    }

    @Override
    public void alias(String name, Class<?> theClass) {
    }

    /**
     * Initialize serialization, visibility, etc..
     * 
     * @param mapper
     */
    public XmlMapper initializeMapper(final XmlMapper mapper) {
        return (XmlMapper) mapper
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
                .setDateFormat(new SimpleDateFormat(DateAdapterConstants.TIME_POINT_OUTPUT_FORMAT))
                // enable wrap
                .enable(SerializationFeature.WRAP_ROOT_VALUE);

    }

    /**
     * Register default module on {@link XmlMapper}.
     * 
     * @param mapper
     * @return {@link XmlMapper} instance.
     */
    public XmlMapper registerModule(XmlMapper mapper) {
        return (XmlMapper) mapper.registerModule(new RestExpressJacksonJsonModule())//
                .registerModule(new GuavaModule())//
                .registerModule(new JodaModule())//
                .registerModule(new JaxbAnnotationModule());
    }

    /**
     * @return an initialized {@link XmlMapper}.
     */
    public XmlMapper newinitializedXmlMapper() {
        return (XmlMapper) registerModule(initializeMapper(new XmlMapper()));
    }
    //
    // public XmlMapper newXmlMapper() {
    // XmlFactory factory = new XmlFactory(new InputFactoryImpl(), new OutputFactoryImpl());
    // return new XmlMapper(factory);
    // }
}
