package org.restexpress.plugin.jaxrs;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.restexpress.RestExpressService;
import org.restexpress.observer.SimpleConsoleLogMessageObserver;

/**
 * {@link EchoTest}
 * 
 * 
 */
public class EchoTest {
    private RestExpressService server;

    private HttpClient http = new DefaultHttpClient();

    @Before
    public void createServer() {
        server = RestExpressService.newBuilder();
        server.addMessageObserver(new SimpleConsoleLogMessageObserver());
        server.bind();
    }

    @After
    public void shutdownServer() {
        server.shutdown();
    }

    @Test
    @SuppressWarnings("unused")
    public void shouldHandleGetRequests() throws Exception {

        HttpGet request = new HttpGet("http://localhost:8081/");
        HttpResponse response = (HttpResponse) http.execute(request);
        // assertEquals(HttpResponseStatus.OK.getCode(), response.getStatusLine().getStatusCode());
        // HttpEntity entity = response.getEntity();

        request.releaseConnection();
    }
}
