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
package org.restexpress.server;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.jboss.netty.handler.codec.http.HttpHeaders;
import org.jboss.netty.handler.codec.http.HttpMethod;
import org.jboss.netty.handler.codec.http.HttpResponseStatus;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.restexpress.Request;
import org.restexpress.Response;
import org.restexpress.RestExpress;
import org.restexpress.domain.CharacterSet;
import org.restexpress.domain.Format;
import org.restexpress.domain.MediaType;
import org.restexpress.pipeline.observer.SimpleConsoleLogMessageObserver;
import org.restexpress.query.QueryRange;
import org.restexpress.response.Wrapper;
import org.restexpress.serialization.JacksonJsonProcessor;
import org.restexpress.serialization.JacksonXmlProcessor;
import org.restexpress.server.processor.ErrorPreprocessor;
import org.restexpress.server.processor.TestPostprocessor;
import org.restexpress.util.TestUtilities;

public class ServerTest {
    private static String JSON = MediaType.APPLICATION_JSON.withCharset(CharacterSet.UTF_8.getCharsetName());
    private static String XML = MediaType.APPLICATION_XML.withCharset(CharacterSet.UTF_8.getCharsetName());
    private static String HAL_JSON = MediaType.APPLICATION_HAL_JSON.withCharset(CharacterSet.UTF_8.getCharsetName());
    private static String TEXT_PLAIN = MediaType.TEXT_PLAIN.withCharset(CharacterSet.UTF_8.getCharsetName());

    private static final String URL_PATTERN1 = "/1/restexpress/{id}/test/{test}.{format}";
    private static final String URL_PATTERN2 = "/2/restexpress/{id}/test/{test}";
    private static final String URL_PATTERN3 = "/3/restexpress/{id}/test/{test}.{format}";
    private static final String URL_PATTERN4 = "/4/restexpress/{id}/test/{test}.{format}";
    private static final String LITTLE_O_PATTERN = "/littleos/{id}.{format}";
    private static final String LITTLE_OS_PATTERN = "/littleos.{format}";
    private static final String URL_PATH1 = "/1/restexpress/sam/test/42";
    private static final String URL_PATH3 = "/3/restexpress/polly/test/56";
    private static final String URL_PATH4 = "/4/restexpress/allen/test/33";
    private static final String LITTLE_O_PATH = "/littleos/1";
    private static final String LITTLE_OS_PATH = "/littleos";
    private static final int SERVER_PORT = 8800;
    private static final String SERVER_HOST = "http://localhost:" + SERVER_PORT;
    private static final String URL1_PLAIN = SERVER_HOST + URL_PATH1;
    private static final String URL1_JSON = SERVER_HOST + URL_PATH1 + ".json";
    private static final String URL1_XML = SERVER_HOST + URL_PATH1 + ".xml";
    private static final String URL3_PLAIN = SERVER_HOST + URL_PATH3;
    private static final String URL4_PLAIN = SERVER_HOST + URL_PATH4;
    private static final String LITTLE_O_URL = SERVER_HOST + LITTLE_O_PATH;
    private static final String LITTLE_OS_URL = SERVER_HOST + LITTLE_OS_PATH;
    private static final String PATTERN_EXCEPTION_STRING = "/strings/exception";
    private static final String PATTERN_EXCEPTION_LITTLE_O = "/objects/exception";
    @SuppressWarnings("unused")
    private static final String URL_EXCEPTION_STRING = SERVER_HOST + PATTERN_EXCEPTION_STRING;
    private static final String URL_EXCEPTION_LITTLE_O = SERVER_HOST + PATTERN_EXCEPTION_LITTLE_O;

    private RestExpress server;
    private HttpClient http = new DefaultHttpClient();

    @Before
    public void createServer() {
        server = new RestExpress();

        StringTestController stringTestController = new StringTestController();
        ObjectTestController objectTestController = new ObjectTestController();

        server.uri(URL_PATTERN1, stringTestController);
        server.uri(URL_PATTERN2, stringTestController);
        server.uri(URL_PATTERN3, stringTestController).method(HttpMethod.GET, HttpMethod.POST);
        server.uri(PATTERN_EXCEPTION_STRING, stringTestController).action("throwException", HttpMethod.GET);
        server.uri(URL_PATTERN4, stringTestController) // Collection route.
                .method(HttpMethod.POST).action("readAll", HttpMethod.GET);
        server.uri(LITTLE_O_PATTERN, objectTestController).method(HttpMethod.GET);
        server.uri(LITTLE_OS_PATTERN, objectTestController).action("readAll", HttpMethod.GET);
        server.uri(PATTERN_EXCEPTION_LITTLE_O, objectTestController).action("throwException", HttpMethod.GET);
        server.addMessageObserver(new SimpleConsoleLogMessageObserver());

    }

