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
package org.restexpress.plugin.gson.json;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Date;
import java.util.List;

import org.jboss.netty.buffer.ChannelBuffer;
import org.restexpress.domain.MediaType;
import org.restexpress.exception.DeserializationException;
import org.restexpress.exception.SerializationException;
import org.restexpress.serialization.JsonProcessor;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;

/**
 * A SerializationProcessor to handle JSON input/output using GSON. It anticipates ISO 8601-compatible time points for date instances
 * and outputs dates as ISO 8601 time points.
 * 
 * @author toddf
 * @since Mar 16, 2010
 */
public class GsonJsonProcessor extends JsonProcessor {
    private final Gson gson;

    public GsonJsonProcessor(List<String> mediaTypes) {
        super(mediaTypes);
        gson = newGson();
    }

    public GsonJsonProcessor(MediaType... mediaTypes) {
        super(mediaTypes);
        gson = newGson();
    }

    public GsonJsonProcessor(String... mediaTypes) throws IllegalArgumentException {
        super(mediaTypes);
        gson = newGson();
    }

    public GsonJsonProcessor() {
        super();
        gson = newGson();
    }

    public GsonJsonProcessor(final Gson gson) {
        super();
        this.gson = gson;
    }

    public GsonJsonProcessor(final Gson gson, List<String> mediaTypes) {
        super(mediaTypes);
        this.gson = gson;
    }

    public GsonJsonProcessor(final Gson gson, MediaType... mediaTypes) {
        super(mediaTypes);
        this.gson = gson;
    }

    public GsonJsonProcessor(final Gson gson, String... mediaTypes) throws IllegalArgumentException {
        super(mediaTypes);
        this.gson = gson;
    }

    @Override
    public <T> T read(ChannelBuffer buffer, Class<T> valueType) throws DeserializationException {
        try {
            return gson.fromJson(getInputStreamReader(buffer), valueType);
        } catch (JsonSyntaxException | JsonIOException e) {
            throw new DeserializationException(e);
        }
    }

    @Override
    public void write(Object value, ChannelBuffer buffer) throws SerializationException {
        try {
            if (value != null) {
                OutputStreamWriter writer = getOutputStreamWriter(buffer);
                gson.toJson(value, writer);
                writer.flush();
            }
        } catch (JsonIOException | IOException e) {
            throw new SerializationException(e);
        }
    }

    protected Gson newGson() {
        return new GsonBuilder().disableHtmlEscaping().registerTypeAdapter(Date.class, new GsonTimepointSerializer()) //
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES) //
                .create();
    }
}
