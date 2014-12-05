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
package org.restexpress.pipeline;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.intelligentsia.commons.http.exception.HttpRuntimeException;
import org.intelligentsia.commons.http.status.HttpResponseStandardStatus;
import org.jboss.netty.handler.codec.http.HttpMethod;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.restexpress.Request;
import org.restexpress.Response;
import org.restexpress.RestExpress;
import org.restexpress.domain.CharacterSet;
import org.restexpress.domain.MediaType;
import org.restexpress.domain.response.ErrorResult;
import org.restexpress.response.ResponseWrapper;

import com.google.common.io.Files;

/**
 * {@link PipelineExceptionTest} introduce some special behavior test case.
 * 
 * @author <a href="mailto:jguibert@intelligents-ia.com" >Jerome Guibert</a>
 */
public class PipelineExceptionTest {

	private static final int TEST_PORT = 8081;
	private static final String TEST_URL = "http://localhost:" + TEST_PORT;
	private static String JSON = MediaType.APPLICATION_JSON.withCharset(CharacterSet.UTF_8.getCharsetName());
	private static String TEXT_PLAIN = MediaType.TEXT_PLAIN.withCharset(CharacterSet.UTF_8.getCharsetName());
	private static String TEXT_ALL = MediaType.TEXT_ALL.withCharset(CharacterSet.UTF_8.getCharsetName());

	protected RestExpress restExpress;
	protected FailurePostprocessor failurePostprocessor;
	protected HttpClient client;