    @After
    public void shutdownServer() {
        server.shutdown();
    }

    // SECTION: TESTS

    @Test
    public void shouldHandleGetRequests() throws Exception {
        server.bind(SERVER_PORT);

        HttpGet request = new HttpGet(URL1_PLAIN);
        HttpResponse response = (HttpResponse) http.execute(request);
        assertEquals(HttpResponseStatus.OK.getCode(), response.getStatusLine().getStatusCode());
        HttpEntity entity = response.getEntity();
        assertTrue(entity.getContentLength() > 0l);
        assertEquals(JSON, entity.getContentType().getValue());
        assertEquals("\"read\"", EntityUtils.toString(entity));
        request.releaseConnection();
    }

    @Test
    public void shouldHandlePutRequests() throws Exception {
        server.bind(SERVER_PORT);

        HttpPut request = new HttpPut(URL1_PLAIN);
        HttpResponse response = (HttpResponse) http.execute(request);
        assertEquals(HttpResponseStatus.OK.getCode(), response.getStatusLine().getStatusCode());
        HttpEntity entity = response.getEntity();
        assertTrue(entity.getContentLength() > 0l);
        assertEquals(JSON, entity.getContentType().getValue());
        assertEquals("\"update\"", EntityUtils.toString(entity));
        request.releaseConnection();
    }

    @Test
    public void shouldHandlePostRequests() throws Exception {
        server.bind(SERVER_PORT);

        HttpPost request = new HttpPost(URL1_PLAIN);
        HttpResponse response = (HttpResponse) http.execute(request);
        assertEquals(HttpResponseStatus.CREATED.getCode(), response.getStatusLine().getStatusCode());
        HttpEntity entity = response.getEntity();
        assertTrue(entity.getContentLength() > 0l);
        assertEquals(JSON, entity.getContentType().getValue());
        assertEquals("\"create\"", EntityUtils.toString(entity));
        request.releaseConnection();
    }

    @Test
    public void shouldHandleDeleteRequests() throws Exception {
        server.bind(SERVER_PORT);

        HttpDelete request = new HttpDelete(URL1_PLAIN);
        HttpResponse response = (HttpResponse) http.execute(request);
        assertEquals(HttpResponseStatus.OK.getCode(), response.getStatusLine().getStatusCode());
        HttpEntity entity = response.getEntity();
        assertTrue(entity.getContentLength() > 0l);
        assertEquals(JSON, entity.getContentType().getValue());
        assertEquals("\"delete\"", EntityUtils.toString(entity));
        request.releaseConnection();
    }

    @Test
    public void shouldCallSpecifiedMethod() throws Exception {
        server.bind(SERVER_PORT);

        HttpGet request = new HttpGet(URL4_PLAIN);
        HttpResponse response = (HttpResponse) http.execute(request);
        assertEquals(HttpResponseStatus.OK.getCode(), response.getStatusLine().getStatusCode());
        HttpEntity entity = response.getEntity();
        assertTrue(entity.getContentLength() > 0l);
        assertEquals(JSON, entity.getContentType().getValue());
        assertEquals("\"readAll\"", EntityUtils.toString(entity));
        request.releaseConnection();
    }

    @Test
    public void shouldFailWithMethodNotAllowed() throws Exception {
        server.bind(SERVER_PORT);

        HttpDelete request = new HttpDelete(URL3_PLAIN);
        HttpResponse response = (HttpResponse) http.execute(request);
        assertEquals(HttpResponseStatus.METHOD_NOT_ALLOWED.getCode(), response.getStatusLine().getStatusCode());
        HttpEntity entity = response.getEntity();
        assertTrue(entity.getContentLength() > 0l);
        assertEquals(JSON, entity.getContentType().getValue());
        assertEquals("{\"httpStatus\":405,\"message\":\"" + URL3_PLAIN + "\",\"errorType\":\"MethodNotAllowedException\"}",
                EntityUtils.toString(entity));
        String methods = response.getHeaders(HttpHeaders.Names.ALLOW)[0].getValue();
        assertTrue(methods.contains("GET"));
        methods = response.getHeaders(HttpHeaders.Names.ALLOW)[1].getValue();
        assertTrue(methods.contains("POST"));
        request.releaseConnection();
    }

