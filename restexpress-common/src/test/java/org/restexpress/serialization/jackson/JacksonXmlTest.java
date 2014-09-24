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
import org.restexpress.domain.metadata.RouteMetadata;
import org.restexpress.domain.metadata.ServerMetadata;
import org.restexpress.domain.metadata.UriMetadata;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

/**
 * {@link JacksonXmlTest}.
 * 
 * @author <a href="mailto:jguibert@intelligents-ia.com" >Jerome Guibert</a>
 */
public class JacksonXmlTest {

    private static final String XML = "<KnownObject><integer>2</integer><string>another string value</string><date>1963-12-06T12:30:00.000Z</date></KnownObject>";

    private XmlMapper objectMapper = Jackson.newXmlMapper();

    @Test
    public void shouldUnderstandMetadata() throws JsonParseException, JsonMappingException, IOException {
        List<RouteMetadata> list = new ArrayList<RouteMetadata>();
        list.add(new RouteMetadata("api", new UriMetadata("uri"), new ArrayList<String>(), new ArrayList<String>(), true));
        ServerMetadata serverMetadata = new ServerMetadata("test", 8081, "httpl/localhost:80801", new HashSet<String>(), "json", list);
        String xml = objectMapper.writeValueAsString(serverMetadata);
        ServerMetadata metadata = objectMapper.readValue(xml, ServerMetadata.class);
        assertNotNull(metadata.getRoutes());
        assertFalse(metadata.getRoutes().isEmpty());
        assertNotNull(metadata.getRoutes().get(0));
        RouteMetadata routeMetadata = metadata.getRoutes().get(0);
        assertEquals("api", routeMetadata.getName());
    }

    @Test
    public void shouldSerializeObject() throws JsonProcessingException {
        String xml = objectMapper.writeValueAsString(new KnownObject());
        assertNotNull(xml);
        assertTrue(xml.startsWith("<KnownObject"));
        assertTrue(xml.contains("<integer>1</integer>"));
        assertTrue(xml.contains("<string>string value</string>"));
        assertTrue(xml.contains("<date>1964-12-17T"));
        assertTrue(xml.endsWith("</KnownObject>"));
    }

    @Test
    public void shouldDeserializeObject() throws JsonParseException, JsonMappingException, IOException {
        KnownObject o = objectMapper.readValue(XML, KnownObject.class);
        assertNotNull(o);
        assertTrue(o.getClass().isAssignableFrom(KnownObject.class));
        assertEquals(2, o.integer);
        assertEquals("another string value", o.string);
        Calendar c = Calendar.getInstance();
        c.setTime(o.date);
        assertEquals(11, c.get(Calendar.MONTH));
        assertEquals(6, c.get(Calendar.DAY_OF_MONTH));
        assertEquals(1963, c.get(Calendar.YEAR));
    }

    @Test
    public void shouldDeserializeEmptyObject() throws JsonParseException, JsonMappingException, IOException {
        KnownObject o = objectMapper.readValue("<KnownObject/>", KnownObject.class);
        assertNotNull(o);
    }

    @Test(expected = IOException.class)
    public void shouldDeserializeEmptyString() throws JsonParseException, JsonMappingException, IOException {
        objectMapper.readValue("", KnownObject.class);
    }

    @Test(expected = NullPointerException.class)
    public void shouldDeserializeNullString() throws JsonParseException, JsonMappingException, IOException {
        objectMapper.readValue((String) null, KnownObject.class);
    }

    @Test
    public void shouldEncodeSerializedXssArray() throws JsonProcessingException {
        KnownObject ko = new KnownObject();
        ko.sa = new String[] { "this", "is", "an", "evil", "Json", "<script>alert(\'xss')</script>" };
        String xml = objectMapper.writeValueAsString(ko);
        System.err.println(xml);
        assertNotNull(xml);
        assertTrue(xml.startsWith("<KnownObject"));
        assertTrue(xml.contains("<integer>1</integer>"));
        assertTrue(xml.contains("<date>1964-12-17T15:30:00.000Z</date>"));
        assertTrue(xml.contains("<p>something private</p>"));
        assertTrue(xml.contains("<sa>&amp;lt;script&amp;gt;alert('xss')&amp;lt;/script&amp;gt;</sa>"));
        assertFalse(xml.contains("<q>"));
        assertTrue(xml.endsWith("</KnownObject>"));
    }

    @Test
    public void shouldEncodeSerializedXssString() throws JsonProcessingException {
        KnownObject ko = new KnownObject();
        ko.string = "<script>alert('xss')</script>";
        String xml = objectMapper.writeValueAsString(ko);
        assertNotNull(xml);
        assertTrue(xml.startsWith("<KnownObject"));
        assertTrue(xml.contains("<integer>1</integer>"));
        assertTrue(xml.contains("<string>&amp;lt;script&amp;gt;alert('xss')&amp;lt;/script&amp;gt;</string>"));
        assertTrue(xml.contains("<date>1964-12-17T15:30:00.000Z</date>"));
        assertTrue(xml.contains("<p>something private</p>"));
        assertFalse(xml.contains("<q>"));
        assertFalse(xml.contains("<sa>"));
        assertTrue(xml.endsWith("</KnownObject>"));
    }
}
