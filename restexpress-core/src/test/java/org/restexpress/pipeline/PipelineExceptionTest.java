package org.restexpress.pipeline;

import static org.junit.Assert.assertEquals;

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
import org.restexpress.response.ResponseWrapper;

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
	 * serialization. We can see that {@link ResponseWrapper} NOT doing their
	 * jobs.
	 * 
	 * <p>
	 * TODO fix that
	 * 
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	@Test(expected = org.junit.ComparisonFailure.class)
	public void checkResponseBodyOnErrorWithSerializationDisabled() throws ClientProtocolException, IOException {
		HttpGet request = new HttpGet(TEST_URL + "/serializationDisabled.json");

		failurePostprocessor.setMustFail(Boolean.FALSE);
		HttpResponse response = (HttpResponse) client.execute(request);
		assertEquals(200, response.getStatusLine().getStatusCode());
		HttpEntity entity = response.getEntity();
		// here we did not handle format automatically
		assertEquals(TEXT_PLAIN, entity.getContentType().getValue());
		assertEquals("A simple content", EntityUtils.toString(entity));
		request.releaseConnection();

		failurePostprocessor.setMustFail(Boolean.TRUE);
		response = (HttpResponse) client.execute(request);
		entity = response.getEntity();
		assertEquals(TEXT_PLAIN, entity.getContentType().getValue());
		assertEquals(417, response.getStatusLine().getStatusCode());
		assertEquals("oups", EntityUtils.toString(entity));
		request.releaseConnection();
	}

	@Test
	public void checkResponseBodyOnErrorWithFile() {

	}

	@Test
	public void checkReponseExceptionOnNoContent() {

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

		public void serializationEnabled(Request request, Response response) {
			response.setBody(new DummyDto("A simple content"));
		}

		public void serializationDisabled(Request request, Response response) {
			response.setBody("A simple content");
		}

		public File fileRequest(Request request, Response response) {

			return null;
		}

		public void noContent(Request request, Response response) {
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