	/**
	 * Simple test case to check what happen when exception occurs using
	 * serialization. We can see that {@link ResponseWrapper} doing their jobs.
	 * 
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	@Test
	public void checkResponseBodyOnErrorWithSerializationEnabled() throws ClientProtocolException, IOException {
		HttpGet request = new HttpGet(TEST_URL + "/serializationEnabled.json");

		failurePostprocessor.setMustFail(Boolean.FALSE);
		HttpResponse response = (HttpResponse) client.execute(request);
		assertEquals(200, response.getStatusLine().getStatusCode());
		HttpEntity entity = response.getEntity();
		assertEquals(JSON, entity.getContentType().getValue());
		assertEquals("{\"message\":\"A simple content\"}", EntityUtils.toString(entity));
		request.releaseConnection();

		failurePostprocessor.setMustFail(Boolean.TRUE);
		response = (HttpResponse) client.execute(request);
		entity = response.getEntity();
		assertEquals(JSON, entity.getContentType().getValue());
		assertEquals(417, response.getStatusLine().getStatusCode());
		assertEquals("{\"httpStatus\":417,\"message\":\"oups\",\"errorType\":\"HttpRuntimeException\"}", EntityUtils.toString(entity));
		request.releaseConnection();
	}

	/**
	 * Simple test case to check what happen when exception occurs with no
	 * serialization and using JSON Format:
	 * <ul>
	 * <li>Without exception, we did not manage content type and serialization.</li>
	 * <li>With an exception, we manage both of them, according wrapper used</li>
	 * </ul>
	 * 
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	@Test
	public void checkResponseBodyOnErrorWithSerializationDisabledWithJson() throws ClientProtocolException, IOException {
		HttpGet request = new HttpGet(TEST_URL + "/serializationDisabled.json");

		failurePostprocessor.setMustFail(Boolean.FALSE);
		HttpResponse response = (HttpResponse) client.execute(request);
		assertEquals(200, response.getStatusLine().getStatusCode());
		HttpEntity entity = response.getEntity();
		// here we did not manage content type without exception
		// text plain is the default content type
		assertEquals(TEXT_PLAIN, entity.getContentType().getValue());
		assertNotEquals(JSON, entity.getContentType().getValue());
		// NO json serialization automatically
		assertEquals("A simple content", EntityUtils.toString(entity));
		request.releaseConnection();

		failurePostprocessor.setMustFail(Boolean.TRUE);
		response = (HttpResponse) client.execute(request);
		entity = response.getEntity();
		// here we manage content type
		assertNotEquals(TEXT_PLAIN, entity.getContentType().getValue());
		assertEquals(JSON, entity.getContentType().getValue());
		assertEquals(417, response.getStatusLine().getStatusCode());
		// here error handling according requested format
		assertEquals("{\"httpStatus\":417,\"message\":\"oups\",\"errorType\":\"HttpRuntimeException\"}", EntityUtils.toString(entity));

		request.releaseConnection();
	}

	/**
	 * Simple test case to check what happen when exception occurs with no
	 * serialization and using TEXT Format.
	 * 
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	@Test
	public void checkResponseBodyOnErrorWithSerializationDisabledWithTxt() throws ClientProtocolException, IOException {
		HttpGet request = new HttpGet(TEST_URL + "/serializationDisabled.txt");

		failurePostprocessor.setMustFail(Boolean.FALSE);
		HttpResponse response = (HttpResponse) client.execute(request);
		assertEquals(200, response.getStatusLine().getStatusCode());
		HttpEntity entity = response.getEntity();
		// here we did not manage content type without exception
		// text plain is the default content type when none is set
		assertEquals(TEXT_PLAIN, entity.getContentType().getValue());
		// and no serialization
		assertEquals("A simple content", EntityUtils.toString(entity));
		request.releaseConnection();

		failurePostprocessor.setMustFail(Boolean.TRUE);
		response = (HttpResponse) client.execute(request);
		entity = response.getEntity();
		// here we manage content type
		// text all came from default media type
		assertEquals(TEXT_ALL, entity.getContentType().getValue());
		assertEquals(417, response.getStatusLine().getStatusCode());
		// here error handling according requested format
		ErrorResult errorResult = new ErrorResult(417, "oups", "HttpRuntimeException");
		assertEquals(errorResult.toString(), EntityUtils.toString(entity));

		request.releaseConnection();
	}

	/**
	 * Simple test case to check what happen when exception occurs with no
	 * serialization, using TEXT Format, with a specific content type
	 * 
	 * @throws ClientProtocolException
	 * 
	 * @throws IOException
	 */
	@Test
	public void checkResponseBodyOnErrorWithSerializationDisabledWithTxtAndContentType() throws ClientProtocolException, IOException {
		HttpGet request = new HttpGet(TEST_URL + "/serializationDisabledWithContentType.txt");

		failurePostprocessor.setMustFail(Boolean.FALSE);
		HttpResponse response = (HttpResponse) client.execute(request);
		assertEquals(200, response.getStatusLine().getStatusCode());
		HttpEntity entity = response.getEntity();
		// here we did not manage content type without exception
		// text plain is the content type from controller
		assertEquals(TEXT_ALL, entity.getContentType().getValue());
		// and no serialization
		assertEquals("A simple content", EntityUtils.toString(entity));
		request.releaseConnection();

		failurePostprocessor.setMustFail(Boolean.TRUE);
		response = (HttpResponse) client.execute(request);
		entity = response.getEntity();
		// here we manage content type
		// text all came from default media type
		assertEquals(TEXT_ALL, entity.getContentType().getValue());
		assertEquals(417, response.getStatusLine().getStatusCode());
		// here error handling according requested format
		ErrorResult errorResult = new ErrorResult(417, "oups", "HttpRuntimeException");
		assertEquals(errorResult.toString(), EntityUtils.toString(entity));

		request.releaseConnection();
	}

	@Test
	public void checkResponseBodyOnErrorWithFile() throws ClientProtocolException, IOException {
		HttpGet request = new HttpGet(TEST_URL + "/fileRequest.txt");

		failurePostprocessor.setMustFail(Boolean.FALSE);
		HttpResponse response = (HttpResponse) client.execute(request);
		assertEquals(200, response.getStatusLine().getStatusCode());
		HttpEntity entity = response.getEntity();
		assertEquals(TEXT_PLAIN, entity.getContentType().getValue());
		assertEquals("", EntityUtils.toString(entity));
		request.releaseConnection();

		failurePostprocessor.setMustFail(Boolean.TRUE);
		response = (HttpResponse) client.execute(request);
		entity = response.getEntity();

		assertEquals(TEXT_ALL, entity.getContentType().getValue());
		assertEquals(417, response.getStatusLine().getStatusCode());
		// here error handling according requested format
		ErrorResult errorResult = new ErrorResult(417, "oups", "HttpRuntimeException");
		assertEquals(errorResult.toString(), EntityUtils.toString(entity));
	}

