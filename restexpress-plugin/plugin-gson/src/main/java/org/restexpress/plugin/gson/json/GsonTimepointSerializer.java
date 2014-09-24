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
package org.restexpress.plugin.gson.json;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.util.Date;

import org.intelligentsia.commons.http.HttpHeaderDateTimeFormat;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;

/**
 * A GSON serializer for Date instances represented (and to be presented) as a timestamps (dates with time component).
 * 
 * @author <a href="mailto:jguibert@intelligents-ia.com" >Jerome Guibert</a>
 * @author toddf
 * @since Nov 13, 2009
 */
public class GsonTimepointSerializer implements GsonSerializer<Date> {
    @Override
    public Date deserialize(final JsonElement json, final Type typeOf, final JsonDeserializationContext context)
            throws JsonParseException {
        try {
            return HttpHeaderDateTimeFormat.parseAny(json.getAsJsonPrimitive().getAsString());
        } catch (final ParseException e) {
            throw new JsonParseException(e);
        }
    }

    @Override
    public JsonElement serialize(final Date date, final Type typeOf, final JsonSerializationContext context) {
        return new JsonPrimitive(HttpHeaderDateTimeFormat.ISO_8601.format(date));
    }

    @Override
    public Date createInstance(final Type typeOf) {
        return new Date();
    }
}
