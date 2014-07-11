/*
    Copyright 2011, Strategic Gains, Inc.

	Licensed under the Apache License, Version 2.0 (the "License");
	you may not use this file except in compliance with the License.
	You may obtain a copy of the License at

		http://www.apache.org/licenses/LICENSE-2.0

	Unless required by applicable law or agreed to in writing, software
	distributed under the License is distributed on an "AS IS" BASIS,
	WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
	See the License for the specific language governing permissions and
	limitations under the License.
*/
package org.restexpress;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.impl.client.DefaultHttpClient;
import org.jboss.netty.handler.codec.http.HttpHeaders;
import org.junit.Test;
import org.restexpress.Request;
import org.restexpress.Response;
import org.restexpress.RestExpress;


/**
 * @author toddf
 * @since Jan 28, 2011
 */
public class RestExpressTest
{
	private static final String TEST_PATH = "/restexpress/test1";
	private static final int TEST_PORT = 8901;
	private static final String TEST_URL = "http://localhost:" + TEST_PORT + TEST_PATH;

	private RestExpress server = new RestExpress();

//	@Test
//	public void shouldUseDefaults()
//	{
//		assertEquals(Format.JSON, server.getDefaultFormat());
//		assertTrue(server.getResponseProcessors().containsKey(Format.JSON));
//		assertTrue(server.getResponseProcessors().containsKey(Format.XML));
//		assertEquals(2, server.getResponseProcessors().size());
//
//		assertEquals(0, server.getPort());
//		assertTrue(server.getMessageObservers().isEmpty());
//		assertTrue(server.getPostprocessors().isEmpty());
//		assertTrue(server.getPreprocessors().isEmpty());
//		assertTrue(server.shouldUseSystemOut());
//	}

//	@Test
//	public void shouldDisableJson()
//	{
//		server.noJson();
//		assertEquals(Format.JSON, server.getDefaultFormat());
//		assertFalse(server.getResponseProcessors().containsKey(Format.JSON));
//		assertTrue(server.getResponseProcessors().containsKey(Format.XML));
//		assertEquals(1, server.getResponseProcessors().size());
//	}
	
//	@Test
//	public void shouldDisableXml()
//	{
//		server.noXml();
//		assertEquals(Format.JSON, server.getDefaultFormat());
//		assertTrue(server.getResponseProcessors().containsKey(Format.JSON));
//		assertFalse(server.getResponseProcessors().containsKey(Format.XML));
//		assertEquals(1, server.getResponseProcessors().size());
//	}
	
//	@Test
//	public void shouldMakeXmlDefault()
//	{
//		server.supportXml(true);
//		assertEquals(Format.XML, server.getDefaultFormat());
//		assertTrue(server.getResponseProcessors().containsKey(Format.JSON));
//		assertTrue(server.getResponseProcessors().containsKey(Format.XML));
//		assertEquals(2, server.getResponseProcessors().size());
//	}

//	@Test
//	public void shouldCustomizeJsonSerializer()
//	{
//		server.putResponseProcessor(Format.JSON, provider.newProcessor(Format.JSON));
//		assertEquals(Format.JSON, server.getDefaultFormat());
//		assertTrue(server.getResponseProcessors().containsKey(Format.JSON));
//		assertTrue(server.getResponseProcessors().containsKey(Format.XML));
//		assertEquals(2, server.getResponseProcessors().size());
//	}

//	@Test
//	public void shouldCustomizeXmlSerializer()
//	{
//		server.putResponseProcessor(Format.XML, provider.newProcessor(Format.XML));
//		assertEquals(Format.JSON, server.getDefaultFormat());
//		assertTrue(server.getResponseProcessors().containsKey(Format.JSON));
//		assertTrue(server.getResponseProcessors().containsKey(Format.XML));
//		assertEquals(2, server.getResponseProcessors().size());
//	}

//	@Test
//	public void shouldNotUpdateJsonSerializer()
//	{
//		ResponseProcessor rp = provider.newProcessor(Format.JSON);
//		server.putResponseProcessor(Format.JSON, rp);
//		server.supportJson(true);
//		assertEquals(Format.JSON, server.getDefaultFormat());
//		assertTrue(server.getResponseProcessors().containsKey(Format.JSON));
//		assertTrue(server.getResponseProcessors().containsKey(Format.XML));
//		assertEquals(2, server.getResponseProcessors().size());
//		
//		assertTrue(rp == server.getResponseProcessors().get(Format.JSON));
//	}

//	@Test
//	public void shouldNotUpdateXmlSerializer()
//	{
//		ResponseProcessor rp = provider.newProcessor(Format.XML);
//		server.putResponseProcessor(Format.XML, rp);
//		server.supportXml(true);
//		assertEquals(Format.XML, server.getDefaultFormat());
//		assertTrue(server.getResponseProcessors().containsKey(Format.JSON));
//		assertTrue(server.getResponseProcessors().containsKey(Format.XML));
//		assertEquals(2, server.getResponseProcessors().size());
//		
//		assertTrue(rp == server.getResponseProcessors().get(Format.XML));
//	}
	