    @Test
    public void shouldFailWithNotFound() throws Exception {
        server.bind(SERVER_PORT);

        HttpDelete request = new HttpDelete(SERVER_HOST + "/x/y/z.json");
        HttpResponse response = (HttpResponse) http.execute(request);
        assertEquals(HttpResponseStatus.NOT_FOUND.getCode(), response.getStatusLine().getStatusCode());
        HttpEntity entity = response.getEntity();
        assertTrue(entity.getContentLength() > 0l);
        assertEquals(JSON, entity.getContentType().getValue());
        assertEquals(404, response.getStatusLine().getStatusCode());
        request.releaseConnection();
    }

    @Test
    public void shouldReturnXmlUsingFormat() throws Exception {
        server.bind(SERVER_PORT);

        HttpGet request = new HttpGet(URL1_XML);
        HttpResponse response = (HttpResponse) http.execute(request);
        assertEquals(HttpResponseStatus.OK.getCode(), response.getStatusLine().getStatusCode());
        HttpEntity entity = response.getEntity();
        assertTrue(entity.getContentLength() > 0l);
        assertEquals(XML, entity.getContentType().getValue());
        assertEquals("<String>read</String>", EntityUtils.toString(entity));
        request.releaseConnection();
    }

    @Test
    public void shouldReturnXmlUsingAccept() throws Exception {
        server.bind(SERVER_PORT);

        HttpGet request = new HttpGet(URL1_PLAIN);
        request.addHeader(HttpHeaders.Names.ACCEPT, "application/xml");
        HttpResponse response = (HttpResponse) http.execute(request);
        assertEquals(HttpResponseStatus.OK.getCode(), response.getStatusLine().getStatusCode());
        HttpEntity entity = response.getEntity();
        assertTrue(entity.getContentLength() > 0l);
        assertEquals(XML, entity.getContentType().getValue());
        assertEquals("<String>read</String>", EntityUtils.toString(entity));
        request.releaseConnection();
    }

    @Test
    public void shouldFavorFormatOverAcceptHeader() throws Exception {
        server.bind(SERVER_PORT);

        HttpGet request = new HttpGet(URL1_XML);
        request.addHeader(HttpHeaders.Names.ACCEPT, "application/json");
        HttpResponse response = (HttpResponse) http.execute(request);
        assertEquals(HttpResponseStatus.OK.getCode(), response.getStatusLine().getStatusCode());
        HttpEntity entity = response.getEntity();
        assertTrue(entity.getContentLength() > 0l);
        assertEquals(XML, entity.getContentType().getValue());
        assertEquals("<String>read</String>", EntityUtils.toString(entity));
        request.releaseConnection();
    }

    @Test
    public void shouldReturnJsonUsingFormat() throws Exception {
        server.bind(SERVER_PORT);

        HttpGet request = new HttpGet(URL1_JSON);
        HttpResponse response = (HttpResponse) http.execute(request);
        assertEquals(HttpResponseStatus.OK.getCode(), response.getStatusLine().getStatusCode());
        HttpEntity entity = response.getEntity();
        assertTrue(entity.getContentLength() > 0l);
        assertEquals(JSON, entity.getContentType().getValue());
        assertEquals("\"read\"", EntityUtils.toString(entity));
        request.releaseConnection();
    }

    @Test
    public void shouldReturnErrorOnCapitalizedFormat() throws Exception {
        server.bind(SERVER_PORT);

        HttpGet request = new HttpGet(URL1_PLAIN + ".JSON");
        HttpResponse response = (HttpResponse) http.execute(request);
        assertEquals(HttpResponseStatus.BAD_REQUEST.getCode(), response.getStatusLine().getStatusCode());
        HttpEntity entity = response.getEntity();
        assertTrue(entity.getContentLength() > 0l);
        assertEquals(JSON, entity.getContentType().getValue());
        assertEquals(400, response.getStatusLine().getStatusCode());
        request.releaseConnection();
    }

