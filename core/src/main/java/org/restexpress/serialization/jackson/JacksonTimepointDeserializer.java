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
package org.restexpress.serialization.jackson;

import java.io.IOException;
import java.text.ParseException;
import java.util.Date;

import org.intelligentsia.commons.http.HttpHeaderDateTimeFormat;
import org.restexpress.exception.DeserializationException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

/**
 * {@link JacksonTimepointDeserializer} deserialize {@link Date}.
 * 
 * @author <a href="mailto:jguibert@intelligents-ia.com" >Jerome Guibert</a>
 */
public class JacksonTimepointDeserializer extends JsonDeserializer<Date> {

    public JacksonTimepointDeserializer() {
        super();
    }

    @Override
    public Date deserialize(final JsonParser parser, final DeserializationContext context) throws IOException, JsonProcessingException {
        try {
            return HttpHeaderDateTimeFormat.parseAny(parser.getText());
        } catch (final ParseException e) {
            throw new DeserializationException(e);
        }
    }
}