package org.restexpress.pipeline.observer;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.restexpress.Request;
import org.restexpress.Response;
import org.restexpress.pipeline.MessageObserver;

/**
 * {@link AccesLogMessageObserver} add an access log observer.
 * 
 * @author <a href="mailto:jguibert@intelligents-ia.com" >Jerome Guibert</a>
 * 
 */
public class AccesLogMessageObserver extends MessageObserver {

	private final Map<String, Long> timers = new ConcurrentHashMap<String, Long>();
	private final OutputStream outputStream;
	private final Charset charset;

	/**
	 * Build a new instance of {@link AccesLogMessageObserver}.
	 * 
	 * @param outputStream
	 *            {@link OutputStream} instance which can be obtained by a
	 *            <code>Files.newOutputStream</code> for example.
	 */
	public AccesLogMessageObserver(OutputStream outputStream) {
		super();
		this.outputStream = outputStream;
		charset = Charset.forName("UTF-8");
	}

	@Override
	protected void onReceived(final Request request, final Response response) {
		timers.put(request.getCorrelationId(), System.currentTimeMillis());
	}

	@Override
	protected void onComplete(Request request, Response response) {
		final Long stop = System.currentTimeMillis();
		final Long start = timers.remove(request.getCorrelationId());

		final StringBuffer sb = new StringBuffer(request.getEffectiveHttpMethod().toString());
		sb.append(" ");
		sb.append(request.getUrl());

		if (start != null) {
			sb.append(" responded with ");
			sb.append(response.getResponseStatus().toString());
			sb.append(" in ");
			sb.append(stop - start);
			sb.append(" ms");
		} else {
			sb.append(" responded with ");
			sb.append(response.getResponseStatus().toString());
		}

		byte[] bytes = sb.toString().getBytes(charset);
		try {
			outputStream.write(bytes, 0, bytes.length);
		} catch (IOException e) {
			// we just lost access log
		}

	}

	@Override
	protected void onException(Throwable exception, Request request, Response response) {
		// nothing
	}

	@Override
	protected void onSuccess(Request request, Response response) {
		// nothing
	}
}