    @Test
    public void shouldReturnWrappedJsonUsingFormat() throws Exception {
        server.bind(SERVER_PORT);

        HttpGet request = new HttpGet(URL1_PLAIN + ".wjson");
        HttpResponse response = (HttpResponse) http.execute(request);
        assertEquals(HttpResponseStatus.OK.getCode(), response.getStatusLine().getStatusCode());
        HttpEntity entity = response.getEntity();
        assertTrue(entity.getContentLength() > 0l);
        assertEquals(JSON, entity.getContentType().getValue());
        String result = EntityUtils.toString(entity);
        assertEquals(200, response.getStatusLine().getStatusCode());
        assertEquals("\"read\"", result);
        request.releaseConnection();
    }

    @Test
    public void shouldReturnWrappedJsonAsDefaultIfSet() throws Exception {
        server.serializationProvider().add(new JacksonJsonProcessor(), Wrapper.newJsendResponseWrapper(), true);
        server.bind(SERVER_PORT);

        HttpGet request = new HttpGet(URL1_PLAIN);
        HttpResponse response = (HttpResponse) http.execute(request);
        assertEquals(HttpResponseStatus.OK.getCode(), response.getStatusLine().getStatusCode());
        HttpEntity entity = response.getEntity();
        assertTrue(entity.getContentLength() > 0l);
        assertEquals(JSON, entity.getContentType().getValue());
        String result = EntityUtils.toString(entity);
        assertTrue(result.contains("\"status\":\"success\""));
        assertTrue(result.contains("\"data\":\"read\""));
        request.releaseConnection();
    }

    @Test
    public void shouldReturnWrappedXmlUsingFormat() throws Exception {
        server.bind(SERVER_PORT);

        HttpGet request = new HttpGet(URL1_PLAIN + ".wxml");
        HttpResponse response = (HttpResponse) http.execute(request);
        assertEquals(HttpResponseStatus.OK.getCode(), response.getStatusLine().getStatusCode());
        HttpEntity entity = response.getEntity();
        assertTrue(entity.getContentLength() > 0l);
        assertEquals(XML, entity.getContentType().getValue());
        String entityString = EntityUtils.toString(entity);
        System.err.println(entityString);
        assertEquals("<String>read</String>", entityString);
        request.releaseConnection();
    }

    @Test
    public void shouldReturnWrappedXmlAsDefaultIfSet() throws Exception {
        server.serializationProvider().add(new JacksonXmlProcessor(), Wrapper.newJsendResponseWrapper(), true);
        server.bind(SERVER_PORT);

        HttpGet request = new HttpGet(URL1_PLAIN);
        HttpResponse response = (HttpResponse) http.execute(request);
        assertEquals(HttpResponseStatus.OK.getCode(), response.getStatusLine().getStatusCode());
        HttpEntity entity = response.getEntity();
        assertTrue(entity.getContentLength() > 0l);
        assertEquals(XML, entity.getContentType().getValue());
        String entityString = EntityUtils.toString(entity);
        assertEquals("<response><status>success</status><data>read</data></response>", entityString);
        request.releaseConnection();
    }

    @Test
    public void shouldReturnNonSerializedTextPlainResult() throws Exception {
        server.uri("/unserialized", new StringTestController()).noSerialization();
        server.bind(SERVER_PORT);

        HttpGet request = new HttpGet(SERVER_HOST + "/unserialized");
        HttpResponse response = (HttpResponse) http.execute(request);
        assertEquals(HttpResponseStatus.OK.getCode(), response.getStatusLine().getStatusCode());
        HttpEntity entity = response.getEntity();
        assertTrue(entity.getContentLength() > 0l);
        assertEquals(TEXT_PLAIN, entity.getContentType().getValue());
        assertEquals("read", EntityUtils.toString(entity));
        request.releaseConnection();
    }

    @Test
    public void shouldFailWithBadRequest() throws Exception {
        server.bind(SERVER_PORT);

        HttpGet request = new HttpGet(URL1_PLAIN + ".xyz");
        HttpResponse response = (HttpResponse) http.execute(request);
        assertEquals(HttpResponseStatus.BAD_REQUEST.getCode(), response.getStatusLine().getStatusCode());
        HttpEntity entity = response.getEntity();
        assertTrue(entity.getContentLength() > 0l);
        assertEquals(JSON, entity.getContentType().getValue());
        assertEquals(400, response.getStatusLine().getStatusCode());
        request.releaseConnection();
    }

