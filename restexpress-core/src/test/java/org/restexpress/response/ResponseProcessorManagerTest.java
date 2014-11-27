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
package org.restexpress.response;

import com.google.common.collect.Lists;
import org.junit.Test;

import static org.junit.Assert.*;

import org.restexpress.domain.CharacterSet;
import org.restexpress.domain.MediaType;

import java.util.List;

public class ResponseProcessorManagerTest {

    @Test
    public void addMediaRangesIfNotExits() {
        List<String> supportedMediaTypes = Lists.newArrayList(
                MediaType.APPLICATION_JSON.withCharset(CharacterSet.UTF_8.getCharsetName()),
                MediaType.APPLICATION_ALL_JSON.withCharset(CharacterSet.UTF_8.getCharsetName()),
                MediaType.APPLICATION_JAVASCRIPT.withCharset(CharacterSet.UTF_8.getCharsetName()),
                MediaType.TEXT_JAVASCRIPT.withCharset(CharacterSet.UTF_8.getCharsetName()),
                MediaType.APPLICATION_HAL_JSON.withCharset(CharacterSet.UTF_8.getCharsetName()));
        List<MediaRange> supportedMediaRanges = MediaRanges.parse(supportedMediaTypes);
        final int expectedSize = supportedMediaRanges.size();

        // create response processor manager and initialize it
        ResponseProcessorManager responseProcessorManager = new ResponseProcessorManager();
        responseProcessorManager.addMediaRanges(supportedMediaRanges);
        // all is good
        assertEquals(expectedSize, responseProcessorManager.supportedMediaRanges.size());
        // add one ever set
        responseProcessorManager.addMediaRanges(Lists.newArrayList(MediaRange.parse(MediaType.APPLICATION_JSON.withCharset(CharacterSet.UTF_8.getCharsetName()))));
        // check no one more
        assertEquals(expectedSize, responseProcessorManager.supportedMediaRanges.size());
    }


    @Test(expected = java.lang.NullPointerException.class)
    public void checkParseFormatFromNullUrl() {
        ResponseProcessorManager.parseFormatFromUrl(null);
    }

    @Test
    public void checkParseFormatFromEmptyUrl() {
        assertEquals(null,ResponseProcessorManager.parseFormatFromUrl(""));
    }

    @Test
    public void checkParseFormatFromPath() {
        assertEquals("txt",ResponseProcessorManager.parseFormatFromUrl("http://dummmy.com/fichier.txt"));
        assertEquals("txt",ResponseProcessorManager.parseFormatFromUrl("http://dummmy.com/fichier.txt?q="));
        assertEquals("txt",ResponseProcessorManager.parseFormatFromUrl("https://dummmy.com/path1/path2/fichier.txt?q=http://ahaha.com/file.json"));
        assertEquals("json",ResponseProcessorManager.parseFormatFromUrl("https://dummmy.com/path1/path2/fichier.json?q=http://ahaha.com/file.txt"));
        assertEquals(null,ResponseProcessorManager.parseFormatFromUrl("http://dummmy.com"));
        assertEquals(null,ResponseProcessorManager.parseFormatFromUrl("://dummmy.com?q="));
        assertEquals(null,ResponseProcessorManager.parseFormatFromUrl("://dummmy.com/file"));
        assertEquals(null,ResponseProcessorManager.parseFormatFromUrl("://dummmy.com/file."));
    }
}