	@Test
	public void shouldNotUseSystemOut()
	{
		server.noSystemOut();
		assertFalse(server.shouldUseSystemOut());
	}

	@Test
	public void shouldCallDefaultMethods()
	throws ClientProtocolException, IOException
	{
		RestExpress re = new RestExpress();
		NoopController controller = new NoopController();
		re.uri(TEST_PATH, controller);
		re.bind(TEST_PORT);
		
		HttpClient client = new DefaultHttpClient();
		HttpGet get = new HttpGet(TEST_URL);
		HttpResponse response = (HttpResponse) client.execute(get);
		assertEquals(200, response.getStatusLine().getStatusCode());
		assertEquals(1, controller.read);
		assertEquals(0, controller.create);
		assertEquals(0, controller.update);
		assertEquals(0, controller.delete);
		get.releaseConnection();
		
		HttpPost post = new HttpPost(TEST_URL);
		response = (HttpResponse) client.execute(post);
		assertEquals(201, response.getStatusLine().getStatusCode());
		assertEquals(1, controller.create);
		assertEquals(1, controller.read);
		assertEquals(0, controller.update);
		assertEquals(0, controller.delete);
		post.releaseConnection();

		HttpPut put = new HttpPut(TEST_URL);
		response = (HttpResponse) client.execute(put);
		assertEquals(200, response.getStatusLine().getStatusCode());
		assertEquals(1, controller.update);
		assertEquals(1, controller.read);
		assertEquals(1, controller.create);
		assertEquals(0, controller.delete);
		put.releaseConnection();

		HttpDelete delete = new HttpDelete(TEST_URL);
		response = (HttpResponse) client.execute(delete);
		assertEquals(200, response.getStatusLine().getStatusCode());
		assertEquals(1, controller.delete);
		assertEquals(1, controller.read);
		assertEquals(1, controller.create);
		assertEquals(1, controller.update);
		delete.releaseConnection();

		re.shutdown();
	}

	@Test
	public void shouldSetOutputMediaType()
	throws ClientProtocolException, IOException
	{
		RestExpress re = new RestExpress();
		NoopController controller = new NoopController();
		re.uri(TEST_PATH, controller);
		re.bind(TEST_PORT);

		HttpClient client = new DefaultHttpClient();

		HttpPost post = new HttpPost(TEST_URL);
		post.addHeader(HttpHeaders.Names.ACCEPT, "application/json");
		HttpResponse response = (HttpResponse) client.execute(post);
		assertEquals(201, response.getStatusLine().getStatusCode());
		assertEquals(ContentType.JSON, controller.outputMediaType);
		post.releaseConnection();

		HttpGet get = new HttpGet(TEST_URL);
		get.addHeader(HttpHeaders.Names.ACCEPT, "application/json");
		response = (HttpResponse) client.execute(get);
		assertEquals(200, response.getStatusLine().getStatusCode());
		assertEquals(ContentType.JSON, controller.outputMediaType);
		get.releaseConnection();

		HttpPut put = new HttpPut(TEST_URL);
		put.addHeader(HttpHeaders.Names.ACCEPT, "application/json");
		response = (HttpResponse) client.execute(put);
		assertEquals(200, response.getStatusLine().getStatusCode());
		assertEquals(ContentType.JSON, controller.outputMediaType);
		put.releaseConnection();

		HttpDelete delete = new HttpDelete(TEST_URL);
		delete.addHeader(HttpHeaders.Names.ACCEPT, "application/json");
		response = (HttpResponse) client.execute(delete);
		assertEquals(200, response.getStatusLine().getStatusCode());
		assertEquals(ContentType.JSON, controller.outputMediaType);
		delete.releaseConnection();

		re.shutdown();
	}

	public class NoopController
    {
		int create, read, update, delete = 0;
		String outputMediaType;

		public void create(Request req, Response res)
		{
			++create;
			res.setResponseCreated();
			outputMediaType = res.getSerializationSettings().getMediaType();
		}

		public void read(Request req, Response res)
		{
			++read;
			outputMediaType = res.getSerializationSettings().getMediaType();
		}

		public void update(Request req, Response res)
		{
			++update;
			outputMediaType = res.getSerializationSettings().getMediaType();
		}

		public void delete(Request req, Response res)
		{
			++delete;
			outputMediaType = res.getSerializationSettings().getMediaType();
		}
    }
}