    @Test
    public void shouldFailOnInvalidAccept() throws Exception {
        server.bind(SERVER_PORT);

        HttpGet request = new HttpGet(URL1_PLAIN);
        request.addHeader(HttpHeaders.Names.ACCEPT, "application/nogood");
        HttpResponse response = (HttpResponse) http.execute(request);
        assertEquals(HttpResponseStatus.NOT_ACCEPTABLE.getCode(), response.getStatusLine().getStatusCode());
        HttpEntity entity = response.getEntity();
        assertTrue(entity.getContentLength() > 0l);
        assertEquals(JSON, entity.getContentType().getValue());
        assertEquals(406, response.getStatusLine().getStatusCode());
        request.releaseConnection();
    }

    @Test
    public void shouldSerializeObjectAsJson() throws Exception {
        server.bind(SERVER_PORT);

        HttpGet request = new HttpGet(LITTLE_O_URL + ".json");
        HttpResponse response = (HttpResponse) http.execute(request);
        assertEquals(HttpResponseStatus.OK.getCode(), response.getStatusLine().getStatusCode());
        HttpEntity entity = response.getEntity();
        assertTrue(entity.getContentLength() > 0l);
        assertEquals(JSON, entity.getContentType().getValue());
        LittleO o = TestUtilities.deserialize(EntityUtils.toString(entity), LittleO.class,
                server.serializationProvider().processor(Format.JSON.getMediaType()));
        verifyObject(o);
        request.releaseConnection();
    }

    @Test
    public void shouldSerializeListAsJson() throws Exception {
        server.bind(SERVER_PORT);

        HttpGet request = new HttpGet(LITTLE_OS_URL + ".json");
        HttpResponse response = (HttpResponse) http.execute(request);
        assertEquals(HttpResponseStatus.OK.getCode(), response.getStatusLine().getStatusCode());
        HttpEntity entity = response.getEntity();
        assertTrue(entity.getContentLength() > 0l);
        assertEquals(JSON, entity.getContentType().getValue());
        Header range = response.getFirstHeader(HttpHeaders.Names.CONTENT_RANGE);
        assertNotNull(range);
        assertEquals("items 0-2/3", range.getValue());
        LittleO[] result = TestUtilities.deserialize(EntityUtils.toString(entity), LittleO[].class, server.serializationProvider()
                .processor(Format.JSON.getMediaType()));
        verifyList(result);
        request.releaseConnection();
    }

    @Test
    public void shouldNotContainContentRangeHeaderOnInvalidAcceptHeader() throws Exception {
        server.bind(SERVER_PORT);

        HttpGet request = new HttpGet(LITTLE_OS_URL);
        request.addHeader(HttpHeaders.Names.ACCEPT, "no-good/no-good");
        HttpResponse response = (HttpResponse) http.execute(request);
        assertEquals(HttpResponseStatus.NOT_ACCEPTABLE.getCode(), response.getStatusLine().getStatusCode());
        HttpEntity entity = response.getEntity();
        assertTrue(entity.getContentLength() > 0l);
        assertEquals(JSON, entity.getContentType().getValue());
        assertNull(response.getFirstHeader(HttpHeaders.Names.CONTENT_RANGE));
        assertEquals(406, response.getStatusLine().getStatusCode());
        request.releaseConnection();
    }

    @Test
    public void shouldSerializeObjectAsWrappedJson() throws Exception {
        server.bind(SERVER_PORT);
        HttpGet request = new HttpGet(LITTLE_O_URL + ".wjson");
        HttpResponse response = (HttpResponse) http.execute(request);
        assertEquals(HttpResponseStatus.OK.getCode(), response.getStatusLine().getStatusCode());
        HttpEntity entity = response.getEntity();
        assertTrue(entity.getContentLength() > 0l);
        assertEquals(JSON, entity.getContentType().getValue());
        String result = EntityUtils.toString(entity);
        LittleO o = TestUtilities.deserialize(result, LittleO.class,
                server.serializationProvider().processor(Format.WJSON.getMediaType()));
        verifyObject(o);
        request.releaseConnection();
    }

