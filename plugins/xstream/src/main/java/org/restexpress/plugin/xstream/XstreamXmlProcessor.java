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
package org.restexpress.plugin.xstream;

import java.util.ArrayList;
import java.util.List;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBufferInputStream;
import org.restexpress.domain.MediaType;
import org.restexpress.domain.response.JsendResult;
import org.restexpress.exception.DeserializationException;
import org.restexpress.exception.SerializationException;
import org.restexpress.serialization.XmlProcessor;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.XStreamException;
import com.thoughtworks.xstream.converters.SingleValueConverter;

public class XstreamXmlProcessor extends XmlProcessor implements Aliasable {

    private final XStream xstream;

    public XstreamXmlProcessor() {
        super();
        xstream = newXStream();
    }

    public XstreamXmlProcessor(String... mediaTypes) throws IllegalArgumentException {
        super(mediaTypes);
        xstream = newXStream();
    }

    public XstreamXmlProcessor(List<String> supportedMediaType) {
        super(supportedMediaType);
        xstream = newXStream();
    }

    public XstreamXmlProcessor(MediaType... mediaTypes) {
        super(mediaTypes);
        xstream = newXStream();
    }

    public XstreamXmlProcessor(XStream xstream) {
        super();
        this.xstream = xstream;
    }

    public XstreamXmlProcessor(XStream xstream, String... mediaTypes) throws IllegalArgumentException {
        super(mediaTypes);
        this.xstream = xstream;
    }

    public XstreamXmlProcessor(XStream xstream, List<String> supportedMediaType) {
        super(supportedMediaType);
        this.xstream = xstream;
    }

    public XstreamXmlProcessor(XStream xstream, MediaType... mediaTypes) {
        super(mediaTypes);
        this.xstream = xstream;
    }

    @Override
    public void write(Object value, ChannelBuffer buffer) throws SerializationException {
        if (value != null) {
            xstream.toXML(value, getOutputStreamWriter(buffer));
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T read(ChannelBuffer buffer, Class<T> valueType) throws DeserializationException {
        if (!buffer.readable()) {
            return null;
        }
        try {
            return (T) xstream.fromXML(new ChannelBufferInputStream(buffer));
        } catch (XStreamException e) {
            throw new DeserializationException(e);
        }
    }

    @Override
    public void alias(String name, Class<?> theClass) {
        xstream.alias(name, theClass);
    }

    public void registerConverter(final SingleValueConverter converter) {
        xstream.registerConverter(converter);
    }

    protected XStream newXStream() {
        XStream xstream = new XStream();
        xstream.registerConverter(new XstreamTimestampConverter());
        xstream.alias("list", ArrayList.class);
        xstream.alias("response", JsendResult.class);
        return xstream;
    }
}
