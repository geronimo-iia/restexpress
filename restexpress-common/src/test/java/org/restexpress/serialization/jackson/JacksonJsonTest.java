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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;

import org.junit.Test;
import org.restexpress.domain.CharacterSet;
import org.restexpress.domain.metadata.RouteMetadata;
import org.restexpress.domain.metadata.ServerMetadata;
import org.restexpress.domain.metadata.UriMetadata;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * {@link JacksonJsonTest}
 * 
 * @author <a href="mailto:jguibert@intelligents-ia.com" >Jerome Guibert</a>
 * @author toddf
 * @since Aug 4, 2011
 */
public class JacksonJsonTest {
    private static final String JSON = "{\"integer\":2,\"string\":\"another string value\",\"date\":\"1963-12-06T12:30:00.000Z\",\"p\":\"good stuff\"}";
    private static final String JSON_UTF8 = "{\"integer\":2,\"string\":\"我能吞下\",\"date\":\"1963-12-06T12:30:00.000Z\"}";

    private ObjectMapper objectMapper = Jackson.newObjectMapper();

    @Test
    public void shouldUnderstandMetadata() throws JsonParseException, JsonMappingException, IOException {
        List<RouteMetadata> list = new ArrayList<RouteMetadata>();
        list.add(new RouteMetadata("api", new UriMetadata("uri"), new ArrayList<String>(), new ArrayList<String>(), true));
        ServerMetadata serverMetadata = new ServerMetadata("test", 8081, "httpl/localhost:80801", new HashSet<String>(), "json", list);
        String json = objectMapper.writeValueAsString(serverMetadata);
        ServerMetadata metadata = objectMapper.readValue(json, ServerMetadata.class);
        assertNotNull(metadata.getRoutes());
        assertFalse(metadata.getRoutes().isEmpty());
        assertNotNull(metadata.getRoutes().get(0));
        RouteMetadata routeMetadata = metadata.getRoutes().get(0);
        assertEquals("api", routeMetadata.getName());
    }

    @Test
    public void shouldSerializeObject() throws JsonProcessingException {
        String json = objectMapper.writeValueAsString(new KnownObject());
        assertNotNull(json);
        assertTrue(json.startsWith("{"));
        assertTrue(json.contains("\"integer\":1"));
        assertTrue(json.contains("\"string\":\"string value\""));
        assertTrue(json.contains("\"date\":\"1964-12-17T"));
        assertTrue(json.contains("\"p\":\"something private"));
        assertFalse(json.contains("\"q\":"));
        assertTrue(json.endsWith("}"));
    }

    @Test
    public void shouldSerializeNull() throws JsonProcessingException {
        String json = objectMapper.writeValueAsString(null);
        assertEquals("null", json);
    }

    @Test
    public void shouldDeserializeObject() throws JsonParseException, JsonMappingException, IOException {
        KnownObject o = objectMapper.readValue(JSON, KnownObject.class);
        assertNotNull(o);
        assertEquals(2, o.integer);
        assertEquals("another string value", o.string);
        Calendar c = Calendar.getInstance();
        c.setTime(o.date);
        assertEquals(11, c.get(Calendar.MONTH));
        assertEquals(6, c.get(Calendar.DAY_OF_MONTH));
        assertEquals(1963, c.get(Calendar.YEAR));
        assertEquals("good stuff", o.getP());
    }

    @Test
    public void shouldDeserializeEmptyObject() throws JsonParseException, JsonMappingException, IOException {
        KnownObject o = objectMapper.readValue("{}", KnownObject.class);
        assertNotNull(o);
    }

    @Test(expected = JsonMappingException.class)
    public void shouldDeserializeEmptyString() throws JsonParseException, JsonMappingException, IOException {
        objectMapper.readValue("", KnownObject.class);
    }

    @Test(expected = NullPointerException.class)
    public void shouldDeserializeNullString() throws JsonParseException, JsonMappingException, IOException {
        objectMapper.readValue((String) null, KnownObject.class);
    }

    @Test
    public void shouldDeserializeUTF8ChannelBuffer() throws JsonParseException, JsonMappingException, IOException {
        KnownObject o = objectMapper.readValue(JSON_UTF8.getBytes(CharacterSet.UTF_8.getCharset()), KnownObject.class);
        assertNotNull(o);
        assertTrue(o.getClass().isAssignableFrom(KnownObject.class));
        assertEquals(2, o.integer);
        assertEquals("我能吞下", o.string);
        Calendar c = Calendar.getInstance();
        c.setTime(o.date);
        assertEquals(11, c.get(Calendar.MONTH));
        assertEquals(6, c.get(Calendar.DAY_OF_MONTH));
        assertEquals(1963, c.get(Calendar.YEAR));
    }

    @Test
    public void shouldEncodeSerializedXssJsonArray() throws JsonProcessingException {
        KnownObject ko = new KnownObject();
        ko.sa = new String[] { "this", "is", "an", "evil", "Json", "<script>alert(\'xss')</script>" };
        String json = objectMapper.writeValueAsString(ko);
        assertNotNull(json);
        System.err.println(json);
        assertTrue(json.startsWith("{"));
        assertTrue(json.contains("\"integer\":1"));
        assertTrue(json.contains("\"string\":\"string value\""));
        assertTrue(json.contains("\"date\":\"1964-12-17T15:30:00.000Z"));
        assertTrue(json.contains("\"p\":\"something private"));
        assertFalse(json.contains("\"q\":"));
        assertTrue(json.contains("\"sa\":[\"this\",\"is\",\"an\",\"evil\",\"Json\",\"&lt;script&gt;alert('xss')&lt;/script&gt;\"]"));
        assertTrue(json.endsWith("}"));
    }

    @Test
    public void shouldEncodeSerializedXssJsonString() throws JsonProcessingException {
        KnownObject ko = new KnownObject();
        ko.string = "<script>alert('xss')</script>";
        String json = objectMapper.writeValueAsString(ko);
        assertNotNull(json);
        assertTrue(json.startsWith("{"));
        assertTrue(json.contains("\"integer\":1"));
        assertTrue(json.contains("\"string\":\"&lt;script&gt;alert('xss')&lt;/script&gt;"));
        assertTrue(json.contains("\"date\":\"1964-12-17T15:30:00.000Z"));
        assertTrue(json.contains("\"p\":\"something private"));
        assertFalse(json.contains("\"q\":"));
        assertFalse(json.contains("\"sa\":"));
        assertTrue(json.endsWith("}"));
    }
}