    @Test
    public void shouldSerializeListAsWrappedJson() throws Exception {
        server.bind(SERVER_PORT);

        HttpGet request = new HttpGet(LITTLE_OS_URL + ".wjson");
        HttpResponse response = (HttpResponse) http.execute(request);
        assertEquals(HttpResponseStatus.OK.getCode(), response.getStatusLine().getStatusCode());
        HttpEntity entity = response.getEntity();
        assertTrue(entity.getContentLength() > 0l);
        assertEquals(JSON, entity.getContentType().getValue());
        Header range = response.getFirstHeader(HttpHeaders.Names.CONTENT_RANGE);
        assertNotNull(range);
        assertEquals("items 0-2/3", range.getValue());
        String result = EntityUtils.toString(entity);
        assertEquals(200, response.getStatusLine().getStatusCode());
        LittleO[] o = TestUtilities.deserialize(result, LittleO[].class,
                server.serializationProvider().processor(Format.WJSON.getMediaType()));
        verifyList(o);
        request.releaseConnection();
    }

    @Test
    public void shouldSerializeObjectAsXml() throws Exception {
        server.bind(SERVER_PORT);

        HttpGet request = new HttpGet(LITTLE_O_URL + ".xml");
        HttpResponse response = (HttpResponse) http.execute(request);
        assertEquals(HttpResponseStatus.OK.getCode(), response.getStatusLine().getStatusCode());
        HttpEntity entity = response.getEntity();
        assertTrue(entity.getContentLength() > 0l);
        assertEquals(XML, entity.getContentType().getValue());
        LittleO o = TestUtilities.deserialize(EntityUtils.toString(entity), LittleO.class,
                server.serializationProvider().processor(Format.XML.getMediaType()));
        verifyObject(o);
        request.releaseConnection();
    }

    @Test
    public void shouldSerializeListAsXml() throws Exception {
        server.bind(SERVER_PORT);

        HttpGet request = new HttpGet(LITTLE_OS_URL + ".xml");
        HttpResponse response = (HttpResponse) http.execute(request);
        assertEquals(HttpResponseStatus.OK.getCode(), response.getStatusLine().getStatusCode());
        HttpEntity entity = response.getEntity();
        assertTrue(entity.getContentLength() > 0l);
        assertEquals(XML, entity.getContentType().getValue());
        Header range = response.getFirstHeader(HttpHeaders.Names.CONTENT_RANGE);
        assertNotNull(range);
        assertEquals("items 0-2/3", range.getValue());
        LittleO[] o = TestUtilities.deserialize(EntityUtils.toString(entity), new LittleO[0].getClass(), server
                .serializationProvider().processor(Format.XML.getMediaType()));
        verifyList(o);
        request.releaseConnection();
    }

    @Test
    public void shouldSerializeObjectAsWrappedXml() throws Exception {
        server.bind(SERVER_PORT);

        HttpGet request = new HttpGet(LITTLE_O_URL + ".wxml");
        HttpResponse response = (HttpResponse) http.execute(request);
        assertEquals(HttpResponseStatus.OK.getCode(), response.getStatusLine().getStatusCode());
        HttpEntity entity = response.getEntity();
        assertTrue(entity.getContentLength() > 0l);
        assertEquals(XML, entity.getContentType().getValue());
        LittleO o = TestUtilities.deserialize(EntityUtils.toString(entity), LittleO.class,
                server.serializationProvider().processor(Format.WXML.getMediaType()));
        verifyObject(o);
        request.releaseConnection();
    }

    @Test
    public void shouldSerializeListAsWrappedXml() throws Exception {
        server.bind(SERVER_PORT);

        HttpGet request = new HttpGet(LITTLE_OS_URL + ".wxml");
        HttpResponse response = (HttpResponse) http.execute(request);
        assertEquals(HttpResponseStatus.OK.getCode(), response.getStatusLine().getStatusCode());
        HttpEntity entity = response.getEntity();
        assertTrue(entity.getContentLength() > 0l);
        assertEquals(XML, entity.getContentType().getValue());
        Header range = response.getFirstHeader(HttpHeaders.Names.CONTENT_RANGE);
        assertNotNull(range);
        assertEquals("items 0-2/3", range.getValue());

        LittleO[] o = TestUtilities.deserialize(EntityUtils.toString(entity), new LittleO[0].getClass(), server
                .serializationProvider().processor(Format.WXML.getMediaType()));
        verifyList(o);
        request.releaseConnection();
    }

