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
package org.restexpress.serialization;

import java.io.IOException;
import java.util.List;

import org.jboss.netty.buffer.ChannelBuffer;
import org.restexpress.domain.MediaType;
import org.restexpress.exception.DeserializationException;
import org.restexpress.exception.SerializationException;
import org.restexpress.serialization.jackson.Jackson;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

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
        this.objectMapper = Jackson.newXmlMapper();
    }

    public JacksonXmlProcessor(List<String> supportedMediaType) {
        super(supportedMediaType);
        this.objectMapper = Jackson.newXmlMapper();
    }

    public JacksonXmlProcessor(MediaType... mediaTypes) {
        super(mediaTypes);
        this.objectMapper = Jackson.newXmlMapper();
    }

    public JacksonXmlProcessor(String... mediaTypes) throws IllegalArgumentException {
        super(mediaTypes);
        this.objectMapper = Jackson.newXmlMapper();
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

    /**
     * @return {@link XmlMapper} instance.
     */
    public XmlMapper getObjectMapper() {
        return objectMapper;
    }
}