	@Test
	public void checkReponseExceptionOnNoContent() throws ClientProtocolException, IOException {
		HttpGet request = new HttpGet(TEST_URL + "/noContent");

		failurePostprocessor.setMustFail(Boolean.FALSE);
		HttpResponse response = (HttpResponse) client.execute(request);
		assertEquals(204, response.getStatusLine().getStatusCode());
		HttpEntity entity = response.getEntity();
		// here we did not manage content type without exception
		// no content means also no content type
		assertEquals(null, entity);
		request.releaseConnection();

		failurePostprocessor.setMustFail(Boolean.TRUE);
		response = (HttpResponse) client.execute(request);
		entity = response.getEntity();
		// here we manage content type
		// text all came from default media type
		assertEquals(JSON, entity.getContentType().getValue());
		assertEquals(417, response.getStatusLine().getStatusCode());
		// here error handling according default processor format
		assertEquals("{\"httpStatus\":417,\"message\":\"oups\",\"errorType\":\"HttpRuntimeException\"}", EntityUtils.toString(entity));
	}

	@Before
	public void initialize() {
		client = new DefaultHttpClient();

		restExpress = new RestExpress();
		NoopController controller = new NoopController();
		restExpress.uri("/serializationEnabled.{format}", controller)//
				.name("route.noop.serializationEnabled")//
				.action("serializationEnabled", HttpMethod.GET)//
				.performSerialization();

		restExpress.uri("/serializationDisabled.{format}", controller)//
				.name("route.noop.serializationDisabled")//
				.action("serializationDisabled", HttpMethod.GET)//
				.noSerialization();

		restExpress.uri("/serializationDisabledWithContentType.{format}", controller)//
				.name("route.noop.serializationDisabledWithContentType")//
				.action("serializationDisabledWithContentType", HttpMethod.GET)//
				.noSerialization();

		// builder set no serialization for us
		restExpress.uri("/fileRequest.{format}", controller)//
				.name("route.noop.fileRequest")//
				.action("fileRequest", HttpMethod.GET);

		restExpress.uri("/noContent", controller)//
				.name("route.noop.noContent")//
				.action("noContent", HttpMethod.GET);

		failurePostprocessor = new FailurePostprocessor();
		restExpress.addPostprocessor(failurePostprocessor);
		restExpress.bind();
	}

	@After
	public void destroy() {
		restExpress.shutdown();
	}

	/**
	 * {@link FailurePostprocessor} to introduce a failure after response body
	 * handler.
	 * 
	 * @author <a href="mailto:jguibert@intelligents-ia.com" >Jerome Guibert</a>
	 * 
	 */
	public class FailurePostprocessor implements Postprocessor {

		private boolean mustFail = true;

		public boolean isMustFail() {
			return mustFail;
		}

		public void setMustFail(boolean mustFail) {
			this.mustFail = mustFail;
		}

		@Override
		public void process(MessageContext context) {
			if (mustFail)
				throw new HttpRuntimeException(HttpResponseStandardStatus.EXPECTATION_FAILED, "oups");
		}
	}

	/**
	 * {@link NoopController} inner controller for our special test case.
	 * 
	 * @author <a href="mailto:jguibert@intelligents-ia.com" >Jerome Guibert</a>
	 * 
	 */
	public class NoopController {

		private File hello = null;

		public void serializationEnabled(Request request, Response response) {
			response.setBody(new DummyDto("A simple content"));
		}

		public void serializationDisabled(Request request, Response response) {
			response.setBody("A simple content");
		}

		public void serializationDisabledWithContentType(Request request, Response response) {
			response.setBody("A simple content");
			response.setContentType(TEXT_ALL);
		}

		public File fileRequest(Request request, Response response) throws IOException {
			if (hello == null) {
				hello = new File(Files.createTempDir(), "hello.txt");
			}
			return hello;
		}

		public void noContent(Request request, Response response) {
			response.setResponseNoContent();
		}

	}

	public class DummyDto implements Serializable {
		private static final long serialVersionUID = 1L;
		protected String message;

		public DummyDto() {
			super();
		}

		public DummyDto(String message) {
			super();
			this.message = message;
		}

		public String getMessage() {
			return message;
		}

		public void setMessage(String message) {
			this.message = message;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + getOuterType().hashCode();
			result = prime * result + ((message == null) ? 0 : message.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			DummyDto other = (DummyDto) obj;
			if (!getOuterType().equals(other.getOuterType()))
				return false;
			if (message == null) {
				if (other.message != null)
					return false;
			} else if (!message.equals(other.message))
				return false;
			return true;
		}

		private PipelineExceptionTest getOuterType() {
			return PipelineExceptionTest.this;
		}

	}
}