    @Test
    public void shouldSerializeApplicationHalJson() throws Exception {
        server.bind(SERVER_PORT);

        HttpGet request = new HttpGet(LITTLE_OS_URL);
        request.addHeader(HttpHeaders.Names.ACCEPT, "application/hal+json");
        HttpResponse response = (HttpResponse) http.execute(request);
        assertEquals(HttpResponseStatus.OK.getCode(), response.getStatusLine().getStatusCode());
        HttpEntity entity = response.getEntity();
        assertTrue(entity.getContentLength() > 0l);
        assertEquals(HAL_JSON, entity.getContentType().getValue());
        Header range = response.getFirstHeader(HttpHeaders.Names.CONTENT_RANGE);
        assertNotNull(range);
        assertEquals("items 0-2/3", range.getValue());
        LittleO[] os = TestUtilities.deserialize(EntityUtils.toString(entity), LittleO[].class, server.serializationProvider()
                .processor(Format.JSON.getMediaType()));
        verifyList(os);
        request.releaseConnection();
    }

    @Test
    public void shouldSerializeUnmappedPreprocessorException() throws Exception {
        server.addPreprocessor(new ErrorPreprocessor());
        server.bind(SERVER_PORT);

        HttpGet request = new HttpGet(LITTLE_OS_URL);
        HttpResponse response = (HttpResponse) http.execute(request);
        assertEquals(HttpResponseStatus.INTERNAL_SERVER_ERROR.getCode(), response.getStatusLine().getStatusCode());
        HttpEntity entity = response.getEntity();
        assertTrue(entity.getContentLength() > 0l);
        assertEquals(JSON, entity.getContentType().getValue());
        assertNull(response.getFirstHeader(HttpHeaders.Names.CONTENT_RANGE));
        String entityString = EntityUtils.toString(entity);
        assertNotNull(entityString);
        assertTrue(entityString.contains("\"httpStatus\":500"));
        assertTrue(entityString.contains("\"message\":\"ErrorPreprocessor\""));
        assertTrue(entityString.contains("\"errorType\":\"RuntimeException\""));
        request.releaseConnection();
    }

    @Test
    public void shouldSerializeMappedPreprocessorException() throws Exception {
        server.addPreprocessor(new ErrorPreprocessor());
        server.bind(SERVER_PORT);

        HttpGet request = new HttpGet(LITTLE_OS_URL);
        HttpResponse response = (HttpResponse) http.execute(request);
        assertEquals(HttpResponseStatus.INTERNAL_SERVER_ERROR.getCode(), response.getStatusLine().getStatusCode());
        HttpEntity entity = response.getEntity();
        assertTrue(entity.getContentLength() > 0l);
        assertEquals(JSON, entity.getContentType().getValue());
        assertNull(response.getFirstHeader(HttpHeaders.Names.CONTENT_RANGE));
        String entityString = EntityUtils.toString(entity);
        assertNotNull(entityString);
        assertTrue(entityString.contains("\"httpStatus\":500"));
        assertTrue(entityString.contains("\"message\":\"ErrorPreprocessor\""));
        assertTrue(entityString.contains("\"errorType\":\"RuntimeException\""));
        request.releaseConnection();
    }

    @Test
    public void shouldSerializeUnmappedException() throws Exception {
        server.bind(SERVER_PORT);

        HttpGet request = new HttpGet(URL_EXCEPTION_LITTLE_O);
        HttpResponse response = (HttpResponse) http.execute(request);
        assertEquals(HttpResponseStatus.INTERNAL_SERVER_ERROR.getCode(), response.getStatusLine().getStatusCode());
        HttpEntity entity = response.getEntity();
        assertTrue(entity.getContentLength() > 0l);
        assertEquals(JSON, entity.getContentType().getValue());
        assertNull(response.getFirstHeader(HttpHeaders.Names.CONTENT_RANGE));
        String entityString = EntityUtils.toString(entity);
        assertNotNull(entityString);
        assertTrue(entityString.contains("\"httpStatus\":500"));
        assertTrue(entityString.contains("\"message\":\"ObjectTestController\""));
        assertTrue(entityString.contains("\"errorType\":\"NullPointerException\""));
        request.releaseConnection();
    }

