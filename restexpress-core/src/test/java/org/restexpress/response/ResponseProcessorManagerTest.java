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
        assertEquals("txt",ResponseProcessorManager.parseFormatFromUrl("://dummmy.com/fichier.txt"));
        assertEquals("txt",ResponseProcessorManager.parseFormatFromUrl("://dummmy.com/fichier.txt?q="));
        assertEquals("txt",ResponseProcessorManager.parseFormatFromUrl("://dummmy.com/path1/path2/fichier.txt?q=http://ahaha.com/file.json"));
        assertEquals("json",ResponseProcessorManager.parseFormatFromUrl("://dummmy.com/path1/path2/fichier.json?q=http://ahaha.com/file.txt"));
        assertEquals(null,ResponseProcessorManager.parseFormatFromUrl("://dummmy.com"));
        assertEquals(null,ResponseProcessorManager.parseFormatFromUrl("://dummmy.com?q="));
        assertEquals(null,ResponseProcessorManager.parseFormatFromUrl("://dummmy.com/file"));
        assertEquals(null,ResponseProcessorManager.parseFormatFromUrl("://dummmy.com/file."));
    }
}