    @Test
    public void shouldSerializeMappedException() throws Exception {
        server.bind(SERVER_PORT);

        HttpGet request = new HttpGet(URL_EXCEPTION_LITTLE_O);
        HttpResponse response = (HttpResponse) http.execute(request);
        assertEquals(HttpResponseStatus.INTERNAL_SERVER_ERROR.getCode(), response.getStatusLine().getStatusCode());
        HttpEntity entity = response.getEntity();
        assertTrue(entity.getContentLength() > 0l);
        assertEquals(JSON, entity.getContentType().getValue());
        assertNull(response.getFirstHeader(HttpHeaders.Names.CONTENT_RANGE));
        String entityString = EntityUtils.toString(entity);
        assertNotNull(entityString);
        assertTrue(entityString.contains("\"httpStatus\":500"));
        assertTrue(entityString.contains("\"message\":\"ObjectTestController\""));
        assertTrue(entityString.contains("\"errorType\":\"NullPointerException\""));
        request.releaseConnection();
    }

    @Test
    public void shouldCallFinallyProcessorOnException() throws Exception {
        TestPostprocessor postprocessor = new TestPostprocessor();
        server.addFinallyProcessor(postprocessor);
        server.bind(SERVER_PORT);

        HttpGet request = new HttpGet(URL_EXCEPTION_LITTLE_O);
        HttpResponse response = (HttpResponse) http.execute(request);
        assertEquals(HttpResponseStatus.INTERNAL_SERVER_ERROR.getCode(), response.getStatusLine().getStatusCode());
        assertEquals(1, postprocessor.callCount());
        request.releaseConnection();
    }

    @Test
    public void shouldCallFinallyProcessorOnPreprocessorException() throws Exception {
        TestPostprocessor postprocessor = new TestPostprocessor();
        server.addFinallyProcessor(postprocessor);
        server.addPreprocessor(new ErrorPreprocessor());
        server.bind(SERVER_PORT);

        HttpGet request = new HttpGet(LITTLE_OS_URL);
        HttpResponse response = (HttpResponse) http.execute(request);
        assertEquals(HttpResponseStatus.INTERNAL_SERVER_ERROR.getCode(), response.getStatusLine().getStatusCode());
        assertEquals(1, postprocessor.callCount());
        request.releaseConnection();
    }

    private void verifyObject(LittleO o) {
        assertNotNull(o);
        LittleO expected = new LittleO();
        assertEquals(expected.getName(), o.getName());
        assertEquals(expected.getInteger(), o.getInteger());
        assertEquals(expected.isBoolean(), o.isBoolean());
        assertEquals(3, o.getChildren().size());
        assertEquals(3, o.getArray().length);
    }

    private void verifyList(LittleO[] result) {
        assertEquals(3, result.length);
        assertEquals("name", result[0].getName());
    }

    // SECTION: INNER CLASSES

    @SuppressWarnings("unused")
    private class StringTestController {
        public String create(Request request, Response response) {
            response.setResponseCreated();
            return "create";
        }

        public String read(Request request, Response response) {
            return "read";
        }

        public String update(Request request, Response response) {
            return "update";
        }

        public String delete(Request request, Response response) {
            return "delete";
        }

        public String readAll(Request request, Response response) {
            return "readAll";
        }

        public void throwException(Request request, Response response) throws Exception {
            throw new NullPointerException(this.getClass().getSimpleName());
        }
    }

    @SuppressWarnings("unused")
    private class ObjectTestController {
        public LittleO read(Request request, Response Response) {
            return newLittleO(3);
        }

        public void throwException(Request request, Response response) throws Exception {
            throw new NullPointerException(this.getClass().getSimpleName());
        }

        public List<LittleO> readAll(Request request, Response response) {
            QueryRange range = new QueryRange(0, 3);
            response.addRangeHeader(range, 3);
            List<LittleO> l = new ArrayList<LittleO>();
            l.add(newLittleO(1));
            l.add(newLittleO(2));
            l.add(newLittleO(3));
            return l;
        }

        private LittleO newLittleO(int count) {
            LittleO l = new LittleO();
            List<LittleO> list = new ArrayList<LittleO>(count);

            for (int i = 0; i < count; i++) {
                list.add(new LittleO());
            }

            l.setChildren(list);
            return l;
        }
    }
}
